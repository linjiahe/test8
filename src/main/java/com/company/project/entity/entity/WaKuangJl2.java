package com.company.project.entity.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.common.converter.DateConverter;
import com.company.project.common.converter.WaKuangJlType;
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
public class WaKuangJl2 extends PageReqVO implements Serializable {

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
	@ExcelProperty(value = "类型",index = 3,converter = WaKuangJlType.class)
	@ApiModelProperty("来源类型（0：其他，1：lp质押上级奖励 2:抽奖手续费 3:铸造手续费 4:铸造费用 5:上架手续费 6:c2c挂单手续费 7:c2c买卖单交易 8:矿机质押奖励 9:节点奖励 10:矿机挖矿奖励)")
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
	@ExcelProperty(value = "创建时间",index = 4,converter = DateConverter.class)
	private Date createTime;

	/**
	 * 修改时间
	 */
	@ExcelIgnore
//    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;




}
