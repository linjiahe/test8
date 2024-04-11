package com.company.project.entity;

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
@Data
public class WaKuangRewardRecordVO implements Serializable {

	private static final long serialVersionUID =  1576871335140882255L;

	private String id;

	private String userName;

	// 推荐人的邀请码
	private String invitationCode;

	// 自己的邀请码
	private String code;

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

	private String address;

	private Date createTime;

}
