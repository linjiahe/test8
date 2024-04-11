package com.company.project.common.job.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangWallet;
import com.company.project.service.WaKuangJlService;
import com.company.project.service.WaKuangWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * testTask为spring bean的名称， 方法名称必须是run
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@Component("jlTask")
public class JlTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private WaKuangJlService waKuangJlService;

	@Resource
	private WaKuangWalletService newWalletService;

	/**
	 * 每秒查出一条未发放的记录
	 * @param params
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void run(String params) throws Exception {

		// status:状态(0：未发放，1:已发放)
		// 查出一条未发放的奖励记录
        WaKuangJl jlRecord = waKuangJlService.getOne(Wrappers.<WaKuangJl>lambdaQuery()
				.eq(WaKuangJl::getStatus,0)
				.last("limit 1"));

		if (jlRecord == null) {
			return;
		}
		int version = jlRecord.getVersion();
		String userId = jlRecord.getUserId();

		// 如果为空，说明被删除了
		WaKuangWallet waKuangWallet = newWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, userId).last("limit 1"));
		if(waKuangWallet==null){
			// 将当前奖励记录状态标记为已修改
			if(waKuangJlService.updateByVersion(jlRecord.getId(),1,version)<0){
				throw new Exception("修改失败");
			}
		}else{
			if(!newWalletService.addBalance(userId, jlRecord.getCoin(), jlRecord.getBalance())){
				throw new Exception("修改失败");
			}
			// 将当前奖励记录状态标记为已修改
			if(waKuangJlService.updateByVersion(jlRecord.getId(),1,version)<0){
				throw new Exception("修改失败");
			}
		}
	}
}
