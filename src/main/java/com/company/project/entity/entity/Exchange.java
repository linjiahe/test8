package com.company.project.entity.entity;

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
 * @Description 矿机
 * @Author  yzz
 * @Date 2023-02-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Exchange extends PageReqVO implements Serializable {

	/**
	 * id
	 */
	@ExcelIgnore
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 发送地址
	 */
	private String fromUserId;

	/**
	 * 接收地址
	 */
	private String toUserId;

	private BigDecimal balance;

	private BigDecimal fee;

	private String signature;

	private String recentBlockhash;

	private BigDecimal slot;

	private String result;

	private String confirmationStatus;

	private String confirmations;

	private String transactionVersion;

	/**
	 * 状态
	 */
	@ExcelProperty(value = "状态",index = 2)
	private int status;

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
