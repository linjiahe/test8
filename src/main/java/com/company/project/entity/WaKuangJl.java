package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("wa_kuang_jl")
public class WaKuangJl extends PageReqVO implements Serializable {

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
	private String userId;

	/**
	 * 充值用户
	 */
	@ExcelIgnore
	private String fromUserId;

	/**
	 * 币种
	 */
	@ExcelProperty(value = "币种",index = 1)
	private String coin;

	/**
	 * 金额
	 */
	@ExcelProperty(value = "金额",index = 2)
	private BigDecimal balance;
	@ApiModelProperty("来源类型（1：分享收益 2：社区收益 3:全球分红")
	private Integer sourceType;

	/**
	 * 状态（0：未发放，1：已发放）
	 */
	@ExcelIgnore
	@ApiModelProperty("状态（0：未发放，1：已发放）")
	private Integer status;

	/**
	 * 创建时间
	 */
//    @TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@ExcelIgnore
//    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;




}
