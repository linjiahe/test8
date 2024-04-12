package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangJlWithdraw extends PageReqVO implements Serializable {

    @TableId
    private String id;

    private String coin;

    private String userId;

    private Integer status;

    private BigDecimal balance;

    private BigDecimal sxfBalance;

    private String address;

    private String remark;

    private Date createTime;

    private Date updateTime;

}