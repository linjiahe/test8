package com.company.project.controller;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelIgnoreUnannotated
public class UsdtBscImport implements Serializable {

    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("私钥")
    private String privateKey;
}