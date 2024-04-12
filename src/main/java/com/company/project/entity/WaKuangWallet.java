package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("wa_kuang_wallet")
public class WaKuangWallet extends PageReqVO implements Serializable {

    @TableId
    @ExcelIgnore
    private String id;
    // 推荐人的邀请码
    @ExcelIgnore
    private String invitationCode;
    // 自己的邀请码
    @ExcelIgnore
    private String code;
    @ExcelProperty(value = "账号",index = 0)
    private String userName;
    @ExcelIgnore
    @ExcelProperty(value = "币种",index = 1)
    private String coin;
    @ExcelIgnore
    private String pwd;
    @ExcelIgnore
    private BigDecimal balance;
    @ExcelIgnore
    private BigDecimal djBalance;
    @ExcelIgnore
    private String address;
    @ExcelIgnore
    private String privateKey;
    @ExcelProperty(value = "创建时间",index = 3,converter = DateConverter.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Integer actualGrade;
    private Integer directPushGrade;
    private Integer teamGrade;
    private Integer globalGrade;
    /**
     * 团队人数
     */
    private Integer teamCount;
    /**
     * 团队业绩
     */
    private BigDecimal teamYeji;
    /**
     * 个人业绩
     */
    private BigDecimal userYeji;
}