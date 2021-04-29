package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.annotation.security.DenyAll;
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
@ApiModel("上传时大包信息")
@TableName(value = "company_cf_wasterecord")
public class BaleStatues implements Serializable {
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
    private Date timestamp;
    private String Sitesn;
}