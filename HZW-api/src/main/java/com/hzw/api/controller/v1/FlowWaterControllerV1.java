package com.hzw.api.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.api.domain.*;
import com.hzw.api.mapper.*;
import com.hzw.api.yreturn.ResponseResult;
import com.hzw.api.yreturn.ResultCode;
import com.hzw.common.core.text.Convert;
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
@RequestMapping("/api/platform")
public class FlowWaterControllerV1 {
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

    //溯源码截取首页4位
    /*
     溯源码获取主键前4位补全，时间搓转换
     */
    @PostMapping("/createTraceSn")
    @ApiOperation("获取溯源码")
    public ResultCode createTraceSn(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        map.forEach((key, Value) -> {
            if (Value instanceof String) {
                if (key == "timestamp") {
                    //throw new RuntimeException(key + "不符合类型");
                    throw new ApiException("参数错误", 10000);
                }
            } else if (Value instanceof Integer) {
                if (key == "deviceSn") {
                    //throw new RuntimeException(key + "不符合类型");
                    throw new ApiException("参数错误", 10000);
                }
                if (key == "sign") {
                    //throw new RuntimeException(key + "不符合类型");
                    throw new ApiException("参数错误", 10000);
                }
            }
        });
        String appId = request.getHeader("appId");
//        System.out.println(companyBasicInfo);
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", appId);
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        if (companyBasicInfo2 != null) {
//             转换时间搓
            String date = timeStamp2Date(map.get("timestamp").toString(), "yyMMddHHmmss");
            String deviceSn = Convert.toStr(map.get("deviceSn"));
            if (StringUtils.isEmpty(deviceSn)) {
                //throw new RuntimeException("没有devicesn");
                throw new ApiException("没有devicesn", 10000);
            }
            int length = deviceSn.length();
            if (length != 6) {
                //throw new RuntimeException("设备编号为6位");
                throw new ApiException("设备编号为6位", 10000);
            }
            if (map.get("timestamp").toString().length() != 10) {
                //throw new RuntimeException("时间戳为10位");
                throw new ApiException("时间戳为10位", 10000);
            }
            String tracesn = FormatCode(companyBasicInfo2.getCompanyid().toString()) + deviceSn + date;
            map.put("appId", tracesn);
            map.put("tracesn", tracesn);
            map.put("createtime", new Date());
            int a = companyBasicInfoMapper2.insertsn(map);
            return new ResultCode(0, "success", tracesn);

        } else {
            //throw new RuntimeException("企业征信代码错误");
            throw new ApiException("参数错误", 10000);
        }
    }


    @ApiOperation("产废点上报")
    @ResponseResult
    @PostMapping("/generateUpload")
    public int generateUpload(@RequestBody @Valid Bale bale, HttpServletRequest request) {
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        //判断溯源码是否用过
        QueryWrapper<Bale> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("traceSn", bale.getTraceSn());

        int storeBales = flowWaterMapper.selectCount(queryWrapper2);
        if (storeBales != 0) {
            //throw new RuntimeException("溯源码已使用");
            throw new ApiException("溯源码已使用", 10000);
        }
        Long appId = companyBasicInfo2.getCompanyid();
        //查询大包id插入小包
        bale.setCompanyid(appId);
        //补全溯源码并插入小包
        bale.getZipPackages().forEach(user -> {
                    user.setTraceSn(bale.getTraceSn());

                    QueryWrapper<SmallBale> queryWrappers = new QueryWrapper<>();
                    queryWrappers.eq("innerSn", user.getInnerSn());
                    int scount = smallBaleMapper.selectCount(queryWrappers);
                    if (scount != 0) {
//                        throw new RuntimeException(user.getInnerSn()+"内部编码溯源码已使用");
                        throw new ApiException("内部编码溯源码已使用", 10000);
                    }
                    smallBaleMapper.insert(user);
                }
        );
        //设置产出状态RecordStatus
        bale.setRecordStatus(0);
        //存到大包
        int insert = flowWaterMapper.insert(bale);
//        flowWaterMapper.insertsmallbabel()
        //存到小包
        if (insert == 1) {
            return insert;
        } else {
            //throw new RuntimeException(String.valueOf(insert));
            throw new ApiException("参数错误", 10000);
        }
    }

    @ResponseResult
    @ApiOperation("暂存库入库上报")
    @PostMapping("/inStoreHouseUpload")
    public int inStoreHouseUpload(@RequestBody @Valid StoreBale bale) {

        QueryWrapper<BaleStatues> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("traceSn", bale.getTraceSn());
        BaleStatues storeBale = baleStatuesMapper.selectOne(queryWrapper2);
        QueryWrapper<StoreBale> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("traceSn", bale.getTraceSn());
        if (storeBale != null && storeBale.getRecordStatus() == 0) {
            bale.setRecordStatus(1);
            int a = storeBaleMapper.update(bale, queryWrapper);
            return a;
        } else {
            //throw new RuntimeException("请先上传产废信息");
            throw new ApiException("请先上传产废信息", 10000);
        }

    }

    @ResponseResult
    @ApiOperation("暂存库出库上报")
    @PostMapping("/outStoreHouseUpload")
    public int outStoreHouseUpload(@RequestBody @Valid OutBale outBale) {
        //补全溯源码并更新小包
        QueryWrapper<OutBale> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("traceSn", outBale.getTraceSn());
        QueryWrapper<BaleStatues> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("traceSn", outBale.getTraceSn());
        BaleStatues storeBale = baleStatuesMapper.selectOne(queryWrapper);
        if (storeBale != null && storeBale.getRecordStatus() == 1) {
            outBale.getZipPackages().forEach(user -> {
                        user.setTraceSn(outBale.getTraceSn());
                        //根据溯源码和内部编码更新小包信息
                        QueryWrapper<SmallBale> queryWrapper3 = new QueryWrapper<>();
                        queryWrapper3.eq("innerSn ", user.getInnerSn());
                        queryWrapper3.eq("traceSn ", outBale.getTraceSn());
                        smallBaleMapper.update(user, queryWrapper3);
                    }
            );
            outBale.setRecordStatus(2);
            int a = outBaleMapper.update(outBale, queryWrapper2);
            return a;
        } else {
            //throw new RuntimeException("请先上传入库信息");
            throw new ApiException("请先上传入库信息", 10000);
        }
    }

    @ResponseResult
    @ApiOperation("处置企业上报")
    @PostMapping("/disposeReceiptUpload")
    public int disposeReceiptUpload(@RequestBody @Valid Dealrecord dealrecord, HttpServletRequest request) {
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        Long appId = companyBasicInfo2.getCompanyid();
        dealrecord.setCompanyid(appId);
        dealrecord.setRecordstatus(1);
        int insert = dealrecordMapper.insert(dealrecord);
        return insert;
    }

    @ResponseResult
    @ApiOperation("出库上报")
    @PostMapping("/disposeLeaveUpload")
    public int disposeLeaveUpload(@RequestBody @Valid Leaverecord leaverecord, HttpServletRequest request) {
        String appId = request.getHeader("appId");
        QueryWrapper<Leaverecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("innersn ", leaverecord.getInnersn());
        leaverecord.setRecordstatus(2);

        log.warn(String.valueOf(leaverecord));
        int insert = leaverecordMapper.update(leaverecord, null);
        return insert;

    }

    //字符串补全
    public String FormatCode(String code) {
        String[] codeArr = code.split(",");
        List<String> newCode = new ArrayList();
        DecimalFormat format = new DecimalFormat("0000");
        String[] var5 = codeArr;
        int var6 = codeArr.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            String c = var5[var7];
            newCode.add(format.format(Integer.parseInt(c)));
        }
        return StringUtils.join(newCode, ",");
    }

    public String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            //throw new RuntimeException("时间戳格式不低");
            throw new ApiException("参数错误", 10000);
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.parseLong(seconds + "000")));
    }

}
