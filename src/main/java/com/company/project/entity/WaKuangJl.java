package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.company.project.common.converter.DateConverter;
import com.company.project.common.converter.WaKuangJlType;
import com.company.project.vo.req.PageReqVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
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
	@ExcelProperty(value = "类型",index = 3,converter = WaKuangJlType.class)
	@ApiModelProperty("来源类型（0：其他，1：团队奖励，2：购买DOMI<直推奖励>，3：质押奖励 4:游戏加减资产" +
			" 5:团队质押奖励 6.动态奖励 7.三星节点奖励 8.映射奖励 9.特殊奖励 10.DOMI+IDM质押奖励 11.后台加资产 12.DOMI+BNB质押奖励 13.lp质押奖励)")
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

	private int version;

}
