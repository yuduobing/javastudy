package com.example.javastudy.实际应用.告警判断;

public class RedisConstant {

    // 企业排污标准数据
    public static final String qyfsbz = "hbj:lct:qyfsbz";
    public static final String outputKey = "hbj:lct:output";

    public static final String exCompanyKey = "hbj:lct:lastExCompanyMap";
    public static final String LAST_SAMPLE_QY = "hbj:lct:lastSampleMap";
    public static final String lctConfigKey = "hbj:lct:config";
    public static final String lctCompanyBottle = "hbj:lct:companyBottle";
    public static final String companyBottleHasLct = "hbj:lct:companyBottleHasLct:";

    public static final String wryfs = "hbj:lct:wryfs";

    public static final String SampleID = "hbj:lct:watersample:id";

    public static final String LctSpecialReason = "hbj:lct:specialReason";

    //大气监测站通知人员
    public static final String Station_Gas_NotifysMap = "hbj:hj212:station:notify:gasMap";
    public static final String Station_Water_NotifysMap = "hbj:hj212:station:notify:waterMap";

    public static final String Station_Config = "hbj:hj212:station:config";

    //在线监测废水环保码配置
    public static final String HBM_Config_ZXJCS = "hbj:hbm:config:zxjcs";
}
