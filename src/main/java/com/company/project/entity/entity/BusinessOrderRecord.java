package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("business_order_record")
public class BusinessOrderRecord extends PageReqVO implements Serializable {
    private static final long serialVersionUID = 4719982828748544626L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderId;

    private String userId;

    private String orderUserId;

    private String type;

    private String address;

    private String coin;

    private String phone;

    private BigDecimal amount;

    private BigDecimal unitPrice;

    private Integer status;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}