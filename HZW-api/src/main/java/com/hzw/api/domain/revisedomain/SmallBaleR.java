package com.hzw.api.domain.revisedomain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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
@Data
public class SmallBaleR implements Serializable {
    @NotNull(message = "内部编码不能为空")
    private String innerSn;

    private String traceSn;
    //    危废代码
    private String wasteCode;
    //    产废点名称
    private String siteSn;

}
