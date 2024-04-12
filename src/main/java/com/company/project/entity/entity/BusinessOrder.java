package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("business_order")
public class BusinessOrder extends PageReqVO implements Serializable {

  @TableId
  private String id;
  
  private String userId;
  
  private String coin;
  
  private String type;
  
  private String address;
  
  private BigDecimal balance;
  
  private BigDecimal surplusBalance;
  
  private String phone;
  
  private BigDecimal unitPrice;
  
  private Integer status;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

}