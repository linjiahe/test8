package com.company.project.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaKuangWalletZyVO {

    @ApiModelProperty(value = "质押金额")
    private BigDecimal zyBalance;

    @ApiModelProperty(value = "静态收益")
    private BigDecimal syBalance;


    @ApiModelProperty(value = "下级人数")
    private Long subNum;

    @ApiModelProperty(value = "下级质押金额")
    private BigDecimal subPledge;


}
