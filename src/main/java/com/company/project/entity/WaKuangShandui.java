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
public class WaKuangShandui extends PageReqVO implements Serializable {

    @TableId
    @ExcelIgnore
    private String id;

    // 用户id，传注册时候的username
    @ExcelProperty(value = "账号",index = 0)
    private String userId;

    // 要扣除的金额
    @ExcelProperty(value = "被兑换金额",index = 3)
    private BigDecimal fromBalance;

    // 要扣除的币种
    @ExcelProperty(value = "被兑换币种",index = 1)
    private String fromCoin;

    // 要增加的金额
    @ExcelProperty(value = "得到金额",index = 4)
    private BigDecimal toBalance;

    // 要增加的币种
    @ExcelProperty(value = "得到币种",index = 2)
    private String toCoin;
    @ExcelProperty(value = "创建时间",index = 5,converter = DateConverter.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}