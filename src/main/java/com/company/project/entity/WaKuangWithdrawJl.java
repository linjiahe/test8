package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.company.project.vo.req.PageReqVO;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description 提现奖励记录 
 * @Author  yzz
 * @Date 2023-03-03 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangWithdrawJl extends PageReqVO implements Serializable {

	private static final long serialVersionUID =  8617584418855081684L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 币种
	 */
	private String coin;

	/**
	 * 金额
	 */
	private BigDecimal balance;

	/**
	 * 类型（1：提现，2：兑换）
	 */
	private Integer types;


	/**
	 * 来源类型（1：团队奖励，2：直推奖励，3：质押奖励）
	 */
	private Integer sourceType;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

}
