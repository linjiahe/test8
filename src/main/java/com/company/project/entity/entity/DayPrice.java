package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DayPrice extends PageReqVO implements Serializable {

    @TableId
    private String id;

    private BigDecimal mectBalance;

    private BigDecimal bnbBalance;

    private BigDecimal usdtbep20Balance;

    private BigDecimal usdterc20Balance;

    private BigDecimal ethBalance;

    private BigDecimal btcBalance;

    private String indeUrl;

    private String bottomUrl;

}