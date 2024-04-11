package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.BusinessOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface BusinessOrderMapper extends BaseMapper<BusinessOrder> {

    @Update("update business_order set surplus_balance=surplus_balance - #{balance} where id=#{id} and surplus_balance >= #{balance}")
    int subSurplusBalance(@Param("id") String id, @Param("balance") BigDecimal balance);

}
