package com.hzw.api.domain;

import com.alibaba.fastjson.annotation.JSONField;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@Data
@ApiModel("上传时大包信息")
@TableName(value = "company_cf_wasterecord")
public class Bale implements Serializable {
    private Long companyid;
    @NotNull(message = "溯源码不能为空")
    private String traceSn;
    @NotNull(message = "产废代码不能为空")
    private String wasteCode;
    //状态
    private int RecordStatus;
    @TableField(exist = false)
    @NotNull(message = "小包信息不能为空")
    private List<SmallBale> zipPackages;
    @NotNull(message = "参数签名不能为空")
    @TableField(exist = false)
    private String sign;
    @NotNull(message = "产出重量不能为空")
    @TableField(value = "produceWeight")
    private BigDecimal weight;
    @TableField(value = "produceTime")
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    private String Sitesn;
    @TableField(value = "produceWeighbridge")
    @NotNull(message = "地磅不能为空")
    private String loadoMeter;

    public Long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getSitesn() {
        return Sitesn;
    }

    public void setSitesn(String sitesn) {
        Sitesn = sitesn;
    }
    //    @NotNull(message = "地磅名称不能为空")
//    private String produceWeighbridge;

}
