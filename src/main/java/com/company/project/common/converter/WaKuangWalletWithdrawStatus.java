package com.company.project.common.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaKuangWalletWithdrawStatus implements Converter<Integer> {
    @Override
    public Class<Date> supportJavaTypeKey() {
        return Date.class;
    }


    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        Integer value = context.getValue();
        if (value==0)return new WriteCellData<>("待审核");
        if (value==1)return new WriteCellData<>("提现成功");
        if (value==2)return new WriteCellData<>("提现失败");
        return new WriteCellData<>("");
    }
}
