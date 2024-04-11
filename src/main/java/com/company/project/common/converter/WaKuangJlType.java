package com.company.project.common.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.util.Date;

public class WaKuangJlType implements Converter<Integer> {
    @Override
    public Class<Date> supportJavaTypeKey() {
        return Date.class;
    }


    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        Integer value = context.getValue();
        if (value==0)return new WriteCellData<>("其他");
        if (value==1)return new WriteCellData<>("团队奖励");
        if (value==2)return new WriteCellData<>("直推奖励");
        if (value==3)return new WriteCellData<>("质押奖励");
        if (value==4)return new WriteCellData<>("游戏加减资产");
        if (value==5)return new WriteCellData<>("团队质押奖励");
        if (value==6)return new WriteCellData<>("动态奖励");
        if (value==7)return new WriteCellData<>("节点奖励");
        if (value==8)return new WriteCellData<>("映射奖励");
        if (value==9)return new WriteCellData<>("特殊奖励");
        return new WriteCellData<>("");
    }
}
