package com.company.project.entity.entity;

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
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangWalletRecharge extends PageReqVO implements Serializable {

    @TableId
    @ExcelIgnore
    private String id;
    @ExcelProperty(value = "币种",index = 1)
    private String coin;
    @ExcelProperty(value = "账号",index = 0)
    private String userId;
    @ExcelProperty(value = "金额",index = 3)
    private BigDecimal balance;
    @ExcelProperty(value = "地址",index = 2)
    private String address;
    @ExcelProperty(value = "创建时间",index = 4,converter = DateConverter.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}