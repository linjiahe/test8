package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaKuangZyDTO extends PageReqVO implements Serializable {

    @TableId
    private String id;

    private String userId;

    private List<String> userIds;

    private String coin;

    private BigDecimal zyBalance;

    private int zyDay;

    private int type;

    private int status;

    private int mbxhStatus;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否归还（1：是，0：否）
     */
    private int isReturn;

    /**
     * 奖励金额
     */
    private BigDecimal jlBalance;

}