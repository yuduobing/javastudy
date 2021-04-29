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
public class DleaveBAle {

    @NotNull(message = "联单号不能为空")
    private String transferOrderSn;
    @NotNull(message = "车辆号码不能为空")
    private String transferCarNumber;
    @TableField(exist = false)
    @NotNull(message = "入厂重量不能为空")
    private BigDecimal weight;
    @TableField(exist = false)
    public String[] tracePackages;
    /*这个时间插入联单中*/
    @TableField(exist = false)
    @NotNull(message = "入厂时间不能为空")
    private Date timestamp;

    private int RecordStatus;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        this.timestamp = date;
    }
}
