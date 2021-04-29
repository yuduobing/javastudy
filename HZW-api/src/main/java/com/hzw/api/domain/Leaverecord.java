package com.hzw.api.domain;

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
@ApiModel("处置企业流水上报实体类")
@TableName(value = "company_cz_dealrecord")
public class Leaverecord {

    /**
     * 处置单位内部的包装编码
     */
    @NotNull(message = "处置单位内部的包装编码不能为空")
    private String innersn;

    /**
     * 废物代码
     */
    @NotNull(message = "废物代码不能为空")
    private String wasteCode;

    /**
     * 状态(1入库、2出库)
     */

    private Integer recordstatus;

    @NotNull(message = "处置时间不能为空")
    private Date dealTime;
    @NotNull(message = "处置入库重量不能为空")
    private BigDecimal dealWeight;

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        if (dealTime != "" && dealTime != null) {

            Date date = new Date(Long.parseLong(dealTime) * 1000);
            this.dealTime = date;
        }

    }
}

