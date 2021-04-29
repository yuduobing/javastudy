package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content 合同信息上传
 */
@Data
@TableName(value = "company_contract_info")
public class ContactInfo {

    private Long companyid;

    /**
     * 处置企业id
     */
    private Long dcompanyid;

    /**
     * 处置企业名
     */
    @NotNull(message = "处置企业名不能为空")
    private String dealName;

    /**
     * 废物名称
     */
    @NotNull(message = "废物名称不能为空")
    private String wasteName;

    /**
     * 废物代码
     */
    @NotNull(message = "废物代码不能为空")
    private String wasteCode;

    /**
     * 处置方式
     */
    @NotNull(message = "处置方式不能为空")
    private String disposalWay;

    /**
     * 包装方式
     */
    private String packageway;

    /**
     * 合同量
     */
    @NotNull(message = "合同量不能为空")
    private Float contractAmount;

    /**
     * 合同开始时间
     */
    @NotNull(message = "合同开始时间不能为空")

    private Date contractStartTime;

    /**
     * 合同结束时间
     */
    @NotNull(message = "合同结束时间不能为空")
    private Date contractEndTime;

    /**
     * 项目编号
     */
    private String projectcode;

    /**
     * 项目名称
     */
    private String projectname;

    /**
     * 批文号
     */
    private String approvalno;

    /**
     * 处理时间
     */
    private Date processtime;

    /**
     * 附件id
     */
    private Long contractattachmentid;

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

    public Date getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(String timestamp) {

        if (timestamp != "" || timestamp != null) {

            Date date = new Date(Long.parseLong(timestamp) * 1000);
            this.contractStartTime = date;
        }
    }

    public Date getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(String timestamp) {

        if (timestamp != "" || timestamp != null) {

            Date date = new Date(Long.parseLong(timestamp) * 1000);
            this.contractEndTime = date;
        }
    }
}