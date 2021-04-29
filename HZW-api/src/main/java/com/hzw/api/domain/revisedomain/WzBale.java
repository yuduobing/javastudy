package com.hzw.api.domain.revisedomain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content 修改中 的完整流水包
 */
@TableName(value = "company_cf_wasterecord")
@Data
public class WzBale {
    /**
     * 主键
     */
    private Long wasterecordid;

    /**
     * 企业主键
     */
    private Long companyid;

    /**
     * 溯源码
     */
    private String tracesn;

    /**
     * 企业统一征信代码
     */
    private String creditcode;

    /**
     * 废物代码(从废料信息表带入)
     */
    private String wastecode;

    /**
     * 产生时间
     */
    private Date producetime;
    private int recordstatus;

    /**
     * 产出重量（kg）
     */
    private Float produceweight;

    /**
     * 产出车间名字
     */
    private String sitesn;

    /**
     * 产出地磅
     */
    private String produceweighbridge;

    /**
     * 贮存仓库编号
     */
    private String storehousesn;

    /**
     * 入库重量（kg）
     */
    private Float putinweight;

    /**
     * 入库时间
     */
    private Date putintime;

    /**
     * 入库地磅
     */
    private String putinweighbridge;

    /**
     * 出库重量（kg）
     */
    private Float outweight;

    /**
     * 出库时间
     */
    private Date outtime;

    /**
     * 出厂地磅
     */
    private String outweighbridge;

    /**
     * 是否自行处理
     */
    private Integer ifdealbyself;

    /**
     * 五联单号
     */
    private String transferordersn;

    /**
     * 车牌号
     */
    private String transfercarnumber;

    /**
     * 处置单位征信代码
     */
    private String handleenterpriseid;

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