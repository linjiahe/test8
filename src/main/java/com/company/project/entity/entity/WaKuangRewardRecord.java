package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description domi奖励记录 
 * @Author  yzz
 * @Date 2023-02-24 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangRewardRecord extends PageReqVO implements Serializable {

	private static final long serialVersionUID =  1576871335140882255L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 下级用户id
	 */
	private String subordinateUserId;

	/**
	 * 人数
	 */
	private Integer numberOfPeople;

	/**
	 * 绩效
	 */
	private BigDecimal achievements;

	/**
	 * 收益
	 */
	private BigDecimal profit;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

}
