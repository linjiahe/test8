package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
public class NewWalletWithdraw extends PageReqVO implements Serializable {

    @TableId
    private String id;

    @NotNull(message = "币种不能为空")
    private String coin;

    @NotNull(message = "用户Id不能为空")
    private String userId;

    private int status;

    @NotNull(message = "金额不能为空")
    private BigDecimal balance;

    @NotNull(message = "钱包地址不能为空")
    private String address;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}