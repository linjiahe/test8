//package com.company.project.controller;
//
//import com.company.project.common.utils.DataResult;
//import com.company.project.entity.WaKuangJl;
//import com.company.project.entity.WaKuangJl2;
//import com.company.project.mapper.WaKuangJlMapper;
//import com.company.project.service.WaKuangJl2Service;
//import com.company.project.service.WaKuangJlService;
//import io.swagger.annotations.Api;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Api(tags = "奖励记录管理")
//@RestController
//@RequestMapping("/data/wakuang")
//@Slf4j
//public class WaKuangJlController {
//
//    @Resource
//    private WaKuangJlService waKuangJlService;
//
//    @Resource
//    private WaKuangJl2Service waKuangJl2Service;
//
//    @Resource
//    private WaKuangJlMapper waKuangJlMapper;
//
//    @PostMapping("/jl/pageInfo")
//    public DataResult pageInfo(@RequestBody WaKuangJl vo) {
//        return DataResult.success(waKuangJlService.pageInfo(vo));
//    }
//
//    @PostMapping("/jl/pageInfoExport")
//    public void pageInfoExport(@RequestBody WaKuangJl vo, HttpServletResponse response) throws IOException {
//        waKuangJlService.pageInfoExport(vo, response);
//    }
//
//    @PostMapping("/jl/selectSum")
//    public DataResult selectSum(@RequestBody WaKuangJl vo) {
//        return DataResult.success(waKuangJlMapper.selectSum(vo.getCoin()));
//    }
//
//    @PostMapping("/jl2/pageInfo")
//    public DataResult pageInfo2(@RequestBody WaKuangJl2 vo) {
//        return DataResult.success(waKuangJl2Service.pageInfo(vo));
//    }
//
//    @PostMapping("/jl2/pageInfo-les")
//    public DataResult pageInfo2Les(@RequestBody WaKuangJl2 vo) {
//        return DataResult.success(waKuangJl2Service.pageInfoLes(vo));
//    }
//
//}
