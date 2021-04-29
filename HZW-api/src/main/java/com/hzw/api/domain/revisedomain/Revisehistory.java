package com.hzw.api.domain.revisedomain;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@TableName(value = "company_cf_revisehistory")
public class Revisehistory {

    /**
     * 溯源码
     */
    private String tracesn;

    /**
     * 修改原因
     */
    private String reason;

    /**
     * 修改码
     */
    private String modifysn;

    /**
     * 修改申请状态1=修改申请中，2=修改申请成功，3=修改申请失败
     */
    private int modifystatus;

    /**
     * 修改前的数据
     */
    private String xgq;

    /**
     * 修改后的数据
     */
    private String xgh;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 审批时间
     */
    private Date processTIme;
    private String comment;

    public String getTracesn() {
        return tracesn;
    }

    public void setTracesn(String tracesn) {
        this.tracesn = tracesn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModifysn() {
        return modifysn;
    }

    public void setModifysn(String modifysn) {
        this.modifysn = modifysn;
    }

    public int getModifystatus() {
        return modifystatus;
    }

    public void setModifystatus(int modifystatus) {
        this.modifystatus = modifystatus;
    }

    public String getXgq() {
        return xgq;
    }

    public void setXgq(String xgq) {
        this.xgq = xgq;
    }

    public String getXgh() {
        return xgh;
    }

    public void setXgh(String xgh) {
        this.xgh = xgh;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getProcessTIme() {
        return processTIme;
    }

    public void setProcessTIme(Date processTIme) {
        this.processTIme = processTIme;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}