package com.hzw.api.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.api.domain.*;
import com.hzw.api.domain.revisedomain.BaleR;
import com.hzw.api.domain.revisedomain.SmallBaleR;
import com.hzw.api.mapper.*;
import com.hzw.api.yreturn.ResponseResult;
import com.hzw.common.exception.api.ApiException;
import com.hzw.common.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@RestController
@Slf4j
@RequestMapping("/api/revise")
public class RevieseControllerV1 {
    @Autowired
    BaleMapper flowWaterMapper;
    @Autowired
    SmallBaleMapper smallBaleMapper;
    @Autowired
    OutBaleMapper outBaleMapper;
    @Autowired
    StoreBaleMapper storeBaleMapper;
    @Autowired
    DealrecordMapper dealrecordMapper;
    @Autowired
    LeaverecordMapper leaverecordMapper;
    @Autowired
    CompanyBasicInfoMapper2 companyBasicInfoMapper2;
    @Autowired
    BaleStatuesMapper baleStatuesMapper;
    @Autowired
    BaleRMapper baleRMapper;
    @Autowired
    SmallBaleRMapper smallBaleRMapper;

    @ResponseResult
    @PostMapping("/generateUpload")
    public int generateUpload(@RequestBody @Valid BaleR baler, HttpServletRequest request) {

        //判断溯源码是否用过
        QueryWrapper<BaleR> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("traceSn", baler.getTraceSn());
        BaleR storeBales = baleRMapper.selectOne(queryWrapper2);
        if (storeBales.getRecordStatus() != 0) {
            //throw new RuntimeException("不能修改");
            throw new ApiException("参数错误", 10000);
        }

        //补全代码
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        Long appId = companyBasicInfo2.getCompanyid();
        queryWrapper2.eq("CompanyId", appId);
        //更新打大包

        baler.getZipPackages().forEach(user -> {

                    QueryWrapper<SmallBaleR> queryWrapperR = new QueryWrapper<>();
                    queryWrapperR.eq("innerSn", user.getInnerSn());
                    queryWrapperR.eq("traceSn", baler.getTraceSn());
                    int updates = smallBaleRMapper.update(user, queryWrapperR);
                    if (updates >= 1) {
                        //throw new RuntimeException("小包内部编码不唯一");
                        throw new ApiException("参数错误", 10000);
                    }
                }
        );
        int update = baleRMapper.update(baler, queryWrapper2);

        if (update == 1) {
            return update;
        } else {
            throw new RuntimeException(String.valueOf(update));
        }

    }

    @ResponseResult
    @ApiOperation("暂存库入库上报")
    @PostMapping("/inStoreHouseUpload")
    public int inStoreHouseUpload(@RequestBody @Valid BaleR baler, HttpServletRequest request) {

        //判断溯源码是否用过
        QueryWrapper<BaleR> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("traceSn", baler.getTraceSn());
        BaleR storeBales = baleRMapper.selectOne(queryWrapper2);
        if (storeBales.getRecordStatus() != 1) {
            //throw new RuntimeException("不能修改");
            throw new ApiException("参数错误", 10000);
        }

        //补全代码
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        Long appId = companyBasicInfo2.getCompanyid();
        queryWrapper2.eq("CompanyId", appId);
        //更新打大包

        baler.getZipPackages().forEach(user -> {

                    QueryWrapper<SmallBaleR> queryWrapperR = new QueryWrapper<>();
                    queryWrapperR.eq("innerSn", user.getInnerSn());
                    queryWrapperR.eq("traceSn", baler.getTraceSn());
                    int updates = smallBaleRMapper.update(user, queryWrapperR);
                    if (updates >= 1) {
                        //throw new RuntimeException("小包信息错误");
                        throw new ApiException("参数错误", 10000);
                    }
                }
        );
        baler.setRecordStatus(1);
        int update = baleRMapper.update(baler, queryWrapper2);

        if (update == 1) {
            return update;
        } else {
            throw new RuntimeException(String.valueOf(update));
        }

    }

    @ResponseResult
    @ApiOperation("暂存库出库上报")
    @PostMapping("/outStoreHouseUpload")
    public int outStoreHouseUpload(@RequestBody @Valid BaleR baler, HttpServletRequest request) {

        //判断溯源码是否用过
        QueryWrapper<BaleR> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("traceSn", baler.getTraceSn());
        BaleR storeBales = baleRMapper.selectOne(queryWrapper2);
        if (storeBales.getRecordStatus() != 2) {
            //throw new RuntimeException("不能修改");
            throw new ApiException("参数错误", 10000);
        }

        //补全代码
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        Long appId = companyBasicInfo2.getCompanyid();
        queryWrapper2.eq("CompanyId", appId);
        //更新打大包

        baler.getZipPackages().forEach(user -> {

                    QueryWrapper<SmallBaleR> queryWrapperR = new QueryWrapper<>();
                    queryWrapperR.eq("innerSn", user.getInnerSn());
                    queryWrapperR.eq("traceSn", baler.getTraceSn());
                    int updates = smallBaleRMapper.update(user, queryWrapperR);
                    if (updates >= 1) {
                        //throw new RuntimeException("小包信息错误");
                        throw new ApiException("参数错误", 10000);
                    }
                }
        );
        baler.setRecordStatus(2);
        int update = baleRMapper.update(baler, queryWrapper2);

        if (update == 1) {
            return update;
        } else {
            throw new RuntimeException(String.valueOf(update));
        }

    }

}
