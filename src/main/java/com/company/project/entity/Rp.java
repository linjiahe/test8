package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class Rp extends PageReqVO implements Serializable {

    @TableId
    private String id;

    @NotNull(message = "币种不能为空")
    private String coin;

    @NotNull(message = "用户Id不能为空")
    private String userId;

    private int status;

    private String msg;

    private int count;

    @NotNull(message = "金额不能为空")
    private double balance;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
