package com.tzmall.operations.util.db;

import cn.hutool.db.Entity;
import cn.hutool.db.ds.pooled.DbConfig;
import cn.hutool.db.ds.pooled.PooledDataSource;
import cn.hutool.db.handler.EntityListHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * tzmall_record数据库工具类
 *
 * @author wrx
 */
@Slf4j
public class ProRecordDBUtil {
    /**
     * 正式db配置
     */
    public static final String DB_PRO_FILENAME = "db/pro_db_setting.properties";

    // 单例_仅初始化一次
    private static volatile ProRecordDBUtil instance;
    private DataSource dataSource;

    private ProRecordDBUtil(String filename) {
        try (InputStream inputStream = new ClassPathResource(filename).getInputStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String host = properties.getProperty("db.host");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            String dbParam = properties.getProperty("db.param");
            String recordDB = properties.getProperty("db.record");
            DbConfig dbConfig = new DbConfig(host + recordDB + dbParam, user, password);
            dbConfig.setMaxWait(10000);
            dbConfig.setInitialSize(5);
            dbConfig.setMinIdle(1);
            dbConfig.setMaxActive(5);
            dataSource = new PooledDataSource(dbConfig);
        } catch (Exception e) {
            log.error("Could not load database configuration file", e);
        }
    }

    public static ProRecordDBUtil getInstance(String filename) {
        if (instance == null) {
            synchronized (ProRecordDBUtil.class) {
                if (instance == null) {
                    instance = new ProRecordDBUtil(filename);
                }
            }
        }
        log.info("获取实例");
        return instance;
    }

    /**
     * 获取db链接
     *
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("获取链接");
        return connection;
    }

    /**
     * 执行sql
     *
     * @param sql
     * @return List<Entity>
     * @throws SQLException
     */
    public List<Entity> executeQuery(String sql) throws SQLException {
        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            return EntityListHandler.create().handle(resultSet);
        }
    }


}