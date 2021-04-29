package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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

@ApiModel("出库实体类")
@TableName(value = "company_cf_wasterecord")
public class OutBale implements Serializable {
    @NotNull(message = "溯源码不能为空")
    private String traceSn;
    //状态
    private int RecordStatus;

    @NotNull(message = "暂存库编号不能为空")
    private String storehouseSn;

    private String handleEnterpriseId;
    @TableField(exist = false)
    @NotNull(message = "小包信息不能为空")
    private List<SmallBale> zipPackages;
    @NotNull(message = "出库重量不能为空")
    @TableField(value = "outWeight")
    private BigDecimal weight;
    @NotNull(message = "出库时间不能为空")
    @TableField(value = "outTime")
    private Date timestamp;

    public String getTraceSn() {
        return traceSn;
    }

    public void setTraceSn(String traceSn) {
        this.traceSn = traceSn;
    }

    public int getRecordStatus() {
        return RecordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        RecordStatus = recordStatus;
    }


    public String getStorehouseSn() {
        return storehouseSn;
    }

    public void setStorehouseSn(String storehouseSn) {
        this.storehouseSn = storehouseSn;
    }


    public String getHandleEnterpriseId() {
        return handleEnterpriseId;
    }

    public void setHandleEnterpriseId(String handleEnterpriseId) {
        this.handleEnterpriseId = handleEnterpriseId;
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
