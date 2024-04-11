package com.company.project.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "今日统计Vo")
public class TodayStatisticsVo {
    @ApiModelProperty(value = "DOMI今日质押数量")
    private BigDecimal DOMITodayPledge;
    @ApiModelProperty(value = "DOMI历史质押数量")
    private BigDecimal DOMIHistoryPledge;
    @ApiModelProperty(value = "DMD今日产出")
    private BigDecimal DMDTodayOutput;
    @ApiModelProperty(value = "DMD今日映射")
    private BigDecimal DMDTodayMapping;
    @ApiModelProperty(value = "DOMI今日映射")
    private BigDecimal DOMITodayMapping;
}
