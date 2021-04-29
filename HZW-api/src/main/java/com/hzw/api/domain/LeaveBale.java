package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content 处置企业出厂上报实体类
 */
@Data
@TableName(value = "company_cf_wasterecord")
public class LeaveBale {

    @NotNull(message = "联单号不能为空")
    private String transferOrderSn;
    @NotNull(message = "车辆号码不能为空")
    private String transferCarNumber;
    @TableField(exist = false)
    @NotNull(message = "出厂重量不能为空")
    private BigDecimal wight;
    @TableField(exist = false)
    public String[] tracePackages;
    /*这个时间插入联单中*/
    @TableField(exist = false)
    @NotNull(message = "出厂时间不能为空")
    private Date timestamp;

    private int RecordStatus;

    @NotNull(message = "地磅不能为空")
    @TableField(value = "outWeighbridge")
    private String loadoMeter;

    public String getTransferOrderSn() {
        return transferOrderSn;
    }

    public void setTransferOrderSn(String transferOrderSn) {
        this.transferOrderSn = transferOrderSn;
    }

    public String getTransferCarNumber() {
        return transferCarNumber;
    }

    public void setTransferCarNumber(String transferCarNumber) {
        this.transferCarNumber = transferCarNumber;
    }

    public BigDecimal getWight() {
        return wight;
    }

    public void setWight(BigDecimal wight) {
        this.wight = wight;
    }

    public String[] getTracePackages() {
        return tracePackages;
    }

    public void setTracePackages(String[] tracePackages) {
        this.tracePackages = tracePackages;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        this.timestamp = date;
    }
}
