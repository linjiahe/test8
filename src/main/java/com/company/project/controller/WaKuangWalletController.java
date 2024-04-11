package com.company.project.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.company.project.service.*;
import com.company.project.util.HttpClientResult;
import com.company.project.util.HttpClientUtils;
import com.company.project.util.WalletUtilBsc;
import java.net.URLEncoder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.bitcoinj.crypto.MnemonicException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/data/wakuang")
@Slf4j
public class WaKuangWalletController {

    @Resource
    private WaKuangWalletService waKuangWalletService;

    @Resource
    private StartAddressMapper startAddressMapper;

    @Resource
    private WaKuangZyMapper waKuangZyMapper;

    @Resource
    private WaKuangJlMapper waKuangJlMapper;

    @Resource
    private WaKuangJl2Mapper waKuangJl2Mapper;

    @Resource
    private WaKuangVersionMapper waKuangVersionMapper;

    @Resource
    private LunbotuMapper lunbotuMapper;

    @Resource
    private BnbPriceMapper bnbPriceMapper;

    @Resource
    private WaKuangJlService waKuangJlService;

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private WaKuangWalletMapper waKuangWalletMapper;

    @Resource
    private WaKuangZhuanZhangMapper waKuangZhuanZhangMapper;

    @Resource
    private WaKuangJlZhuanZhangMapper waKuangJlZhuanZhangMapper;

    @Resource
    private WaKuangWalletRechareService waKuangWalletRechareService;

    @Resource
    private WaKuangShanduiMapper waKuangShanduiMapper;

    @PostMapping("/wallet/bnbToday")
    public DataResult bnbToday(@RequestBody WaKuangWallet wakuangWallet) throws Exception {
        // 查出bnb价格
        BigDecimal bnbPrice = new BigDecimal(0);
        BnbPrice bnbPrice1 = bnbPriceMapper.selectOne(null);
        bnbPrice = new BigDecimal(bnbPrice1.getImg());

        // 查出usdt数量
        BigDecimal usdtCount = new BigDecimal(0);
        Map<String,String> reqMap2 = new HashMap<>();
        reqMap2.put("address","0xcCe1b3fFdA960B8801A23Ae82C019C95365653CC");
        reqMap2.put("Contract","0x55d398326f99059fF775485246999027B3197955");
        HttpClientResult result2 = HttpClientUtils.doGet("http://103.231.254.152:21800/getContract",reqMap2);
        if (result2!=null) {
            JSONObject jsonObject = JSONObject.fromObject(result2.getContent());
            usdtCount = new BigDecimal(JSONObject.fromObject(jsonObject.get("data").toString()).get("balance").toString());
        }
        BigDecimal res = usdtCount.divide(bnbPrice,5,BigDecimal.ROUND_HALF_UP);
        return DataResult.success(res);
    }

    @PostMapping("/wallet/bnbToday2")
    public DataResult bnbToday2(@RequestBody WaKuangWallet wakuangWallet) throws Exception {

        // 查出bnb价格
        BigDecimal bnbPrice = new BigDecimal(0);
        Map<String,String> reqMap = new HashMap<>();
        reqMap.put("ids","binancecoin");
        reqMap.put("vs_currencies","usd");
        HttpClientResult result = HttpClientUtils.doGet("https://api.coingecko.com/api/v3/simple/price",reqMap);
        if (result!=null) {
            JSONObject jsonObject = JSONObject.fromObject(result.getContent());
            bnbPrice = new BigDecimal(JSONObject.fromObject(jsonObject.get("binancecoin").toString()).get("usd").toString());
        }
        return DataResult.success(bnbPrice);
    }

    @PostMapping("/wallet/addZichan")
    public DataResult addZichan(@RequestBody WaKuangWallet vo) throws Exception {

        if(!vo.getPwd().equals("24.@22ad..d02423pd")){
            return DataResult.fail("暂无权限");
        }

        WaKuangWallet waKuangWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, "Domi").eq(WaKuangWallet::getUserName, vo.getUserName()));
        waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().add(vo.getBalance()));
        waKuangWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(waKuangWallet);

        WaKuangJl2 waKuangJl2 = new WaKuangJl2();
        waKuangJl2.setUserId(vo.getUserName());
        waKuangJl2.setCoin("Domi");
        waKuangJl2.setBalance(vo.getBalance());
        waKuangJl2.setSourceType(99);
        waKuangJl2.setStatus(1);
        waKuangJl2.setCreateTime(new Date());
        waKuangJl2Mapper.insert(waKuangJl2);
        return DataResult.success();
    }

    @PostMapping("/wallet/lesZichan")
    public DataResult lesZichan(@RequestBody WaKuangWallet vo) throws Exception {

        if(!vo.getPwd().equals("24.@22ad..d02423pd")){
            return DataResult.fail("暂无权限");
        }

        WaKuangWallet waKuangWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, "Domi").eq(WaKuangWallet::getUserName, vo.getUserName()));
        if (waKuangWallet.getJltxBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().subtract(vo.getBalance()));
        waKuangWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(waKuangWallet);

        WaKuangJl2 waKuangJl2 = new WaKuangJl2();
        waKuangJl2.setUserId(vo.getUserName());
        waKuangJl2.setCoin("Domi");
        waKuangJl2.setBalance(vo.getBalance().multiply(new BigDecimal(-1)));
        waKuangJl2.setSourceType(99);
        waKuangJl2.setStatus(1);
        waKuangJl2.setCreateTime(new Date());
        waKuangJl2Mapper.insert(waKuangJl2);
        return DataResult.success();
    }

    @PostMapping("/wallet/pageInfo")
    public DataResult pageInfo(@RequestBody WaKuangWallet wakuangWallet) {
        return DataResult.success(waKuangWalletService.pageInfo(wakuangWallet)
        );
    }

    @PostMapping("/wallet/pageInfoExport")
    public void pageInfoExport(@RequestBody WaKuangWallet wakuangWallet,HttpServletResponse response) throws IOException {
       waKuangWalletService.pageInfoExport(wakuangWallet,response);
    }


    @PostMapping("/wallet/pageInfoGroupByUser")
    public DataResult pageInfoGroupByUser(@RequestBody WaKuangWallet wakuangWallet) {
        return DataResult.success(waKuangWalletService.pageInfoGroupByUser(wakuangWallet)
        );
    }

    @PostMapping("/wallet/findUserAll")
    public DataResult findUserAll(@RequestBody WaKuangWallet wakuangWallet) {
        return DataResult.success(waKuangWalletService.ListByUser(wakuangWallet)
        );
    }

    @PostMapping("/wallet/recharge/pageInfo")
    public DataResult rechargePageInfo(@RequestBody WaKuangWalletRecharge vo) {
        return DataResult.success(waKuangWalletRechareService.pageInfo(vo)
        );
    }

    @ApiOperation("充值记录导出")
    @PostMapping("/wallet/recharge/pageInfoExport")
    public void rechargePageInfoExport(@RequestBody WaKuangWalletRecharge vo, HttpServletResponse response) throws IOException {
        waKuangWalletRechareService.pageInfoExport(vo,response);
    }


    @PostMapping("/wallet/recharge/add")
    public DataResult rechargeAdd(@RequestBody WaKuangWalletRecharge vo) {
        return DataResult.success(waKuangWalletRechareService.save(vo)
        );
    }

    @PostMapping("/wallet/setPwd")
    public DataResult update(@RequestBody WaKuangWallet dto) {
        List<WaKuangWallet> walletList = waKuangWalletService.List(dto);
        for (WaKuangWallet waKuangWallet : walletList) {
            waKuangWallet.setPwd(dto.getPwd());
        }
        return DataResult.success(waKuangWalletService.updateBatchById(walletList)
        );
    }

    @PostMapping("/wallet/syncUsers")
    public DataResult syncUsers(@RequestBody WaKuangJl vo) {
        List<WaKuangJl> userIds = waKuangJlMapper.syncUsers(vo.getSourceType());
        for (WaKuangJl item : userIds) {
            WaKuangJl waKuangJl = waKuangJlMapper.syncUsersJl(item.getUserId(),vo.getSourceType());
            waKuangJl.setCreateTime(new Date("2023/12/06 00:00:00"));
            waKuangJl.setUpdateTime(new Date("2023/12/06 00:00:00"));
            waKuangJlMapper.updateById(waKuangJl);
        }
        return DataResult.success();
    }


    @PostMapping("/wallet/setysBalance")
    public DataResult setysBalance(@RequestBody WaKuangWallet dto) {
        WaKuangWallet wallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, dto.getUserName()).eq(WaKuangWallet::getCoin, dto.getCoin()).last("limit 1"));
        if (wallet == null) {
            return DataResult.fail("用户不存在");
        }
//        if(wallet.getYsBalance().compareTo(BigDecimal.ZERO)>0){
//            return DataResult.fail("用户已映射过");
//        }
        wallet.setYsedBalance(dto.getYsBalance());
        wallet.setYsBalance(dto.getYsBalance());
        return DataResult.success(waKuangWalletService.updateById(wallet)
        );
    }

    @PostMapping("/wallet/setjwBalance")
    public DataResult setjwBalance(@RequestBody WaKuangWallet dto) {
        WaKuangWallet wallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, dto.getUserName()).eq(WaKuangWallet::getCoin, dto.getCoin()).last("limit 1"));
        if (wallet == null) {
            return DataResult.fail("用户不存在");
        }
//        if(wallet.getYsBalance().compareTo(BigDecimal.ZERO)>0){
//            return DataResult.fail("用户已映射过");
//        }
        wallet.setJwBalance(dto.getJwBalance());
        wallet.setJwedBalance(dto.getJwBalance());
        return DataResult.success(waKuangWalletService.updateById(wallet)
        );
    }

    /**
     * 修改奖励等级（-1-7，-1不参与团队奖励 0：没有设置等级，1表示D1等级。。。）
     */
    @PostMapping("/wallet/setType")
    public DataResult setType(@RequestBody WaKuangWallet dto) {
        if (Objects.isNull(dto.getType())) {
            return DataResult.fail("等级不能为空");
        }
        if (dto.getType() < -1 || dto.getType() > 7) {
            return DataResult.fail("等级必须为 -1 - 7");
        }
        return DataResult.success(waKuangWalletService.setType(dto.getUserName(), dto.getType()));
    }

    /**
     * 质押地址
     * @param dto
     * @return
     */
    @PostMapping("/wallet/resultZyAddress")
    public DataResult resultZyAddress(@RequestBody WaKuangWallet dto) throws MnemonicException.MnemonicLengthException, ExecutionException, InterruptedException {
//        return DataResult.success("0xccd9fE91391fB1DF2a75839684a551a20F9607c6");
        return DataResult.success("0x7223628637dBf76461A82B4B0CD1C840AF3E5D53");
    }

    /**
     * 质押地址
     * @param dto
     * @return
     */
    @PostMapping("/wallet/resultZyAddressRengou")
    public DataResult resultZyAddressRengou(@RequestBody WaKuangWallet dto) throws MnemonicException.MnemonicLengthException, ExecutionException, InterruptedException {
//        return DataResult.success("0xccd9fE91391fB1DF2a75839684a551a20F9607c6");
        return DataResult.success("0xE419CD38889Df077001FafeC96D916d605A6fa6c");
    }


    /**
     * 转账记录
     * @param vo
     * @return
     */
    @PostMapping("/wallet/zhuanzhang/pageInfo")
    public DataResult zhuanzhangPageInfo(@RequestBody WaKuangZhuanzhang vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangZhuanzhang> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangZhuanzhang::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getFromUserId())) {
            queryWrapper.eq(WaKuangZhuanzhang::getFromUserId, vo.getFromUserId());
        }
        if (!StringUtils.isEmpty(vo.getToUserId())) {
            queryWrapper.eq(WaKuangZhuanzhang::getToUserId, vo.getToUserId());
        }
        IPage<WaKuangZhuanzhang> iPage = waKuangZhuanZhangMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

    /**
     * 转账记录
     *
     * @param vo
     * @return
     */
    @PostMapping("/wallet/jlZhuanzhang/pageInfo")
    public DataResult jlZhuanzhangPageInfo(@RequestBody WaKuangJlZhuanzhang vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangJlZhuanzhang> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJlZhuanzhang::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getToUserId())) {
            queryWrapper.eq(WaKuangJlZhuanzhang::getToUserId, vo.getToUserId());
        }
        IPage<WaKuangJlZhuanzhang> iPage = waKuangJlZhuanZhangMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

    /**
     * 闪兑记录
     * @param vo
     * @return
     */
    @PostMapping("/wallet/shandui/pageInfo")
    public DataResult shanduiPageInfo(@RequestBody WaKuangShandui vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangShandui> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangShandui::getUserId, vo.getUserId());
        }
        IPage<WaKuangShandui> iPage = waKuangShanduiMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @PostMapping("/wallet/shandui/pageInfoExport")
    public void shanduiPageInfoExport(@RequestBody WaKuangShandui vo,HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangShandui> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangShandui::getUserId, vo.getUserId());
        }
        List<WaKuangShandui> waKuangShanduis = waKuangShanduiMapper.selectList(queryWrapper);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("闪兑记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangShandui.class)
                .sheet("闪兑记录")
                .doWrite(waKuangShanduis);
    }

    /**
     * 同步新币种
     * @param dto
     * @return
     */
    @PostMapping("/wallet/syncByCoin")
    public DataResult syncByCoin(@RequestBody WaKuangWallet dto) throws MnemonicException.MnemonicLengthException, ExecutionException, InterruptedException {

        List<WaKuangWallet> alllist = waKuangWalletService.list(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "Domi")
        );
        for (WaKuangWallet data:alllist
             ) {
            List<WaKuangWallet> wakuangWalletList = waKuangWalletService.list(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, data.getUserName()));
            if (wakuangWalletList.size() > 0) {
                // 判断有没有币种，没有的话添加
                if(wakuangWalletList.size()<7){
                    WaKuangWallet waKuangWallet = wakuangWalletList.get(0);

                    WaKuangWallet nftDto = new WaKuangWallet();
                    nftDto.setCode(waKuangWallet.getCode());
                    nftDto.setInvitationCode(waKuangWallet.getInvitationCode());
                    nftDto.setUserName(waKuangWallet.getUserName());
                    nftDto.setUserPwd(waKuangWallet.getUserPwd());
                    nftDto.setUserPwd("123456");
                    nftDto.setPwd("123456");
                    nftDto.setCoin("NFT-D");
                    nftDto.setAddress(waKuangWallet.getAddress());
                    nftDto.setPrivateKey(waKuangWallet.getPrivateKey());
                    nftDto.setBalance(new BigDecimal(0.00));
                    nftDto.setKzyBalance(new BigDecimal(0.00));
                    nftDto.setJldhBalance(new BigDecimal(0.00));
                    nftDto.setJltxBalance(new BigDecimal(0.00));
                    nftDto.setDjBalance(new BigDecimal(0.00));
                    nftDto.setZyBalance(new BigDecimal(0.00));

                    WaKuangWallet lpDto = new WaKuangWallet();
                    lpDto.setCode(waKuangWallet.getCode());
                    lpDto.setInvitationCode(waKuangWallet.getInvitationCode());
                    lpDto.setUserName(waKuangWallet.getUserName());
                    lpDto.setUserPwd(waKuangWallet.getUserPwd());
                    lpDto.setUserPwd("123456");
                    lpDto.setPwd("123456");
                    lpDto.setCoin("LP");
                    lpDto.setAddress(waKuangWallet.getAddress());
                    lpDto.setPrivateKey(waKuangWallet.getPrivateKey());
                    lpDto.setBalance(new BigDecimal(0.00));
                    lpDto.setKzyBalance(new BigDecimal(0.00));
                    lpDto.setJldhBalance(new BigDecimal(0.00));
                    lpDto.setJltxBalance(new BigDecimal(0.00));
                    lpDto.setDjBalance(new BigDecimal(0.00));
                    lpDto.setZyBalance(new BigDecimal(0.00));

                    List<WaKuangWallet> list = new ArrayList<>();
                    list.add(nftDto);
                    list.add(lpDto);
                    waKuangWalletService.saveBatch(list);
                }
            }
        }
        return DataResult.success();
    }

    /**
     * 资产批量转移
     * @param dto
     * @return
     */
    @PostMapping("/wallet/syncByBalance")
    public DataResult syncByBalance(@RequestBody WaKuangWallet dto) throws MnemonicException.MnemonicLengthException, ExecutionException, InterruptedException {

        List<WaKuangWallet> alllist = waKuangWalletService.list(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "Domi")
        );
        for (WaKuangWallet data:alllist
        ) {
            if(data.getJltxBalance().compareTo(BigDecimal.ZERO)>0){
                BigDecimal jl = data.getJltxBalance();
                data.setKzyBalance(data.getKzyBalance().add(jl));
                data.setJltxBalance(new BigDecimal(0));
                waKuangWalletService.updateWallet(data);
            }
        }
        return DataResult.success();
    }


    /**
     * 登陆
     *
     * @param dto
     * @return
     */
    @PostMapping("/wallet/login")
    public DataResult login(@RequestBody WaKuangWallet dto) throws MnemonicException.MnemonicLengthException, ExecutionException, InterruptedException {


        // 存在直接返回，即登陆
        List<WaKuangWallet> wakuangWalletList = waKuangWalletService.list(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, dto.getUserName()));
        if (wakuangWalletList.size() > 0) {
            return DataResult.success(wakuangWalletList);
        }
        return DataResult.fail("未注册");
    }


    /**
     * 三星节点列表
     *
     * @return
     */
    @PostMapping("/wallet/startAddressList")
    public DataResult startAddressList(@RequestBody StartAddress vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<StartAddress> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(StartAddress::getAddress, vo.getAddress());
        }
        IPage<StartAddress> iPage = startAddressMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }


    /**
     * 三星节点列表
     *
     * @return
     */
    @PostMapping("/wallet/startAddressAdd")
    public DataResult startAddressAdd(@RequestBody StartAddress vo) {
        return DataResult.success(startAddressMapper.insert(vo));
    }

    /**
     * 三星节点列表
     *
     * @return
     */
    @PostMapping("/wallet/startAddressDelete")
    public DataResult startAddressDelete(@RequestBody StartAddress vo) {
        return DataResult.success(startAddressMapper.deleteById(vo));
    }

    /**
     * 闪兑
     *
     * @return
     */
    @PostMapping("/wallet/shandui")
    public DataResult shandui(@RequestBody WaKuangShandui vo) {
//        if(vo.getUserId().equals("魁哥500")||vo.getUserId().equals("吉丽姐500")||vo.getUserId().equals("曾杨500")||vo.getUserId().equals("米多多500")||vo.getUserId().equals(" 众赢江西500")||vo.getUserId().equals("众赢湖北500")||vo.getUserId().equals("快乐人生500")||vo.getUserId().equals("冯屹星宇500")||vo.getUserId().equals("宝宝霜500")){
//            return DataResult.fail("该账号暂未开放兑换");
//        }
        if(vo.getUserId().equals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")||
                vo.getUserId().equals("0xEb12aC080A0B50fC96c6956eAEC623113af59AcD")||
                vo.getUserId().equals("0xeE37E74457d7aC44d70F4a28e742Ccf6a1E9ccE0")||
                vo.getUserId().equals("0x0c84Ac6AF681278f00A1101BDf10a838391AE55D")){
            return DataResult.fail("非法兑换");
        }
        if(vo.getToCoin().equals("DMD")){
            return DataResult.fail("暂未开放");
        }
        WaKuangWallet fromWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getFromCoin()).eq(WaKuangWallet::getUserName, vo.getUserId()));
        if (fromWallet.getBalance().compareTo(vo.getFromBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        WaKuangWallet toWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getToCoin()).eq(WaKuangWallet::getUserName, vo.getUserId()));
        waKuangWalletService.shandui(fromWallet, toWallet, vo);
        return DataResult.success();
    }

    /**
     * 闪兑2
     * @param vo
     * @return
     */
    @PostMapping("/wallet/shanduiJlDh")
    public DataResult shanduiJlDh(@RequestBody WaKuangShandui vo) {
        if(vo.getToCoin().equals("DMD")){
            return DataResult.fail("暂未开放");
        }
        if(vo.getUserId().equals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")||
                vo.getUserId().equals("0xEb12aC080A0B50fC96c6956eAEC623113af59AcD")||
                vo.getUserId().equals("0xeE37E74457d7aC44d70F4a28e742Ccf6a1E9ccE0")||
                vo.getUserId().equals("0x0c84Ac6AF681278f00A1101BDf10a838391AE55D")){
            return DataResult.fail("非法兑换");
        }
        WaKuangWallet fromWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getFromCoin()).eq(WaKuangWallet::getUserName, vo.getUserId()));
        if (fromWallet.getJldhBalance().compareTo(vo.getFromBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        WaKuangWallet toWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getToCoin()).eq(WaKuangWallet::getUserName, vo.getUserId()));
        waKuangWalletService.shanduiJlDh(fromWallet, toWallet, vo);
        return DataResult.success();
    }


    /**
     * 转账
     *
     * @param vo
     * @return
     */
    @PostMapping("/wallet/zhuanzhang")
    public DataResult zhuanzhang(@RequestBody WaKuangZhuanzhang vo) {
        vo.setFromUserId("master");
        WaKuangWallet fromWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getFromUserId()));
        if (fromWallet.getBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        WaKuangWallet toWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getToUserId()));
        waKuangWalletService.zhuanzhang(fromWallet, toWallet, vo);
        return DataResult.success();
    }

    @PostMapping("/wallet/zhuanzhangKzy")
    public DataResult zhuanzhangKzy(@RequestBody WaKuangZhuanzhang vo) {
        if(vo.getBalance().compareTo(BigDecimal.ZERO)<=0){
            return DataResult.fail("金额不能为负数");
        }
        WaKuangWallet fromWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getFromUserId()));
        if (fromWallet.getKzyBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        WaKuangWallet toWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getToUserId()));
        waKuangWalletService.zhuanzhangKzy(fromWallet, toWallet, vo);
        return DataResult.success();
    }

    /**
     * 转账
     *
     * @param vo
     * @return
     */
    @PostMapping("/wallet/zhuanzhangNeibu")
    public DataResult zhuanzhangNeibu(@RequestBody WaKuangZhuanzhang vo) {
        if(vo.getFromUserId().equals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")||
                vo.getFromUserId().equals("0xEb12aC080A0B50fC96c6956eAEC623113af59AcD")||
                vo.getFromUserId().equals("0xeE37E74457d7aC44d70F4a28e742Ccf6a1E9ccE0")||
                vo.getFromUserId().equals("0x0c84Ac6AF681278f00A1101BDf10a838391AE55D")){
            return DataResult.fail("非法转账");
        }
        if(vo.getToUserId().equals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")){
            return DataResult.fail("非法转账");
        }
        if(vo.getBalance().compareTo(BigDecimal.ZERO)<=0){
            return DataResult.fail("金额不能为负数");
        }
        WaKuangWallet fromWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getFromUserId()));
        if (fromWallet.getBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        WaKuangWallet toWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getToUserId()));
        waKuangWalletService.zhuanzhang(fromWallet, toWallet, vo);
        return DataResult.success();
    }

    /**
     * 划转
     * @return
     */
    @PostMapping("/wallet/huazhuanBalance")
    public DataResult huazhuanBalance(@RequestBody WaKuangWallet vo) {
        WaKuangWallet waKuangWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getUserName()));
        if (waKuangWallet.getBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        if(vo.getBalance().compareTo(BigDecimal.ZERO)<=0){
            return DataResult.fail("金额不能为负数");
        }
        waKuangWallet.setBalance(waKuangWallet.getBalance().subtract(vo.getBalance()));
        waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().add(vo.getBalance()));
        waKuangWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(waKuangWallet);
        return DataResult.success();
    }

    /**
     * 可兑换账户划转到可质押账户
     * @return
     */
    @PostMapping("/wallet/huazhuanBalance2")
    public DataResult huazhuanBalance2(@RequestBody WaKuangWallet vo) {
        WaKuangWallet waKuangWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getUserName()));
        if (waKuangWallet.getJldhBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        if(vo.getBalance().compareTo(BigDecimal.ZERO)<=0){
            return DataResult.fail("金额不能为负数");
        }
        waKuangWallet.setJldhBalance(waKuangWallet.getJldhBalance().subtract(vo.getBalance()));
        waKuangWallet.setKzyBalance(waKuangWallet.getKzyBalance().add(vo.getBalance()));
        waKuangWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(waKuangWallet);
        return DataResult.success();
    }


    /**
     * 划转
     * @return
     */
    @PostMapping("/wallet/huazhuanJldh")
    public DataResult huazhuanJldh(@RequestBody WaKuangWallet vo) {
        WaKuangWallet waKuangWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getUserName()));
        if (waKuangWallet.getJldhBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        if(vo.getBalance().compareTo(BigDecimal.ZERO)<=0){
            return DataResult.fail("金额不能为负数");
        }
        waKuangWallet.setJldhBalance(waKuangWallet.getJldhBalance().subtract(vo.getBalance()));
        waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().add(vo.getBalance()));
        waKuangWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(waKuangWallet);
        return DataResult.success();
    }

    @PostMapping("/wallet/zhuanzhangNeibuByDappJltx")
    public DataResult zhuanzhangNeibuByDappJltx(@RequestBody WaKuangZhuanzhang vo) {
        if(vo.getFromUserId().equals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")||
                vo.getFromUserId().equals("0xEb12aC080A0B50fC96c6956eAEC623113af59AcD")||
                vo.getFromUserId().equals("0xeE37E74457d7aC44d70F4a28e742Ccf6a1E9ccE0")||
                vo.getFromUserId().equals("0x0c84Ac6AF681278f00A1101BDf10a838391AE55D")){
            return DataResult.fail("非法转账");
        }
        if(vo.getToUserId().equals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")){
            return DataResult.fail("非法转账");
        }
        WaKuangWallet fromWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getFromUserId()));
        if (fromWallet.getJltxBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("余额不足");
        }
        if(vo.getBalance().compareTo(BigDecimal.ZERO)<=0){
            return DataResult.fail("金额不能为负数");
        }
        WaKuangWallet toWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, vo.getCoin()).eq(WaKuangWallet::getUserName, vo.getToUserId()));
        waKuangWalletService.zhuanzhangByDappJltx(fromWallet, toWallet, vo);
        return DataResult.success();
    }

    /**
     * Domi+IDM和Domi+BNB历史总质押
     * @param vo
     * @return
     */
    @PostMapping("/wallet/allZy")
    public DataResult allZy(@RequestBody WaKuangZhuanzhang vo) {

        int bnbSumZyBalance = waKuangZyMapper.SumZyBalance("DOMI+BNB");
        int idmSumZyBalance = waKuangZyMapper.SumZyBalance("DOMI+IDM");
        // 全网总IDM
        int idmSumZyBalance1 = waKuangZyMapper.SumZyBalance1("DOMI+IDM");
        int bnbSumZyBalance1 = waKuangZyMapper.SumZyBalance1("DOMI+BNB");
        int yesterdaySumZyBalance = bnbSumZyBalance+idmSumZyBalance;

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterdayDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayDateString = dateFormat.format(yesterdayDate);

        // 获取当前日期
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.add(Calendar.DATE, 0);
        Date todayDate = calendar.getTime();
        String todayDateString = dateFormat.format(todayDate);



        int allSumBnbBalance = waKuangJlMapper.allSumBnbBalance();
        int yesterdaySumBnbBalance = waKuangJlMapper.yesterdaySumBnbBalance(yesterdayDateString+" 00:00:00",yesterdayDateString+" 23:59:59");

        int lpSumZyBalance = waKuangZyMapper.SumZyBalance("LP");
        int allSumLpBalance = waKuangJlMapper.allSumLpBalance();
        int todaySumLpBalance = waKuangJlMapper.yesterdaySumLpBalance(todayDateString+" 00:00:00",todayDateString+" 23:59:59");

        int allSumTjLpBalance = waKuangJlMapper.allSumTjLpBalance();
        int todaySumTjLpBalance = waKuangJlMapper.yesterdaySumTjLpBalance(todayDateString+" 00:00:00",todayDateString+" 23:59:59");

        Map map = new HashMap();
        map.put("allSumBnbBalance",allSumBnbBalance); //总释放bnb
        map.put("yesterdaySumBnbBalance",yesterdaySumBnbBalance); //昨天释放bnb
        map.put("yesterdaySumZyBalance",yesterdaySumZyBalance);//昨天质押
        map.put("idmSumZyBalance1",idmSumZyBalance1+500); //全网总IDM质押
        map.put("bnbSumZyBalance1",bnbSumZyBalance1); //全网总BNB质押

        map.put("allSumLpBalance",allSumLpBalance*1.42); //总释放lp
        map.put("todaySumLpBalance",todaySumLpBalance*1.42); //今天总释放lp
        map.put("allSumTjLpBalance",allSumTjLpBalance); //总释放lp推荐奖励
        map.put("todaySumTjLpBalance",todaySumTjLpBalance); //今天总释放lp推荐奖励
        map.put("lpSumZyBalance",lpSumZyBalance); //全网总质押lp
        return DataResult.success(map);
    }

    /**
     * 禁用
     */
    @PostMapping("/wallet/deleteUser")
    public DataResult deleteUser(@RequestBody WaKuangWallet vo) {

        if(vo.getUserName()==null||vo.getUserName().equals("")){
            return DataResult.fail("用户名不能为空");
        }

        Map map = new HashMap();
        map.put("user_name",vo.getUserName());
        return DataResult.success(waKuangWalletService.removeByMap(map));
    }

    /**
     * Domi+IDM和Domi+BNB历史总质押
     * @param vo
     * @return
     */
        @PostMapping("/wallet/sumByUser")
    public DataResult sumByUser(@RequestBody WaKuangJl vo) {

        BigDecimal  JlBalance = waKuangJlMapper.selectJlSumByUser(vo.getCoin(),vo.getUserId());
        BigDecimal shanduiFromBalance = waKuangJlMapper.shanduiFromBalance(vo.getCoin(),vo.getUserId());
        BigDecimal shanduiToBalance = waKuangJlMapper.shanduiToBalance(vo.getCoin(),vo.getUserId());
        BigDecimal zhuanzhangFromBalance = waKuangJlMapper.zhuanzhangFromBalance(vo.getCoin(),vo.getUserId());
        BigDecimal zhuanzhangToBalance = waKuangJlMapper.zhuanzhangToBalance(vo.getCoin(),vo.getUserId());
        BigDecimal withdrawBalance = waKuangJlMapper.withdrawBalance(vo.getCoin(),vo.getUserId());

        Map map = new HashMap();
        map.put("JlBalance",JlBalance);//奖励总额
        map.put("shanduiFromBalance",shanduiToBalance); //闪兑进来的
        map.put("shanduiToBalance",shanduiFromBalance); //闪兑出去的
        map.put("zhuanzhangFromBalance",zhuanzhangToBalance); //转账进来的
        map.put("zhuanzhangToBalance",zhuanzhangFromBalance); //转账出去的
        map.put("withdrawBalance",withdrawBalance.divide(new BigDecimal(0.8),7, BigDecimal.ROUND_DOWN)); //提现成功的总金额
        return DataResult.success(map);
    }



    /**
     * 加资产
     * @param dto
     * @return
     */
    @PostMapping("/wallet/addbalanceByCoin-a")
    public DataResult addbalanceByCoin(@RequestBody WaKuangWallet dto) {
        WaKuangWallet waKuangWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, dto.getCoin()).eq(WaKuangWallet::getUserName, dto.getUserName()));
        waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().add(dto.getJltxBalance()));
        waKuangWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(waKuangWallet);

        WaKuangJl jl = new WaKuangJl();
        jl.setUserId(dto.getUserName());
        jl.setFromUserId("-1");
        jl.setBalance(dto.getJltxBalance());
        jl.setCoin(dto.getCoin());
        jl.setSourceType(11);
        jl.setStatus(-1);
        jl.setCreateTime(new Date());
        jl.setUpdateTime(new Date());
        waKuangJlService.save(jl);

        return DataResult.success();
    }


    @PostMapping("/wallet/detail")
    public DataResult detail(@RequestBody WaKuangWallet wakuangWallet) {
        return DataResult.success(waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, "USDT-BEP20").eq(WaKuangWallet::getUserName, wakuangWallet.getUserName()).last("limit 1"))
        );
    }

    @PostMapping("/version/list")
    public DataResult Version() {
        return DataResult.success(waKuangVersionMapper.selectList(Wrappers.<WaKuangVersion>lambdaQuery().orderByDesc(WaKuangVersion::getVersion)).get(0));
    }

    @PostMapping("/version/add")
    public DataResult VersionAdd(@RequestBody WaKuangVersion waKuangVersion) {
        waKuangVersionMapper.insert(waKuangVersion);
        return DataResult.success(waKuangVersionMapper.selectList(null));
    }

    @PostMapping("/lunbotu/list")
    public DataResult lunbotuList() {
        return DataResult.success(lunbotuMapper.selectList(null));
    }

    @PostMapping("/lunbotu/add")
    public DataResult lunbotuAdd(@RequestBody Lunbotu lunbotu) {
        lunbotuMapper.insert(lunbotu);
        return DataResult.success(lunbotuMapper.selectList(null));
    }

    @PostMapping("/lunbotu/delete")
    public DataResult lunbotuDelete(@RequestBody Lunbotu lunbotu) {
        lunbotuMapper.deleteById(lunbotu);
        return DataResult.success(lunbotuMapper.selectList(null));
    }

    @PostMapping("/token/list")
    public DataResult Token() {
        Token token = tokenMapper.selectOne(Wrappers.<Token>lambdaQuery().eq(Token::getId, "1"));
        return DataResult.success(token);
    }

    @PostMapping("/token/add")
    public DataResult TokenAdd(@RequestBody Token dto) {
        Token token = tokenMapper.selectOne(Wrappers.<Token>lambdaQuery().eq(Token::getId, "1"));
        token.setToken(dto.getToken());
        tokenMapper.updateById(token);
        return DataResult.success();
    }

    @ApiOperation("奖励提现")
    @PostMapping("/wallet/withdraw")
    public DataResult withdraw(
            @RequestBody WaKuangJlWithdraw dto
    ) {
        return DataResult.success(waKuangWalletService.withdraw(dto));
    }

    @ApiOperation("奖励提现2")
    @PostMapping("/wallet/withdraw2")
    public DataResult withdraw(
            @RequestBody WaKuangJlWithdrawVO dto
    ) {
        return DataResult.success(waKuangWalletService.withdraw2(dto));
    }

    @ApiOperation("奖励闪兑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", dataType = "WaKuangShanduiVO", paramType = "body", value = "表单", required = true),
    })
    @PostMapping("/wallet/jlShandui")
    public DataResult jlShandui(
            @RequestBody WaKuangShanduiVO param
    ) {
        return DataResult.success(waKuangWalletService.jlShandui(param.getUserId(), param.getBalance()));
    }

    @ApiOperation("奖励闪兑2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", dataType = "WaKuangShanduiVO", paramType = "body", value = "表单", required = true),
    })
    @PostMapping("/wallet/jlShandui2")
    public DataResult jlShandui2(
            @RequestBody WaKuangShanduiVO param
    ) {
        return DataResult.success(waKuangWalletService.jlShandui(param));
    }

}
