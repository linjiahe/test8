package com.company.project.entity.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.common.converter.DateConverter;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 矿机
 * @Author  yzz
 * @Date 2023-02-23
 */
@Data
public class LiveClusterStats implements Serializable {

	/**
	 * id
	 */
	@ExcelIgnore
	@TableId(type = IdType.AUTO)
	private Long id;

	private String slot;

	private String blockHeight;

	private Date clusterTime;

	private int slottimeMin;

	private int slottimeHr;

	private BigDecimal epoch;

	private String epochProgress;

	private String epochTimeRemaining;

	/**
	 * 创建时间
	 */
//    @TableField(fill = FieldFill.INSERT)
	@ExcelProperty(value = "创建时间",index = 4,converter = DateConverter.class)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@ExcelIgnore
//    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
