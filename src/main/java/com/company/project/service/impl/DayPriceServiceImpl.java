package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.DayPrice;
import com.company.project.mapper.DayPriceMapper;
import com.company.project.service.DayPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class DayPriceServiceImpl extends ServiceImpl<DayPriceMapper, DayPrice> implements DayPriceService {

    @Resource
    private DayPriceMapper dayPriceMapper;

    @Override
    public void updateWallet(DayPrice dayPrice) {
        dayPriceMapper.updateById(dayPrice);
    }

    @Override
    public IPage<DayPrice> pageInfo(DayPrice vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        return dayPriceMapper.selectPage(page,null);
    }
}
