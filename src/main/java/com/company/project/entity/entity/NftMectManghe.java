package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
public class NftMectManghe extends PageReqVO implements Serializable {

    @TableId
    private String id;

    private String name;

    private int price;

    private int count;

    private int syCount;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}