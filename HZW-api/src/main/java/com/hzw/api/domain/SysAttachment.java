package com.hzw.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.annotation.security.DenyAll;
import java.util.Date;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */
@Data
@TableName(value = "sys_attachment ")
public class SysAttachment {
    /**
     * 主键
     */
    private Long attachment_Id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件大小
     */
    private Float length;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

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