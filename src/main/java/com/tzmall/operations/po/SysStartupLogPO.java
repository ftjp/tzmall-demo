package com.tzmall.operations.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 系统启动日志表
 *
 * @author wrx
 * @date 2023-05-15 11:37:40
 */
@Data
public class SysStartupLogPO {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * mac地址
     */
    private String macAddress;
    /**
     * 启动时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 事件(启动、销毁)
     */
    private String event;

    public SysStartupLogPO() {
    }

    public SysStartupLogPO(String systemName, String ipAddress, String event) {
        this.systemName = systemName;
        this.ipAddress = ipAddress;
        this.event = event;
    }
}
