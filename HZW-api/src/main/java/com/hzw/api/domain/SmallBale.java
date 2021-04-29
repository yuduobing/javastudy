package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */

@ApiModel("小包信息")
@TableName(value = "company_cf_smallrecord")
public class SmallBale implements Serializable {

    //    内部编码
    @NotNull(message = "内部编码不能为空")
    private String innerSn;
    //    溯源码
    private String traceSn;
    //    危废代码
    @NotNull(message = "危废代码不能为空")
    private String wasteCode;
    //    产废点名称
    @NotNull(message = "产废点名称")
    private String siteSn;
    //    时间戳
    @NotNull(message = "时间戳名称不能为空")
    private Date timestamp;
    @NotNull(message = "重量不能为空")
    private BigDecimal weight;

    public String getInnerSn() {
        return innerSn;
    }

    public void setInnerSn(String innerSn) {
        this.innerSn = innerSn;
    }

    public String getTraceSn() {
        return traceSn;
    }

    public void setTraceSn(String traceSn) {
        this.traceSn = traceSn;
    }

    public String getWasteCode() {
        return wasteCode;
    }

    public void setWasteCode(String wasteCode) {
        this.wasteCode = wasteCode;
    }

    public String getSiteSn() {
        return siteSn;
    }

    public void setSiteSn(String siteSn) {
        this.siteSn = siteSn;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        this.timestamp = date;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
