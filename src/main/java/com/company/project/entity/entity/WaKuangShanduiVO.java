package com.company.project.entity.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class WaKuangShanduiVO implements Serializable {

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("金额（可兑换成DomiDao）")
    private BigDecimal balance;

    @ApiModelProperty("团队奖励（自定义兑换金额）")
    private BigDecimal jlBalance;

    @ApiModelProperty("直推奖励（自定义兑换金额）")
    private BigDecimal ztBalance;

    @ApiModelProperty("质押奖励（自定义兑换金额）")
    private BigDecimal zyjlBalance;

}