package com.company.project.entity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
public class Token implements Serializable {

    @TableId
    private int id;

    private String token;

}