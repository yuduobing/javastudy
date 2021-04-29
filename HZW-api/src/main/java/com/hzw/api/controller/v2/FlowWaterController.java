package com.hzw.api.controller.v2;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.api.domain.*;
import com.hzw.api.domain.revisedomain.DWzBale;
import com.hzw.api.domain.revisedomain.Revisehistory;
import com.hzw.api.mapper.*;
import com.hzw.api.yreturn.ResponseResult;
import com.hzw.api.yreturn.ResultCode;
import com.hzw.common.config.HZWConfig;
import com.hzw.common.core.text.Convert;
import com.hzw.common.exception.api.ApiException;
import com.hzw.common.utils.StringUtils;
import com.hzw.common.utils.file.FileUploadUtils;
import com.hzw.framework.config.ServerConfig;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@RestController
@Slf4j
@RequestMapping("/api/v2/platform")
public class FlowWaterController {
    @Autowired
    private ServerConfig serverConfig;
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
    LeaveBaleMapper leaveBaleMapper;
    @Autowired
    TrackInfoMapper trackInfoMapper;
    @Autowired
    RevisehistoryMapper revisehistoryMapper;
    @Autowired
    ContactINfoMapper contactINfoMapper;
    @Autowired
    SysAttachmentMapper sysAttachmentMapper;
    @Autowired
    DWzBaleMapper dwzBaleMapper;

    //溯源码截取首页4位
    /*
     溯源码获取主键前4位补全，时间搓转换
     */
    @PostMapping("/createTraceSn")
    @ApiOperation("获取溯源码")
    public ResultCode createTraceSn(@RequestBody Map<String, Object> map, HttpServletRequest request) {
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
                throw new ApiException("没有devicesn", 10000);
            }
            int length = deviceSn.length();
            if (length != 6) {
                throw new ApiException("设备编号为6位", 10000);
            }
            if (map.get("timestamp").toString().length() != 10) {
                throw new ApiException("时间搓为10位", 10000);
            }
            String tracesn = FormatCode(companyBasicInfo2.getCompanyid().toString()) + deviceSn + date;
            map.put("appId", tracesn);
            map.put("tracesn", tracesn);
            map.put("createtime", new Date());
            int a = companyBasicInfoMapper2.insertsn(map);
            return new ResultCode(0, "success", tracesn);

        } else {
            throw new ApiException("企业征信代码错误", 10000);
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
            throw new ApiException("溯源码已使用", 10000);
        }
        Long appId = companyBasicInfo2.getCompanyid();
        //查询大包id插入小包
        bale.setCompanyid(appId);
        //补全溯源码并插入小包
        bale.getZipPackages().forEach(user -> {
                    user.setTraceSn(bale.getTraceSn());
                    bale.setSitesn(user.getSiteSn());
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
            throw new ApiException(String.valueOf(insert), 10000);
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
            throw new ApiException("请勿重复入库", 10000);
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
//            根据溯源码和内部编码更新小包信息
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
            throw new ApiException("请勿重复出库", 10000);
        }
    }

    @ResponseResult
    @ApiOperation("出厂上报")
    @PostMapping("/deliveryUpload")
    public int deliveryUpload(@RequestBody @Valid LeaveBale leaveBale, HttpServletRequest request) {

        String appId = request.getHeader("appId");
        String[] tracePackages = leaveBale.getTracePackages();
        QueryWrapper<BaleStatues> queryWrapperB = new QueryWrapper<>();
        queryWrapperB.eq("traceSn", tracePackages[0]);
        BaleStatues storeBale = baleStatuesMapper.selectOne(queryWrapperB);
        if (storeBale != null && storeBale.getRecordStatus() == 3) {
            leaveBale.setRecordStatus(3);
            /*根据联单号更新流水表*/
            for (int i = 0; i < tracePackages.length; i++) {
                QueryWrapper<LeaveBale> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tracesn ", tracePackages[i]);
                leaveBaleMapper.update(leaveBale, queryWrapper);
            }
            /*插入联单*/
            QueryWrapper<CompanyBasicInfo2> queryWrapperb = new QueryWrapper<>();
            queryWrapperb.eq("creditcode", appId);
            CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapperb);
            TrackInfo trackInfo = new TrackInfo();
            trackInfo.setProdcompanyid(companyBasicInfo2.getCompanyid().intValue());
            trackInfo.setTracknumber(leaveBale.getTransferOrderSn());
            //转移时间
            trackInfo.setTransfertime(leaveBale.getTimestamp());
            //转移重量
            trackInfo.setTransferweight(leaveBale.getWight());
            trackInfo.setState(1);
            trackInfo.setCarnum(leaveBale.getTransferCarNumber());
            return trackInfoMapper.insert(trackInfo);
        } else {
            throw new ApiException("请勿重复出厂", 10000);
        }

    }


    /*
     *出厂修改
     * */

    /*
     *
     * 处置企业
     *复用流水的出厂实体类
     */
    @ResponseResult
    @ApiOperation("处置企业入厂")
    @PostMapping("/disposeEntryUpload")
    public int disposeEntryUpload(@RequestBody @Valid DleaveBAle leaveBale, HttpServletRequest request) {

        QueryWrapper<TrackInfo> queryWrappert2 = new QueryWrapper<>();
        queryWrappert2.eq("tracknumber", leaveBale.getTransferOrderSn());
        TrackInfo storeBale = trackInfoMapper.selectOne(queryWrappert2);

        if (storeBale.getState() == 2) {
            throw new ApiException("已有此联单号", 10000);
        }
        //判断联单号是否多次创穿
        String appId = request.getHeader("appId");
        QueryWrapper<CompanyBasicInfo2> queryWrapperb = new QueryWrapper<>();
        queryWrapperb.eq("creditcode", appId);
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapperb);
        TrackInfo trackInfo = new TrackInfo();
        trackInfo.setProdcompanyid(companyBasicInfo2.getCompanyid().intValue());
        //联单号
        trackInfo.setTracknumber(leaveBale.getTransferOrderSn());
        //转移时间dealreceivetime
        trackInfo.setDealreceivetime(leaveBale.getTimestamp());
        //转移重量receiveweight
        trackInfo.setReceiveweight(leaveBale.getWeight());
        trackInfo.setState(2);
        //车牌号
        trackInfo.setCarnum(leaveBale.getTransferCarNumber());
        QueryWrapper<TrackInfo> queryWrappert = new QueryWrapper<>();

        queryWrappert.eq("tracknumber", leaveBale.getTransferOrderSn());
//        queryWrapperb.eq("carnum", leaveBale.getTransferCarNumber());

        if (storeBale != null) {

            return trackInfoMapper.update(trackInfo, queryWrappert);
        } else {

            return trackInfoMapper.insert(trackInfo);
        }
    }

    @ResponseResult
    @ApiOperation("处置企业入库上报")
    @PostMapping("/disposeReceiptUpload")
    public int disposeReceiptUpload(@RequestBody @Valid Dealrecord dealrecord, HttpServletRequest request) {
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        Long appId = companyBasicInfo2.getCompanyid();
        dealrecord.setCompanyid(appId);
        dealrecord.setRecordstatus(1);
        System.out.println(dealrecord);
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
//        JSONObject jsonObjectxgq = JSONObject.parseObject(leaverecordMapper.selectOne(queryWrapper));
        QueryWrapper<DWzBale> queryWrapperwz = new QueryWrapper<>();
        queryWrapperwz.eq("innersn ", leaverecord.getInnersn());

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dwzBaleMapper.selectOne(queryWrapperwz));
        if (jsonObject.get("recordstatus") != null && Convert.toInt(jsonObject.get("recordstatus")) != 1) {
            throw new ApiException("请勿重复出库", 10000);
        }
        leaverecord.setRecordstatus(2);
        int insert = leaverecordMapper.update(leaverecord, queryWrapper);
        return insert;

    }

    /*
    修改申请查询
     */
    @ResponseResult
    @ApiOperation("修改申请查询")
    @PostMapping("/applyQuery")
    public HashMap<String, Object> applyQuery(@RequestBody @Valid Map<String, Object> map, HttpServletRequest request) {

        String modifysn = Convert.toStr(map.get("modifySn"));
        if (StringUtils.isEmpty(modifysn)) {
            throw new ApiException("修改申请序列号不能为空", 10000);
        }
        QueryWrapper<Revisehistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("modifysn", modifysn);
        Revisehistory revisehistory = revisehistoryMapper.selectOne(queryWrapper);
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("modifyStatus ", revisehistory.getModifystatus());
        stringObjectHashMap.put("remark ", revisehistory.getComment());
        return stringObjectHashMap;

    }

    /**
     * 合同信息文件上传
     * @param file
     * @param contactINfo
     * @param request
     * @return
     * @throws IOException
     */
    @ResponseResult
    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    public int fileUpload(MultipartFile file, ContactInfo contactINfo, HttpServletRequest request) throws IOException {
        //上传文件
        String filePath = HZWConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        //插入附件表
        long uuid = System.currentTimeMillis();
        SysAttachment sysAttachment = new SysAttachment();
        sysAttachment.setAttachment_Id(uuid);
        sysAttachment.setRemark(file.getOriginalFilename());
        sysAttachment.setName(fileName);
        sysAttachment.setLength(Convert.toFloat(file.getSize()));
        sysAttachment.setSuffix(fileName.substring(fileName.indexOf(".")));
        sysAttachmentMapper.insert(sysAttachment);
        //插入合同表
        QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creditcode", request.getHeader("appId"));
        CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
        contactINfo.setCompanyid(companyBasicInfo2.getCompanyid());
        contactINfo.setContractattachmentid(uuid);
        contactINfoMapper.insert(contactINfo);
        return contactINfoMapper.insert(contactINfo);

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
            throw new ApiException("时间戳格式不低", 10000);
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

}
