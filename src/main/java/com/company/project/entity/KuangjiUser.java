package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.common.converter.DateConverter;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 矿机用户
 * @Author  yzz
 * @Date 2023-02-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KuangjiUser extends PageReqVO implements Serializable {

	/**
	 * id
	 */
	@ExcelIgnore
	@TableId(type = IdType.AUTO)
	private Long id;

	private Long kjId;

	private String userId;

	/**
	 * 矿机名称
	 */
	private String name;

	/**
	 * 矿机数量
	 */
	private int count;

	/**
	 * 矿机剩余数量
	 */
	private int syCount;

	@ExcelIgnore
	private String img;

	/**
	 * 质押金额
	 */
	@ExcelProperty(value = "币种",index = 1)
	private BigDecimal balance;

	/**
	 * 固定奖励（值：0.01=百分之1）
	 */
	private BigDecimal gdjl;

	private float status;

	/**
	 * 金额
	 */
	@ExcelProperty(value = "金额",index = 2)
	private int day;

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
