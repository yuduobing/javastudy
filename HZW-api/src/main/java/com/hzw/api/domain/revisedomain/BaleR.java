package com.hzw.api.domain.revisedomain;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hzw.api.domain.revisedomain.SmallBaleR;
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
@ApiModel("上传时大包信息修改")
@TableName(value = "company_cf_wasterecord")
public class BaleR implements Serializable {
    @NotNull(message = "溯源码不能为空")
    private String traceSn;

    private String wasteCode;
    //状态 记录修改哪部分
    private Integer recordStatus;
    @TableField(exist = false)
    private List<SmallBaleR> zipPackages;

    //暂存库上报
    private String storehouseSn;
    //暂存库
    private String transferOrderSn;
    //修改原因
    @NotNull(message = "修改原因不能为空")
    @TableField(exist = false)
    private String reason;
    @TableField(value = "produceWeighbridge")
    private String loadoMeter;
}
