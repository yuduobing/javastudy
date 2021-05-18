package com.example.javastudy.实际应用.告警判断;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import com.example.javastudy.util.redis.RedisUtil;
import com.example.javastudy.util.utils.DateUtils;
import com.example.javastudy.util.utils.StringUtils;
import com.hbj.common.datasource.DynamicDataSourceContextHolder;
import com.hbj.common.enums.DataSourceType;
import com.hbj.common.utils.ip.IpUtils;
import com.hbj.us.domain.Qyfsbz;
import com.hbj.us.domain.ps.Output;
import com.hbj.us.domain.ps.RealTimeWW;
import com.hbj.us.domain.ps.WaterSample;
import com.hbj.us.domain.ps.WaterSampleOperRecord;
import com.hbj.us.mapper.LctCollectMapper;
import com.hbj.us.mapper.lct.WaterSampleMapper;
import com.hbj.us.service.lct.IRealTimeWWService;
import com.hbj.us.service.lct.IWaterSampleOperRecordService;
import com.hbj.us.service.lct.IWaterSampleService;
import com.hbj.us.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.hbj.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.hbj.us.constant.RedisConstant.*;

/**
 * 污染源废水实时数据定时检测
 */
@Component
@Slf4j
public class TimeCheckService {

    @Value("${spring.profiles.active}")
    private String profile;
    @Resource
    private LctCollectMapper collectMapper;

    @Autowired
    private IWaterSampleService sampleService;
    @Resource
    private WaterSampleMapper sampleMapper;

    @Autowired
    private IWaterSampleOperRecordService operRecordService;

    @Autowired
    private IRealTimeWWService realTimeWWService; //废水源污水实时数据

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AsynchronousService asynchronousService;

    private static Map<String, String> lctMap = new HashMap<>();
    public static boolean  messageDictsign=false;
    long nd = 1000 * 24 * 60 * 60;
    long nh = 1000 * 60 * 60;
    /**
     * 从redis获取企业排污标准
     *
     * @param siteID
     * @return
     */
    public Qyfsbz getOutputStandard(String siteID, Map<String, Object> basicData) {
        Object o = basicData.get(siteID);
        if (o == null) {
            return null;
        }
        Qyfsbz qyfsbz = JSONObject.parseObject(JSONObject.toJSONString(o), Qyfsbz.class);
        return qyfsbz;
    }

    /**
     * 从redis获取中间库企业数据
     *
     * @param siteID
     * @return
     */
    public Output getOutputData(String siteID, Map<String, Object> outputData) {
        Object o = outputData.get(siteID);
        if (o == null) {
            return null;
        }
        Output output = JSONObject.parseObject(JSONObject.toJSONString(o), Output.class);
        return output;
    }
    /*
    * 检查报警是否超时 连续超标4小时报警一次，或者超过50%报警告一次
    * */

    public void checkHourData(String time) {
        Map<String, Object> basicData = redisUtil.hmget(qyfsbz);
        Map<String, Object> outputData = redisUtil.hmget(outputKey);

        // 查询指定时间之后的污水实时数据
        Date today = new Date();
        String timeNow = DateUtils.getTime();
        String time10Ago = DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, DateUtils.addMinutes(today, -10));
        String time5Later = DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, DateUtils.addMinutes(today, 5));
        RealTimeWW vo = new RealTimeWW();
        vo.setBeginTime(time10Ago);
        vo.setEndTime(time5Later);
        List<RealTimeWW> timeData = realTimeWWService.selectTimeList(vo);

        String hour1Ago = DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, DateUtils.addHours(today, -2));
        Set<String> selectSamples = realTimeWWService.selectSample(hour1Ago);

        List<WaterSample> sampleList = new ArrayList<>();//我方记录采样记录
        List<WaterSampleOperRecord> operRecordList = new ArrayList<>();//采样记录操作

        //连续超标次数才会触发采样 默认3次
        int configCount = Convert.toInt(redisUtil.hget(lctConfigKey, "lctCount"), 3);
        String totalNum = Convert.toStr(redisUtil.hget(lctConfigKey, "totalNum"), "500");

        //redis获取上一次采样企业map
        Map<String, Object> lastSampleMap = redisUtil.hmget(LAST_SAMPLE_QY);
        //redis获取上一次超标企业map
        Map<String, Object> lastExCompanyMap = redisUtil.hmget(exCompanyKey);

        if (timeData.size() > 0) {

//            RealTimeWW实时数据
            for (RealTimeWW realTimeWW : timeData) {
                String siteID = realTimeWW.getSiteID();//点位编号
                // 从企业排污标准中查询数据
                Qyfsbz qyfsbz = getOutputStandard(siteID, basicData);
                if (qyfsbz == null) {
                    continue;
                }
                // 检查排污数据
                String reason = checkData(realTimeWW, qyfsbz);


                if (StringUtils.isNotEmpty(reason)) {//有超标
                    //判断企业上一次是否超标,连续超标3次才算超标
                    if (lastExCompanyMap.containsKey(siteID)) {
                        //上次有超标,判断超标次数
                        String timelast = Convert.toStr(lastExCompanyMap.get(siteID));
                        long diff = System.currentTimeMillis() - new Date(timelast).getTime();
                        long  hour = diff / (60 * 60 * 1000) ;
                        if (hour >= 4  ||  messageDictsign)  {//连续超标次数大于配置次数
//                            50%超标恢复默认值
                            messageDictsign=false;
//                             待定
                            //判断企业上次是否采样，一直超标不触发采样,但更新上次采样的时间
                            if (!lastSampleMap.containsKey(siteID)) {
                                //如果上次连续超标，没有触发采样，进行自动采样
                                //排除采样数量太多，指定小时内只采样一次  待定是否还要
                                boolean contains = selectSamples.contains(siteID);
                                if (contains) continue;
                                /**/
                                String sampleTime = realTimeWW.getDataTime();
                                long dayIncr = redisUtil.dayIncr(SampleID, 1);
                                //当日采样次数累计
                                long sampleId = Convert.toLong(IDUtil.getSampleID(dayIncr));
                                WaterSample sample = new WaterSample();
                                String firstEx = getFirstEx(reason);
                                sample.setExFirst(firstEx);
                                sample.setSampleID(sampleId);
                                sample.setSiteID(siteID);
                                sample.setSampleTime(sampleTime);
                                sample.setSampleReason(reason);
                                sample.setSampleType(0);
                                Output output = getOutputData(siteID, outputData);//获取采样命令中采样企业的数据
                                int sampleBottleNum = getSampleBottleNum(siteID);//获取采样瓶号：根据redis里的记录值来定
                                String sampleRst = sendSample(output, sampleBottleNum, totalNum);//发送采样命令
                                int status = 1;
                                if (StringUtils.isNotEmpty(sampleRst)) {//采样失败
                                    status = 5;
                                    sample.setBottleNo(Convert.toStr(sampleBottleNum));
                                    sample.setRemark(sampleRst);
                                } else {
                                    sample.setBottleNo(Convert.toStr(sampleBottleNum));
                                    setSampleBottleNum(siteID, sampleBottleNum);
                                }
                                sample.setSampleStatus(status);
                                sampleList.add(sample);

                                //新的我方采样操作记录
                                WaterSampleOperRecord operRecord = new WaterSampleOperRecord();
                                operRecord.setSampleID(sampleId);
                                operRecord.setUserid(1L);
                                operRecord.setOperType(0);
                                operRecord.setOperTime(DateUtils.getTime());
                                operRecord.setOperDesc("系统自动采样");
                                operRecordList.add(operRecord);
                            }
                            //redis记录本次超标
                            lastSampleMap.put(siteID, timeNow);
                        }
                    } else {
                        //上次没有超标，记录时间
                        lastExCompanyMap.put(siteID, timeNow);
                    }
                } else {
                    //没有超标
                    //redis清除上次采样
                    lastSampleMap.remove(siteID);
                    //redis清除上次超标
                    lastExCompanyMap.remove(siteID);
                }
            }
            //排除企业数据不更新时，之前的超标记录和采样记录一直存在
            Set<String> lastSampleSiteIds = lastSampleMap.keySet();
            for (String lastSampleSiteId : lastSampleSiteIds) {
                String sampleTime = Convert.toStr(lastSampleMap.get(lastSampleSiteId));
                long offsetHour = DateUtils.getOffsetHour(sampleTime, timeNow);
                if (offsetHour >= 1) {
                    lastSampleMap.remove(lastSampleSiteId);
                    lastExCompanyMap.remove(lastSampleSiteId);
                }
            }
            redisUtil.del(LAST_SAMPLE_QY);
            //redis记录超标
            redisUtil.hmset(LAST_SAMPLE_QY, lastSampleMap);
            redisUtil.del(exCompanyKey);
            redisUtil.hmset(exCompanyKey, lastExCompanyMap);
        }
        if (sampleList.size() > 0) {
            DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.TimeData.name());
            sampleService.saveBatch(sampleList);
        }
        if (operRecordList.size() > 0) {
            DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.TimeData.name());
            operRecordService.saveBatch(operRecordList);
        }
    }
//提示消息
    private String getFirstEx(String reason){
        if (reason.contains("COD超标")){
            return "codArr";
        }
        if (reason.contains("氨氮超标")){
            return "nh3Arr";
        }
        if (reason.contains("总氮超标")){
            return "tnArr";
        }
        if (reason.contains("总磷超标")){
            return "tpArr";
        }
        if (reason.contains("PH超标")){
            return "phArr";
        }
        if (reason.contains("总镍超标")){
            return "tniArr";
        }
        if (reason.contains("总铜超标")){
            return "tcuArr";
        }
        if (reason.contains("六价铬超标")){
            return "cr6Arr";
        }
        if (reason.contains("总铅超标")){
            return "tPbArr";
        }
        return "phArr";
    }

    private int getSampleBottleNum(String siteCode) {
        if ("local".equals(profile) || "test".equals(profile)) {
            return 0;
        }
        int bottleNum = Convert.toInt(redisUtil.hget(lctCompanyBottle, siteCode), 0);//自动采样默认从15号瓶开始
        if (bottleNum >= 15 && bottleNum < 23) bottleNum++;
        else bottleNum = 15;
        redisUtil.hset(lctCompanyBottle, siteCode, bottleNum);
        return bottleNum;
    }

    private void setSampleBottleNum(String siteCode, int bottleNum) {
        redisUtil.hset(lctCompanyBottle, siteCode, bottleNum);
        redisUtil.sSet(companyBottleHasLct + siteCode, bottleNum);
    }

    /**
     * 检查采样瓶是否有水样，平台上是否确认已采样
     * @param sampleBottleNum
     * @param output
     * @return
     */
    private boolean checkSampleBottleNum(int sampleBottleNum, Output output){
        String siteID = output.getSiteID();
        Set<Object> objects = redisUtil.sGet(companyBottleHasLct + siteID);
        if (objects.contains(sampleBottleNum)){
            return false;
        }
        return true;
    }

    public String sendSample(Output output, int sampleBottleNum, String totalNum) {
        if (output == null) {
            return "企业没有采样设备";
        }
        String ip = output.getIP();
        String memo = output.getMemo();
        if (StringUtils.isNotEmpty(memo)){
            return memo;
        }
        if (!IpUtils.ipCheck(ip)) {
            return "采样企业IP地址异常";
        }
        if ("local".equals(profile) || "test".equals(profile)) {
            return "测试环境，不真实采样";
        }
        if (!checkSampleBottleNum(sampleBottleNum, output)) {
            return "采样瓶水样未清，采样失败";
        }
        Future future = asynchronousService.springAsynchronousMethod(ip, output.getStationID(), totalNum, output.getIsGuest(), sampleBottleNum, output.getHasUpgrade());
        while (true) {
            if (future.isDone()) {
                try {
                    String result = Convert.toStr(future.get());
                    if (result.contains("成功") || result.contains("success")){
                        return "";
                    }else {
                        return result;
                    }
                } catch (InterruptedException e) {
                    log.error(e.toString());
                    return e.getMessage();
                } catch (ExecutionException e) {
                    log.error(e.toString());
                    return e.getMessage();
                }
            }
        }
    }

    //报警数据逻辑判断是否超标
    private String checkData(RealTimeWW timeWW, Qyfsbz qyfsbz) {
        //流量最低值
        Double instFlowConfig = Convert.toDouble(redisUtil.hget(lctConfigKey, "instFlow"), 1.);

        Double instFlow = Convert.toDouble(timeWW.getInstFlow(), 0.);
        if (instFlow <= instFlowConfig) {//瞬时流量为0时不做检查
            return "";
        }

        BigDecimal cod = null;//化学需氧量 mg/L
        if (StringUtils.isNotEmpty(timeWW.getCOD())) {
            cod = new BigDecimal(timeWW.getCOD());
            if (cod.doubleValue() <= 0) {
                cod = null;
            }
        }
        BigDecimal bzCOD = qyfsbz.getCOD();

        BigDecimal nh3 = null;//氨氮浓度 mg/L
        if (StringUtils.isNotEmpty(timeWW.getNH3_N())) {
            nh3 = new BigDecimal(timeWW.getNH3_N());
            if (nh3.doubleValue() <= 0) {
                nh3 = null;
            }
        }
        BigDecimal bzNh3 = qyfsbz.getNH();

        BigDecimal zd = null;//总氮浓度 mg/L
        if (StringUtils.isNotEmpty(timeWW.getTN())) {
            zd = new BigDecimal(timeWW.getTN());
        }
        BigDecimal bzZn = qyfsbz.getZN();

        BigDecimal zl = null;//总磷浓度 mg/L
        if (StringUtils.isNotEmpty(timeWW.getTP())) {
            zl = new BigDecimal(timeWW.getTP());
            if (zl.doubleValue() <= 0) {
                zl = null;
            }
        }
        BigDecimal bzZl = qyfsbz.getZP();

        BigDecimal ph = null;//PH值
        if (StringUtils.isNotEmpty(timeWW.getPH())) {
            ph = new BigDecimal(timeWW.getPH());
            if (ph.doubleValue() <= 0) {
                ph = null;
            }
        }
        String bzPH = qyfsbz.getPH();

        String metal = qyfsbz.getMETAL();//获取重金属
        BigDecimal zm = null;//总镍浓度 mg/L
        if (StringUtils.isNotEmpty(timeWW.getTNi())) {
            zm = new BigDecimal(timeWW.getTNi());
            if (zm.doubleValue() <= 0) {
                zm = null;
            }
        }
        BigDecimal ljg = null;//六价铬浓度 mg/L
        if (StringUtils.isNotEmpty(timeWW.getCr6())) {
            ljg = new BigDecimal(timeWW.getCr6());
            if (ljg.doubleValue() <= 0) {
                ljg = null;
            }
        }
        BigDecimal zt = null;//总铜浓度 mg/L
        if (StringUtils.isNotEmpty(timeWW.getTCu())) {
            zt = new BigDecimal(timeWW.getTCu());
            if (zt.doubleValue() <= 0) {
                zt = null;
            }
        }
        BigDecimal zPb = null;//总铅浓度 mg/L
        if (StringUtils.isNotEmpty(timeWW.getTPb())) {
            zPb = new BigDecimal(timeWW.getTPb());
            if (zPb.doubleValue() <= 0) {
                zPb = null;
            }
        }

        StringBuilder reasonBuilder = new StringBuilder();

        boolean isEx = false;
        reasonBuilder.append("当前流量为 " + instFlow + "m3/h;\n");

        //超标百分比配置，默认10%
        double exRate = Convert.toDouble(redisUtil.hget(lctConfigKey, "exRate"), 25.);


        exRate = 1+exRate/100;

        if (bzCOD!=null && bzCOD.doubleValue()>0){
            BigDecimal cod200 = Convert.toBigDecimal(redisUtil.hget(lctConfigKey, "cod200"), new BigDecimal(250));
            BigDecimal cod500 = Convert.toBigDecimal(redisUtil.hget(lctConfigKey, "cod500"), new BigDecimal(575));
            if (200 == bzCOD.intValue()) bzCOD  = cod200;
            else if (500 == bzCOD.intValue()) bzCOD = cod500;
            else bzCOD = bzCOD.multiply(new BigDecimal(exRate));
            bzCOD = bzCOD.setScale(0, RoundingMode.HALF_UP);
        }

        if (bzCOD != null && bzCOD.doubleValue() > 0 && cod != null && exCompareNoRate(cod, bzCOD)) {//COD超标
            //记录原因
            reasonBuilder.append("COD超标：标准值<=" + bzCOD + "mg/L 当前值" + cod + "mg/L;\n");
            isEx = true;
        }
        if (bzNh3 != null && bzNh3.doubleValue() > 0 && nh3 != null && exCompare(nh3, bzNh3)) {//氨氮超标
            bzNh3 = bzNh3.multiply(new BigDecimal(exRate));
            bzNh3 = bzNh3.setScale(0, RoundingMode.HALF_UP);
            reasonBuilder.append("氨氮超标：标准值<=" + bzNh3 + "mg/L 当前值" + nh3 + "mg/L;\n");
            isEx = true;
        }
        if (bzZn != null && bzZn.doubleValue() > 0 && zd != null && exCompare(zd, bzZn)) {//总氮超标
            bzZn = bzZn.multiply(new BigDecimal(exRate));
            bzZn = bzZn.setScale(0, RoundingMode.HALF_UP);
            reasonBuilder.append("总氮超标：标准值<=" + bzZn + "mg/L 当前值" + zd + "mg/L;\n");
            isEx = true;
        }
        if (bzZl != null && bzZl.doubleValue() > 0 && zl != null && exCompare(zl, bzZl)) {//总磷超标
            bzZl = bzZl.multiply(new BigDecimal(exRate));
            bzZl = bzZl.setScale(0, RoundingMode.HALF_UP);
            reasonBuilder.append("总磷超标：标准值<=" + bzZl + "mg/L 当前值" + zl + "mg/L;\n");
            isEx = true;
        }
        if (ph!=null && ph.doubleValue()<=14){
            if (bzPH != null) {//PH超标
                String[] phArr = bzPH.split("--");
                if (phArr.length == 2) {
                    BigDecimal phBegin = Convert.toBigDecimal(redisUtil.hget(lctConfigKey, "phBegin"), new BigDecimal(5.5));
                    BigDecimal phEnd = Convert.toBigDecimal(redisUtil.hget(lctConfigKey, "phEnd"), new BigDecimal(9.5));
                    if (phBegin.compareTo(ph)>=0 || ph.compareTo(phEnd)>=0) {
                        reasonBuilder.append("PH超标：标准范围" + phBegin + "~" + phEnd + " 当前值" + ph + ";\n");
                        isEx = true;
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(metal)) {//重金属超标
            JSONObject jsonObject = JSONObject.parseObject(metal);
            BigDecimal bzZM = jsonObject.getBigDecimal("ZM");
            BigDecimal bzZT = jsonObject.getBigDecimal("ZT");
            BigDecimal bzLjg = jsonObject.getBigDecimal("LJG");
            BigDecimal bzPB = jsonObject.getBigDecimal("PB");

            if (bzZM != null && bzZM.doubleValue() > 0 && zm != null && exCompare(zm, bzZM)) {
                //总镍超标
                bzZM = bzZM.multiply(new BigDecimal(exRate));
                bzZM = bzZM.setScale(0, RoundingMode.HALF_UP);
                reasonBuilder.append("总镍超标：标准值<=" + bzZM + "mg/L 当前值" + zm + "mg/L;\n");
                isEx = true;
            }
            if (bzZT != null && bzZT.doubleValue() > 0 && zt != null && exCompare(zt, bzZT)) {
                //总铜超标
                bzZT = bzZT.multiply(new BigDecimal(exRate));
                bzZT = bzZT.setScale(0, RoundingMode.HALF_UP);
                reasonBuilder.append("总铜超标：标准值<=" + bzZT + "mg/L 当前值" + zt + "mg/L;\n");
                isEx = true;
            }
            if (bzLjg != null && bzLjg.doubleValue() > 0 && ljg != null && exCompare(ljg, bzLjg)) {//六价铬超标
                bzLjg = bzLjg.multiply(new BigDecimal(exRate));
                bzLjg = bzLjg.setScale(0, RoundingMode.HALF_UP);
                reasonBuilder.append("六价铬超标：标准值<=" + bzLjg + "mg/L 当前值" + ljg + "mg/L;\n");
                isEx = true;
            }
            if (bzPB != null && bzPB.doubleValue() > 0 && zPb != null && exCompare(zPb, bzPB)) {//总铅超标
                bzPB = bzPB.multiply(new BigDecimal(exRate));
                bzPB = bzPB.setScale(0, RoundingMode.HALF_UP);
                reasonBuilder.append("总铅超标：标准值<=" + bzPB + "mg/L 当前值" + zPb + "mg/L;\n");
                isEx = true;
            }
        }
        if (isEx) {


            return reasonBuilder.toString();
        } else {
            return "";
        }


    }

    private boolean exCompareNoRate(BigDecimal realValue, BigDecimal bzValue) {
        if (realValue.compareTo(bzValue) <= 0) {
            return false;
        }
        return true;
    }
//比较超标多少
    private boolean exCompare(BigDecimal realValue, BigDecimal bzValue) {
        if (realValue.compareTo(bzValue) <= 0) {
            return false;
        }
        double bigRate = (realValue.doubleValue() - bzValue.doubleValue()) / bzValue.doubleValue() * 100;
        if (bigRate>new Double(50.))
        {
            messageDictsign=true;
        }
        //超标百分比配置，默认10%
        Double exRate = Convert.toDouble(redisUtil.hget(lctConfigKey, "exRate"), 25.);
        if (bigRate < exRate) {
            return false;
        }
        return true;
    }
}

