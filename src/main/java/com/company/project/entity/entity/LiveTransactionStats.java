package com.company.project.entity.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LiveTransactionStats implements Serializable {

	@ExcelIgnore
	@TableId(type = IdType.AUTO)
	private Long id;

	private BigDecimal transactionCount;

	private int transactionsPerSecond;

}
