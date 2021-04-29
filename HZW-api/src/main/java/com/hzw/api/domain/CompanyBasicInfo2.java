package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content 企业基本信息表主表
 */
@Data
@AllArgsConstructor
@TableName(value = "company_basic_info")
public class CompanyBasicInfo2 {
    /**
     * 主键
     */
    private Long companyid;

    /**
     * 企业名称
     */
    private String companyname;

    /**
     * 统一征信代码
     */
    private String creditcode;

    /**
     * 接口调用秘钥
     */
    private String secretid;

    /**
     * 企业类型（1产废单位、2处置单位）
     */
    private Integer companytype;

    /**
     * 企业等级，产废能力(1、1类企业；2、2类企业；3、3类企业；4、4类企业)
     */
    private Integer companylevel;

    /**
     * 企业规模，人数
     */
    private String companyscale;

    /**
     * 企业状态(1存续、2在业、3吊销4、注销、5迁入、6迁出、7停业、8清算)
     */
    private Integer companystatus;

    /**
     * 企业注册地址
     */
    private String address;

    /**
     * 生产设施地址
     */
    private String productionaddress;

    /**
     * 行政区划
     */
    private String administrativearea;

    /**
     * 所属环保部门
     */
    private String belongto;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 所属行业代码
     */
    private String industrialcode;

    /**
     * 法人代表
     */
    private String legalperson;

    /**
     * 法人联系电话
     */
    private String legalphone;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactsphone;

    /**
     * 其他联系人
     */
    private String contactsanother;

    /**
     * 其他联系人的联系电话
     */
    private String contactsphoneanother;

    /**
     * 传真号码
     */
    private String fax;

    /**
     * 邮政编码
     */
    private String mail;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 成立时间
     */
    private Date foundingtime;

    /**
     * 坐标经度
     */
    private BigDecimal longitude;

    /**
     * 坐标维度
     */
    private BigDecimal latitude;

    /**
     * 是否危废经营
     */
    private Integer ifwastemanagement;

    /**
     * 是否豁免危废经营
     */
    private Integer ifwastemanagementfree;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updatetime;

}