package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@Data
@Accessors(chain = true)
@ApiModel("处置企业流水上报实体类")
@TableName(value = "company_cz_dealrecord")
public class Dealrecord {

    /**
     * 企业基本信息表主键
     */
    private Long companyid;

    /**
     * 联单号码
     */
    @NotNull(message = "联单号码不能为空")

    @TableField(value = "tracknumber")
    private String transferOrderSn;
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

    private String wasteCode;

    /**
     * 状态(1入库、2出库)
     */

    private Integer recordstatus;

    @NotNull(message = "处置入库重量不能为空")
    @TableField(value = "dealinweight")
    private BigDecimal weight;

    /**
     * 处置入库时间
     */
    @NotNull(message = "处置入库时间不能为空")
    @TableField(value = "dealinTime")
    private Date timestamp;

    /**
     * 处置时间
     */
    private Date dealtime;

    /**
     * 入场照片附件id
     */
    private Long inpictureid;

    /**
     * 来源状态来源（区内1区外2）
     */

    private Integer sourcesta;

    /**
     * 暂存库
     */

    private String storeHousesn;

    public Long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    public String getTransferOrderSn() {
        return transferOrderSn;
    }

    public void setTransferOrderSn(String transferOrderSn) {
        this.transferOrderSn = transferOrderSn;
    }

    public String getTracesn() {
        return tracesn;
    }

    public void setTracesn(String tracesn) {
        this.tracesn = tracesn;
    }

    public String getInnersn() {
        return innersn;
    }

    public void setInnersn(String innersn) {
        this.innersn = innersn;
    }

    public Integer getRecordstatus() {
        return recordstatus;
    }

    public void setRecordstatus(Integer recordstatus) {
        this.recordstatus = recordstatus;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        if (timestamp != "" || timestamp != null) {

            Date date = new Date(Long.parseLong(timestamp) * 1000);
            this.timestamp = date;
        }
    }

    public Date getDealtime() {
        return dealtime;
    }

    public void setDealtime(Date dealtime) {
        this.dealtime = dealtime;
    }

    public Long getInpictureid() {
        return inpictureid;
    }

    public void setInpictureid(Long inpictureid) {
        this.inpictureid = inpictureid;
    }

    public Integer getSourcesta() {
        return sourcesta;
    }

    public void setSourcesta(Integer sourcesta) {
        this.sourcesta = sourcesta;
    }

    public String getStoreHousesn() {
        return storeHousesn;
    }

    public void setStoreHousesn(String storeHousesn) {
        this.storeHousesn = storeHousesn;
    }

}
