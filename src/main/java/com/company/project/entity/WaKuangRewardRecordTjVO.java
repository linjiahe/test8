package com.company.project.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description domi奖励记录 
 * @Author  yzz
 * @Date 2023-02-24 
 */
@Data
public class WaKuangRewardRecordTjVO implements Serializable {

	private static final long serialVersionUID =  1576871335140882255L;

	@ApiModelProperty("用户ID")
	private String userId;

	@ApiModelProperty("总人数")
	private Integer numberOfPeople;

	@ApiModelProperty("总绩效")
	private BigDecimal achievements;

	@ApiModelProperty("总收益")
	private BigDecimal profit;

	@ApiModelProperty("小区团队")
	private List<WaKuangRewardRecordVO> child;

}
