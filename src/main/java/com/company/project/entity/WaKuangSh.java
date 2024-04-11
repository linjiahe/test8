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
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangSh extends PageReqVO implements Serializable {

    @TableId
    private String id;

    private String userId;

    private String zyId;

    private String coin;

    private BigDecimal zyBalance;

    private BigDecimal zyBalance1;

    @ExcelProperty(value = "创建时间",index = 4,converter = DateConverter.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}