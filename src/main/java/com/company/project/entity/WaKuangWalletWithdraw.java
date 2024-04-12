package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.common.converter.DateConverter;
import com.company.project.common.converter.WaKuangWalletWithdrawStatus;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangWalletWithdraw extends PageReqVO implements Serializable {
    @ExcelIgnore
    @TableId
    private String id;

    @ExcelProperty(value = "币种",index = 1)
    private String coin;

    @ExcelProperty(value = "账号",index = 0)
    private String userId;
    @ExcelProperty(value = "状态",index = 5,converter = WaKuangWalletWithdrawStatus.class)
    // 0.待审核 1.审核通过 2.审核未通过
    private int status;
    @ExcelIgnore
    private BigDecimal balance;
    @ExcelIgnore
    private BigDecimal dzBalance;
    @ExcelIgnore
    private BigDecimal sxfBalance;
    @ExcelProperty(value = "地址",index = 2)
    private String address;
    @ExcelIgnore
    private String remark;
    @ExcelProperty(value = "创建时间",index = 4,converter = DateConverter.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}