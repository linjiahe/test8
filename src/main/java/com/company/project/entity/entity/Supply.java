package com.company.project.entity.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 矿机
 * @Author  yzz
 * @Date 2023-02-23
 */
@Data
public class Supply implements Serializable {

	/**
	 * id
	 */
	@ExcelIgnore
	@TableId(type = IdType.AUTO)
	private Long id;

	private int totalSupply;

	private int circulatingSupply;

	private int nonCirculatingSupply;

}
