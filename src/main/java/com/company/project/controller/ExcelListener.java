package com.company.project.controller;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.company.project.entity.UsdtBsc;
import com.company.project.service.UsdtBscService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


public class ExcelListener extends AnalysisEventListener<UsdtBsc> {

    private UsdtBscService usdtBscService;


    /**
     * 提供ExcelListener的构造器 注入userService
     */
    public ExcelListener(UsdtBscService usdtBscService) {
        this.usdtBscService = usdtBscService;
    }

    /**
     * 定义接受数据的list
     */
    List<UsdtBsc> list = new ArrayList<>();

    /**
     * 数据是一条一条读取的
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(UsdtBsc data, AnalysisContext context) {
        list.add(data);
        //这里做了批量插入的操作 所以在 doAfterAllAnalysed方法需要判断list是否为空 不为空的话需要再次插入 防止数据缺失
        if (list.size() == 10) {
            //执行插入操作
            usdtBscService.addList(list);
            //清理list
            list.clear();
        }
    }

    /**
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //不为空执行插入操作
        if (!CollectionUtils.isEmpty(list)) {
            //xx.save(list)
            usdtBscService.addList(list);
            //清理list
            System.out.println(list);
            list.clear();
        }
    }
}