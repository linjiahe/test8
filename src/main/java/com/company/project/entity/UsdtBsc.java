package com.company.project.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.company.project.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UsdtBsc extends PageReqVO implements Serializable {

    @TableId("id")
    private String id;

    @ExcelProperty("Address")
    private String address;

    @ExcelProperty("PrivateKey")
    private String privateKey;

    private int status;
}