package com.tzmall.operations.util.db;

import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
public class SQLexecuteUtil {

    /**
     * 执行sql (执行完会放回连接池）
     *
     * @param conn
     * @param sql
     * @return List<Entity>
     * @throws SQLException
     */
    public static List<Entity> executeQuery(Connection conn, String sql) throws SQLException {
        //资源对象之间存在依赖关系，按照倒序依次关闭它们
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            return EntityListHandler.create().handle(rs);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                log.error("未能正常关闭的 Connection,当前sql:{}", sql);
            }
        }
    }
}
