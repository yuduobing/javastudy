package com.hzw.api.domain.revisedomain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@Data
@TableName(value = "company_cf_wasterecord")
public class InStoreBale {
    @NotNull(message = "溯源码不能为空")
    private String traceSn;

    //状态 记录修改哪部分
    private Integer recordStatus;
    @TableField(exist = false)
    private List<SmallBaleR> zipPackages;
    //小包信息补全的
    private String Sitesn;

    //暂存库上报

    private String storehouseSn;
    //暂存库
    private String transferOrderSn;

    private String transferCarNumber;

    private String handleEnterpriseId;
    //修改原因
    @NotNull(message = "修改原因不能为空")
    @TableField(exist = false)
    private String reason;

    @TableField(exist = false)
    private String modifyOrderSn;

    //    地磅
    @TableField(value = "putInWeighbridge")
    private String loadoMeter;
}

