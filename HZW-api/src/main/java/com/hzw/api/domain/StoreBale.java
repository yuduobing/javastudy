package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@Data
@ApiModel("暂存库入库上报")
@TableName(value = "company_cf_wasterecord")
public class StoreBale implements Serializable {
    @NotNull(message = "溯源码不能为空")
    private String traceSn;

    @NotNull(message = "暂存库编号不能为空")
    private String storehouseSn;
    //状态
    private int RecordStatus;
    @TableField(exist = false)
    @NotNull(message = "小包信息不能为空")
    private List<SmallBale> zipPackages;

    @NotNull(message = "入库上报重量不能为空")
    @TableField(value = "putInWeight")
    private BigDecimal weight;
    @NotNull(message = "入库时间不能为空")
    @TableField(value = "putInTime")
    private Date timestamp;
    @TableField(value = "putInWeighbridge")
    private String loadoMeter;

    public String getTraceSn() {
        return traceSn;
    }

    public void setTraceSn(String traceSn) {
        this.traceSn = traceSn;
    }

    public String getStorehouseSn() {
        return storehouseSn;
    }

    public void setStorehouseSn(String storehouseSn) {
        this.storehouseSn = storehouseSn;
    }

    public int getRecordStatus() {
        return RecordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        RecordStatus = recordStatus;
    }

    public List<SmallBale> getZipPackages() {
        return zipPackages;
    }

    public void setZipPackages(List<SmallBale> zipPackages) {
        this.zipPackages = zipPackages;
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
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        this.timestamp = date;
    }
}
