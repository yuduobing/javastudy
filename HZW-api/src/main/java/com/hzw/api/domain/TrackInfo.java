package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content 联单实体类 时间由其他地方获取
 */
@Data
@TableName(value = "company_track_info")
public class TrackInfo {

    /**
     * 产废企业主键
     */
    private Integer prodcompanyid;

    /**
     * 发运人
     */
    private String prodpersonname;

    /**
     * 发运人电话
     */
    private String prodpersontel;

    /**
     * 联单号
     */
    private String tracknumber;

    /**
     * 危废代码
     */
    private String wastecode;

    /**
     * 危废名称（国标上只有大类，这个名称是企业内部自己的名称）
     */
    private String wastename;

    /**
     * 申请时间
     */
    private Date applytime;

    /**
     * 转移时间
     */
    private Date transfertime;

    /**
     * 出库重量
     */
    private BigDecimal outweight;

    /**
     * 转移重量
     */
    private BigDecimal transferweight;

    /**
     * 处置接收重量
     */
    private BigDecimal receiveweight;

    /**
     * 转移方式（1、区内；2、区外）
     */
    private Integer transferway;

    /**
     * 状态(1、运输中2、完成）
     */
    private Integer state;

    /**
     * 状态名称
     */
    private String statename;

    /**
     * 处置企业主键
     */
    private Long dealcompanyid;

    /**
     * 处置企业名称
     */
    private String dealcompany;

    /**
     * 处置协议附件id
     */
    private Long dealprotocolid;

    /**
     * 处置协议附件
     */
    private String dealprotocol;

    /**
     * 处置暂存仓库id
     */
    private Long dealtempwarehouseid;

    /**
     * 处置暂存仓库名称
     */
    private String dealtempwarehouse;

    /**
     * 处置企业接收人姓名
     */
    private String dealpersonname;

    /**
     * 处置企业接收人电话
     */
    private String dealpersontel;

    /**
     * 处置接收时间
     */
    private Date dealreceivetime;

    /**
     * 运输企业
     */
    private String transfercompanyname;

    /**
     * 车牌号
     */
    private String carnum;

    /**
     * 运输人员姓名
     */
    private String transfername;

    /**
     * 运输人员电话
     */
    private String transfertel;

    /**
     * 运输协议附件id
     */
    private Long transferprotocolid;

    /**
     * 运输协议附件
     */
    private String transferprotocol;

    /**
     * 预计处置时间
     */
    private String expecteddealtime;

    /**
     * 实际处置时间
     */
    private String infactdealtime;

    /**
     * 发票附件id
     */
    private Long billid;

    /**
     * 发票附件
     */
    private String bill;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updatetime;

}