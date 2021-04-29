package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content 首字母必须小写
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("仓库信息表")
@TableName(value = "company_warehouse_info")
public class CompanyWarehouseInfo {

    private Long wareHousId;
    /**
     * 企业基本信息表主键
     */
    private Long companyid;

    /**
     * 仓库编码
     */
    @NotNull(message = " 仓库编码不能为空")
    private String Warehousesid;

    /**
     * 仓库名称
     */

    @NotNull(message = "仓库名称 ")
    private String warehouseName;
    /**
     * 仓库类别1危废、2一般固废
     */
    @NotNull(message = "仓库类别(1危废、2一般固废)不能为空")
    private Integer wareHouseType;

    /**
     * 所属类别（1产废、2处置）
     */
    @NotNull(message = " 所属类别（1产废、2处置）不能为空 ")
    private Integer belongtype;

    /**
     * 仓库经度
     */
    @NotNull(message = " 仓库经度不能为空")
    private BigDecimal Longitude;
//    Longitude
    /**
     * 仓库纬度
     */
    @NotNull(message = "仓库纬度不能为空 ")
    private BigDecimal Latitude;

    /**
     * 仓库容积
     */
    @NotNull(message = " 仓库容积不能为空")
    private Float Volume;

    /**
     * 仓库面积
     */
    @NotNull(message = "仓库面积不能为空")
    private Float Area;

    /**
     * 仓库容量
     */
    @NotNull(message = "仓库容量不能为空 ")
    private Float Weight;

    /**
     * 计算单位(对应容量)
     */
    @NotNull(message = "计算单位(对应容量) ")
    private String unit;

    /**
     * 联系人
     */
    @NotNull(message = "联系人 ")
    private String Contacts;

    /**
     * 联系电话
     */
    @NotNull(message = "联系电话 ")
    private String Contactsphone;

}
