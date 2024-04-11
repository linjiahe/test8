package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.common.converter.DateConverter;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangWallet extends PageReqVO implements Serializable {

    @TableId
    @ExcelIgnore
    private String id;
    @ExcelProperty(value = "账号",index = 0)
    private String userName;
    @ExcelIgnore
    private String userPwd;
    @ExcelProperty(value = "币种",index = 1)
    private String coin;

    // 推荐人的邀请码
    @ExcelIgnore
    private String invitationCode;

    private String jiedianCode;


    // 自己的邀请码
    @ExcelIgnore
    private String code;
    @ExcelIgnore
    private String pwd;
    @ExcelIgnore
    private BigDecimal balance;
    @ExcelIgnore
    private BigDecimal kzyBalance;

    /**
     * 计算团队奖励的金额
     */
    @ExcelIgnore
    private BigDecimal tdjlBalance;
    @ExcelIgnore
    private BigDecimal djBalance;
    @ExcelIgnore
    private BigDecimal zyBalance;

    /**
     * 团队奖励
     */
    @ExcelIgnore
    private BigDecimal jlBalance;

    /**
     * 可提现账户
     */

    @ExcelProperty(value = "余额",index = 2)
    private BigDecimal jltxBalance;


    /**
     * 矿机账户
     */
    @ExcelProperty(value = "矿机账户金额",index = 3)
    private BigDecimal kjBalance;

    /**
     * 节点（1，大 2，中）
     */
    @ExcelIgnore
    private int jiedianLevel;

    /**
     * 可兑换账户
     */
    @ExcelIgnore
    private BigDecimal jldhBalance;

    /**
     * 直推奖励
     */
    @ExcelIgnore
    private BigDecimal ztBalance;

    /**
     * 映射设置的金额
     */
    @ExcelIgnore
    private BigDecimal ysBalance;

    /**
     * 映射额度
     */
    @ExcelIgnore
    private BigDecimal ysedBalance;

    /**
     * 映射账户余额
     */
    @ExcelIgnore
    private BigDecimal ysinBalance;
    @ExcelIgnore
    private BigDecimal jwinBalance;
    @ExcelIgnore
    private BigDecimal jwBalance;
    @ExcelIgnore
    private BigDecimal jwedBalance;

    /**
     * 质押奖励
     */
    @ExcelIgnore
    private BigDecimal zyjlBalance;
    @ExcelIgnore
    private String address;
    @ExcelIgnore
    private String privateKey;
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 设置等级
     */
    @ExcelIgnore
    private Integer type;

    /**
     * 质押等级
     */    @ExcelIgnore
    private Integer level;
    @ExcelIgnore
    private BigDecimal usdtZyBalance;
    @ExcelIgnore
    private BigDecimal jtsyBalance;
    @ExcelIgnore
    private BigDecimal xjzyBalance;
    @ExcelIgnore
    private BigDecimal xjsyBalance;
    /**
     * 下级人数
     */    @ExcelIgnore
    private Long subNum;
    /**
     * 下级质押
     */    @ExcelIgnore
    private BigDecimal subPledge;

    @ExcelProperty(value = "domi地址",index = 4)
    private String yingsheAddress;


}