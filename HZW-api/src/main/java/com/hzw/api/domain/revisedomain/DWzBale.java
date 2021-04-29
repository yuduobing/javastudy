package com.hzw.api.domain.revisedomain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@TableName(value = "company_cz_dealrecord")
@Data
public class DWzBale {

    /**
     * 主键
     */
    private Long dealrecordid;

    /**
     * 企业基本信息表主键
     */
    private Long companyid;

    /**
     * 联单号码
     */
    private String tracknumber;

    /**
     * 产废单位溯源码
     */
    private String tracesn;

    /**
     * 处置单位内部的包装编码
     */
    private String innersn;

    /**
     * 废物代码
     */
    private String wastecode;

    /**
     * 处置入库时间
     */
    private Date dealintime;

    /**
     * 处置时间
     */
    private Date dealtime;

    /**
     * 入场照片附件id
     */
    private Long inpictureid;

    /**
     * 来源状态区内1 ，区外0
     */
    private Integer sourcesta;

    /**
     * 暂存库唯一编码
     */
    private String storehousesn;
    private String recordstatus;

    /**
     * 更新者
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
