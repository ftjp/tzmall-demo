package com.tzmall.operations.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import com.tzmall.operations.po.SysStartupLogPO;
import com.tzmall.operations.util.db.ProRecordDBUtil;
import com.tzmall.operations.util.db.SQLexecuteUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务启动工具监测工具类
 */
@Slf4j
public class ServiceStartupUtil {

    /**
     * 监测服务启动、销毁信息
     */
    public static String checkServiceStartup(Connection conn, String beginDate, String endDate) {
        try {
            List<SysStartupLogPO> startupList = listStartup(conn, beginDate, endDate);
            if (CollUtil.isNotEmpty(startupList)) {
                StringBuffer msgBuffer = new StringBuffer("【商城服务启停推送】\n");
                startupList.forEach(startup -> {
                    String ip = StrUtil.length(startup.getIpAddress()) > 17 ? StrUtil.subPre(startup.getIpAddress(), startup.getIpAddress().length() - 17) : startup.getIpAddress();
                    msgBuffer.append(String.format("> %s在%s%s了,IP:%s\n", startup.getSystemName(), DateUtil.formatDateTime(startup.getCreateTime()), startup.getEvent(), ip));
                });
                log.info(msgBuffer.toString());
                return msgBuffer.toString();
            }
        } catch (SQLException e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        }
        return StrUtil.EMPTY;
    }

    /**
     * 查询最近3分钟的启动日志记录
     *
     * @return
     */
    private static List<SysStartupLogPO> listStartup(Connection conn, String beginDate, String endDate) throws SQLException {
        String sql = String.format("SELECT * FROM sys_startup_log WHERE create_time>='%s' AND create_time<'%s' ORDER BY create_time DESC;", beginDate, endDate);
        log.info("sql: {}",sql);
        List<Entity> entityList = SQLexecuteUtil.executeQuery(conn, sql);
        log.info("执行sql后的结果：{}", JSONUtil.toJsonStr(entityList));
        List<SysStartupLogPO> startupList = new ArrayList<>();
        if (CollUtil.isNotEmpty(entityList)) {
            entityList.forEach(entity -> {
                SysStartupLogPO startup = new SysStartupLogPO();
                entity.toBeanWithCamelCase(startup);
                startupList.add(startup);
            });
        }
        return startupList;
    }

}
