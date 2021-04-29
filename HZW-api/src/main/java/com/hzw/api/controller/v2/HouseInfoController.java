package com.hzw.api.controller.v2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.api.domain.CompanyBasicInfo2;
import com.hzw.api.domain.CompanyWarehouseInfo;
import com.hzw.api.mapper.CompanyBasicInfoMapper2;
import com.hzw.api.mapper.CompanyWarehouseInfoMapper;
import com.hzw.api.yreturn.ResponseResult;
import com.hzw.common.exception.api.ApiException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@RestController
@Validated
@Slf4j
@RequestMapping("/api/v2/platform")
public class HouseInfoController {
    @Autowired
    CompanyWarehouseInfoMapper houseInfoMapper;
    @Autowired
    CompanyBasicInfoMapper2 companyBasicInfoMapper2;

    @ResponseResult
    @PostMapping("/addStore")
    @ApiOperation("上传仓库")
    public int addStore(@RequestBody @Valid CompanyWarehouseInfo companyWarehouseInfo, HttpServletRequest request) {
        //插入企业征信代码
        QueryWrapper<CompanyBasicInfo2> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.like("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper2);
        Long appId = companyBasicInfo2.getCompanyid();
        //添加企业实体类查询
        companyWarehouseInfo.setCompanyid(appId);
        QueryWrapper<CompanyWarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Warehousesid", companyWarehouseInfo.getWarehousesid());
        queryWrapper.eq("companyid", appId);
        int count = houseInfoMapper.selectCount(queryWrapper);
        if (count != 0) {
            throw new ApiException("已有相同仓库编码", 10000);
        }
        //插入
        int insert = houseInfoMapper.insert(companyWarehouseInfo);
        if (insert == 1) {
            return insert;
        } else {
            throw new ApiException("插入失败", 500);
        }
    }
    @ResponseResult
    @PostMapping("/recomposeStore")
    @ApiOperation("修改贮存仓库")
    public int recomposeStore(@RequestBody @Valid CompanyWarehouseInfo companyWarehouseInfo, HttpServletRequest request) {
        //插入企业征信代码
        QueryWrapper<CompanyBasicInfo2> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.like("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper2);
        Long appId = companyBasicInfo2.getCompanyid();

        //添加企业实体类查询
        companyWarehouseInfo.setCompanyid(appId);
        QueryWrapper<CompanyWarehouseInfo> queryWrapper = new QueryWrapper<>();
        System.out.println(companyWarehouseInfo.getWareHousId());
        queryWrapper.like("Warehousesid", companyWarehouseInfo.getWarehousesid());
        queryWrapper.like("companyid", appId);
        CompanyWarehouseInfo companyWarehouseInfo1 = houseInfoMapper.selectOne(queryWrapper);
        if (companyBasicInfo2 != null) {
            int insert = houseInfoMapper.update(companyWarehouseInfo, queryWrapper);
            //如果更新为空的会怎么样
            if (insert == 0) {
                throw new ApiException("修改失败", 500);

            } else {
                return insert;
            }
        } else {
            throw new ApiException("无此仓库", 10000);
        }
    }

    @ResponseResult
    @GetMapping("/delStore")
    @ApiOperation("刪除贮存仓库")
    public int delStore(@RequestBody HashMap<String, String> map, HttpServletRequest request) {

        //插入企业征信代码

        String sid = map.get("wareHouseSid");
        QueryWrapper<CompanyBasicInfo2> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.like("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper2);
        Long appId = companyBasicInfo2.getCompanyid();
        QueryWrapper<CompanyWarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("wareHousId", sid);
        queryWrapper.like("companyid", appId);
        CompanyWarehouseInfo companyWarehouseInfo = houseInfoMapper.selectOne(queryWrapper);
        if (companyBasicInfo2 != null) {
            int insert = houseInfoMapper.delete(queryWrapper);
            System.out.println(insert);
            return insert;

        } else {
            throw new ApiException("无此仓库", 10000);
        }
//        if (insert==1){
//            return  insert;
//        }
//        else {
//            throw new RuntimeException("删除失败");
//        }
    }
    @ResponseResult
    @GetMapping("/listAll")
    @ApiOperation("查看全部仓库")
    public List<CompanyWarehouseInfo> listAll(HttpServletRequest request) {
        QueryWrapper<CompanyBasicInfo2> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.like("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper2);
        Long appId = companyBasicInfo2.getCompanyid();
        QueryWrapper<CompanyWarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("companyid", appId);
        return houseInfoMapper.selectList(queryWrapper);
    }
}
