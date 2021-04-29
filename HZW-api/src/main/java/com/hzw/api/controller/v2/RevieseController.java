package com.hzw.api.controller.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hzw.api.domain.*;

import com.hzw.api.domain.revisedomain.*;
import com.hzw.api.mapper.*;
import com.hzw.api.yreturn.ResponseResult;
import com.hzw.api.yreturn.ResultCode;
import com.hzw.common.core.text.Convert;
import com.hzw.common.exception.api.ApiException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
/*
 * 取出实体类判断一下且如果有多余参数说不合格
 * */

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@RestController
@Slf4j
@RequestMapping("/api/v2/revise")
public class RevieseController {
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
    @Autowired
    WzBaleMapper wzBaleMapper;
    @Autowired
    RevisehistoryMapper revisehistoryMapper;
    @Autowired
    TrackInfoMapper trackInfoMapper;
    @Autowired
    InStoreBaleMapper inStoreBaleMapper;

    //是否通过  。modifysn statues 3不通过2通过  reason
    //*   通过后执行
    @PostMapping("/updateAfter")
    public ResultCode updateAfter(@RequestBody @Valid JSONObject jsonObject, HttpServletRequest request) {

        QueryWrapper<Revisehistory> queryWrapperR = new QueryWrapper<>();
        queryWrapperR.eq("modifysn", Convert.toStr(jsonObject.get("modifysn")));
        Revisehistory revisehistory = new Revisehistory();
        revisehistory.setModifystatus(Convert.toInt(jsonObject.get("statues")));
        revisehistory.setReason(Convert.toStr(jsonObject.get("reason")));
        int a = revisehistoryMapper.update(revisehistory, queryWrapperR);
        //判断是否通过
        if (Convert.toInt(jsonObject.get("statues")) == 3) {

            return new ResultCode(0, "success", a);
        }
        Revisehistory revisehistory1 = revisehistoryMapper.selectOne(queryWrapperR);
        String map = revisehistory1.getXgq();
        String mas = revisehistory1.getXgh();
        JSONObject jsonObjectxgq = JSONObject.parseObject(map);
        JSONObject jsonObjectxqh = JSONObject.parseObject(mas);

        int recordstatus = Convert.toInt(jsonObjectxgq.get("recordstatus"));
        //生产点修改
        if (recordstatus == 0) {
            BaleR baler = JSON.toJavaObject(jsonObjectxqh, BaleR.class);

            QueryWrapper<BaleR> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("traceSn", baler.getTraceSn());

            //更新小包
            baler.getZipPackages().forEach(user -> {
                        QueryWrapper<SmallBaleR> queryWrapperRs = new QueryWrapper<>();
                        queryWrapperRs.eq("innerSn", user.getInnerSn());
                        queryWrapperRs.eq("traceSn", baler.getTraceSn());
                        int updates = smallBaleRMapper.update(user, queryWrapperRs);
                        if (updates > 1) {
                            throw new ApiException("小包内部编码不唯一", 10000);
                        }
                    }
            );
            //状态不改变

            int update = baleRMapper.update(baler, queryWrapper2);

        }
        //暂存库入库上报修改
        if (recordstatus == 1) {
            InStoreBale baler = JSON.toJavaObject(jsonObjectxqh, InStoreBale.class);

            QueryWrapper<InStoreBale> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("traceSn", baler.getTraceSn());

            //状态不改变

            int update = inStoreBaleMapper.update(baler, queryWrapper2);
        }
        //出厂修改

        if (recordstatus == 2) {

            JSONArray tracePackages = jsonObjectxqh.getJSONArray("tracePackages");
            String modifyOrderSn = Convert.toStr(jsonObjectxqh.get("modifyOrderSn"));
            //要修改的车牌号
            String transferCarNumber = Convert.toStr(jsonObjectxqh.get("transferCarNumber"));
            String loadoMeter = Convert.toStr(jsonObjectxqh.get("loadoMeter"));
            //插入流水
            WzBale wzBale = new WzBale();
            wzBale.setTransferordersn(modifyOrderSn);
            wzBale.setTransferordersn(modifyOrderSn);

            //设置出厂地磅
            wzBale.setOutweighbridge(loadoMeter);
            wzBale.setTransfercarnumber(transferCarNumber);
            //插入联单
            TrackInfo trackInfos = new TrackInfo();
            trackInfos.setTracknumber(modifyOrderSn);
            trackInfos.setCarnum(transferCarNumber);
            //更新流水表的信息
            for (int i = 0; i < tracePackages.size(); i++) {

                QueryWrapper<WzBale> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tracesn ", tracePackages.get(i));
                wzBaleMapper.update(wzBale, queryWrapper);

            }
            QueryWrapper<TrackInfo> queryWrappert = new QueryWrapper<>();
            queryWrappert.eq("tracknumber ", modifyOrderSn);
            trackInfoMapper.update(trackInfos, queryWrappert);
        }
        //更新状态

        return new ResultCode(0, "success", a);
    }

    //*   生产点修改
    @PostMapping("/generateUpload")
    public ResultCode generateUpload(@RequestBody @Valid BaleR baler, HttpServletRequest request) {
        //记录以前的更新现在的
        //修改生成序列号
        //判断溯源码是否用过
        QueryWrapper<WzBale> queryWrapperWz = new QueryWrapper<>();
        queryWrapperWz.eq("traceSn", baler.getTraceSn());
        WzBale storeBales = wzBaleMapper.selectOne(queryWrapperWz);
        /*z这里改状态*/
        if (storeBales == null) {
            throw new ApiException("溯源码有误", 10000);
        }
        if (storeBales != null && storeBales.getRecordstatus() != 0) {
            throw new ApiException("不能修改", 10000);
        }
        long cha = System.currentTimeMillis() - storeBales.getProducetime().getTime();
        double result = cha * 1.0 / (1000 * 60 * 60);
        //修改的数据
        Revisehistory revisehistory = new Revisehistory();
        String xgq = JSON.toJSONString(storeBales);
        String xgh = JSON.toJSONString(baler);
        revisehistory.setTracesn(baler.getTraceSn());
        //修改原因
        revisehistory.setReason(baler.getReason());
        revisehistory.setXgq(xgq);
        revisehistory.setXgh(xgh);
        revisehistory.setSubmitTime(new Date());
        //原数据
        if (result >= 24) {
            //原数据
            revisehistory.setModifystatus(1);
        } else {
            revisehistory.setModifystatus(2);
            //补全代码此处的apppid是征信代码
            QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("creditcode", request.getHeader("appId"));
            CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
            Long appId = companyBasicInfo2.getCompanyid();
            QueryWrapper<BaleR> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("traceSn", baler.getTraceSn());
            queryWrapper2.eq("CompanyId", appId);
            //更新小包
            baler.getZipPackages().forEach(user -> {
                        QueryWrapper<SmallBaleR> queryWrapperR = new QueryWrapper<>();
                        queryWrapperR.eq("innerSn", user.getInnerSn());
                        queryWrapperR.eq("traceSn", baler.getTraceSn());
                        int updates = smallBaleRMapper.update(user, queryWrapperR);
                        if (updates > 1) {
                            throw new ApiException("小包内部编码不唯一", 10000);
                        }
                    }
            );

            int update = baleRMapper.update(baler, queryWrapper2);
        }
        //生成溯源码插入
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String reverseID = sdf.format(System.currentTimeMillis());
        revisehistory.setModifysn(reverseID);
        int insert = revisehistoryMapper.insert(revisehistory);
        if (insert != 1) {
            throw new ApiException("修改失败", 500);

        }

        return new ResultCode(0, "success", reverseID);
    }

    @ResponseResult
    @ApiOperation("暂存库入库修改")
    @PostMapping("/inStoreHouseUpload")
    public ResultCode inStoreHouseUpload(@RequestBody @Valid InStoreBale baler, HttpServletRequest request) {

        //记录以前的更新现在的
        //修改生成序列号
        //判断溯源码是否用过
        QueryWrapper<WzBale> queryWrapperWz = new QueryWrapper<>();
        queryWrapperWz.eq("traceSn", baler.getTraceSn());
        WzBale storeBales = wzBaleMapper.selectOne(queryWrapperWz);
        /*z这里改状态*/
        if (storeBales != null && storeBales.getRecordstatus() != 1) {
            throw new ApiException("该流水不是入库状态", 10000);
        }
        long cha = System.currentTimeMillis() - storeBales.getPutintime().getTime();
        double result = cha * 1.0 / (1000 * 60 * 60);
        //修改的数据
        Revisehistory revisehistory = new Revisehistory();
        String xgq = JSON.toJSONString(storeBales);
        String xgh = JSON.toJSONString(baler);
        revisehistory.setTracesn(baler.getTraceSn());
        //修改原因
        revisehistory.setReason(baler.getReason());
        revisehistory.setXgq(xgq);
        revisehistory.setXgh(xgh);
        revisehistory.setSubmitTime(new Date());
        //原数据
        if (result >= 24) {
            //原数据
            revisehistory.setModifystatus(1);
        } else {
            revisehistory.setModifystatus(2);
            //补全代码此处的apppid是征信代码
            QueryWrapper<CompanyBasicInfo2> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("creditcode", request.getHeader("appId"));
            CompanyBasicInfo2 companyBasicInfo2 = companyBasicInfoMapper2.selectOne(queryWrapper);
            Long appId = companyBasicInfo2.getCompanyid();
            QueryWrapper<InStoreBale> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("traceSn", baler.getTraceSn());
            queryWrapper2.eq("CompanyId", appId);
            //更新小包
//            baler.getZipPackages().forEach(user -> {
//                        QueryWrapper<SmallBaleR> queryWrapperR = new QueryWrapper<>();
//                        queryWrapperR.eq("innerSn", user.getInnerSn());
//                        queryWrapperR.eq("traceSn", baler.getTraceSn());
//                        int updates = smallBaleRMapper.update(user, queryWrapperR);
//                        if (updates > 1) {
//                            throw new RuntimeException("小包内部编码不唯一");
//                        }
//                    }
//            );
//            baler.setRecordStatus(1);
            int update = inStoreBaleMapper.update(baler, queryWrapper2);
        }
        //生成溯源码插入
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String reverseID = sdf.format(System.currentTimeMillis());
        revisehistory.setModifysn(reverseID);
        int insert = revisehistoryMapper.insert(revisehistory);

        if (insert != 1) {
            throw new ApiException("修改失败", 500);

        }

        return new ResultCode(0, "success", reverseID);
    }

    /*
     * 要修改联单和流水表呕
     * */
    @ResponseResult
    @ApiOperation("产废企业出厂修改")
    @PostMapping("/outStoreHouseUpload")
    public ResultCode outStoreHouseUpload(@RequestBody @Valid JSONObject jsonObject, HttpServletRequest request) {

        if (jsonObject.get("tracePackages") == null) {
            throw new ApiException("溯源码集合不能为空", 10000);
        }
        //获取联单号
        JSONArray tracePackages = jsonObject.getJSONArray("tracePackages");
//        String[] tracePackages = new String[list.size()];
//
//        list.forEach(use->{
//            tracePackages.push(use);
//        });

        QueryWrapper<WzBale> queryWrapperWz = new QueryWrapper<>();
        queryWrapperWz.eq("traceSn", tracePackages.get(0));
        WzBale storeBales = wzBaleMapper.selectOne(queryWrapperWz);
        /*z这里改状态*/
        if (storeBales != null && storeBales.getRecordstatus() != 2) {
            throw new ApiException("暂未出厂", 10000);
        }
        //判断联单状态
        QueryWrapper<TrackInfo> queryWrapperTr = new QueryWrapper<>();
        queryWrapperTr.eq("trackNumber", storeBales.getTransferordersn());
        TrackInfo trackInfo = trackInfoMapper.selectOne(queryWrapperTr);
        /*
         * 如果上传联单号重复了怎么办
         * */

        //1多个溯源啊有为完成的
        if (trackInfo == null) {
            throw new ApiException("无此联单", 10000);
        }
        long cha = System.currentTimeMillis() - trackInfo.getTransfertime().getTime();
        double result = cha * 1.0 / (1000 * 60 * 60);
        //修改的数据

        Revisehistory revisehistory = new Revisehistory();
        String xgq = JSON.toJSONString(storeBales);
        String xgh = JSON.toJSONString(jsonObject);
        String trace = JSON.toJSONString(tracePackages);
        revisehistory.setTracesn(trace);
        //修改原因
        revisehistory.setReason(Convert.toStr(jsonObject.get("reason")));
        revisehistory.setXgq(xgq);
        revisehistory.setXgh(xgh);
        revisehistory.setSubmitTime(new Date());
        //原数据
        if (result >= 24) {
            //原数据
            revisehistory.setModifystatus(1);
        } else {
            revisehistory.setModifystatus(2);

            //要修改的联单号
            String modifyOrderSn = Convert.toStr(jsonObject.get("modifyOrderSn"));
            //要修改的车牌号
            String transferCarNumber = Convert.toStr(jsonObject.get("transferCarNumber"));
            String loadoMeter = Convert.toStr(jsonObject.get("loadoMeter"));
            //插入流水
            WzBale wzBale = new WzBale();
            wzBale.setTransferordersn(modifyOrderSn);
            wzBale.setTransferordersn(modifyOrderSn);

            //设置出厂地磅
            wzBale.setOutweighbridge(loadoMeter);
            wzBale.setTransfercarnumber(transferCarNumber);
            //插入联单
            TrackInfo trackInfos = new TrackInfo();
            trackInfos.setTracknumber(modifyOrderSn);
            trackInfos.setCarnum(transferCarNumber);
            //更新流水表的信息
            for (int i = 0; i < tracePackages.size(); i++) {

                QueryWrapper<WzBale> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tracesn ", tracePackages.get(i));
                wzBaleMapper.update(wzBale, queryWrapper);

            }
            QueryWrapper<TrackInfo> queryWrappert = new QueryWrapper<>();
            queryWrappert.eq("tracknumber ", modifyOrderSn);
            trackInfoMapper.update(trackInfos, queryWrappert);

        }
        //生成溯源码插入
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String reverseID = sdf.format(System.currentTimeMillis());
        revisehistory.setModifysn(reverseID);
        int insert = revisehistoryMapper.insert(revisehistory);
        if (insert != 1) {
            throw new ApiException("修改失败", 500);

        }
        return new ResultCode(0, "success", reverseID);
    }

}
