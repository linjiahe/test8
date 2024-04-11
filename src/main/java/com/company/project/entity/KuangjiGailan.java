package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.common.converter.DateConverter;
import com.company.project.vo.req.PageReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 奖励明细 
 * @Author  yzz
 * @Date 2023-02-23 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KuangjiGailan extends PageReqVO implements Serializable {

	/**
	 * id
	 */
	@ExcelIgnore
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户id
	 */
	@ExcelProperty(value = "账号",index = 0)
	private String hight1;

	/**
	 * 节点类型(0:大 1:中 2:小)
	 */
	@ExcelIgnore
	private String hight2;

	/**
	 * 质押金额
	 */
	@ExcelProperty(value = "币种",index = 1)
	private String hight3;

	/**
	 * 金额
	 */
	@ExcelProperty(value = "金额",index = 2)
	private String hight4;

	/**
	 * 用户id
	 */
	@ExcelProperty(value = "账号",index = 0)
	private String hight5;

	/**
	 * 节点类型(0:大 1:中 2:小)
	 */
	@ExcelIgnore
	private String hight6;

	/**
	 * 质押金额
	 */
	@ExcelProperty(value = "币种",index = 1)
	private String hight7;

	/**
	 * 金额
	 */
	@ExcelProperty(value = "金额",index = 2)
	private String hight8;

	@ExcelProperty(value = "金额",index = 2)
	private String hight9;

	@ExcelProperty(value = "金额",index = 2)
	private String hight10;

}
