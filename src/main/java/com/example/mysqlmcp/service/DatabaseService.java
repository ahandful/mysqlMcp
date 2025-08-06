package com.example.mysqlmcp.service;

import com.example.mysqlmcp.config.DatabaseConfig;
import com.example.mysqlmcp.mcp.SqlRequest;
import com.example.mysqlmcp.mcp.SqlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class DatabaseService {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    
    @Autowired
    private DatabaseConfig databaseConfig;
    
    public SqlResponse executeSql(SqlRequest request) {
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        
        try {
            // 获取数据库连接配置
            DatabaseConfig.DatabaseConnection dbConfig = getDatabaseConnection(request);
            
            // 构建数据库连接URL
            String url = buildConnectionUrl(request, dbConfig);
            logger.info("连接到数据库: {}", url);
            
            // 建立连接
            String username = getUsername(request, dbConfig);
            String password = getPassword(request, dbConfig);
            connection = DriverManager.getConnection(url, username, password);
            
            // 执行SQL
            String sql = request.getSql().trim();
            logger.info("执行SQL: {}", sql);
            
            if (sql.toLowerCase().startsWith("select")) {
                return executeQuery(connection, sql, startTime);
            } else {
                return executeUpdate(connection, sql, startTime);
            }
            
        } catch (SQLException e) {
            logger.error("数据库操作失败", e);
            SqlResponse response = new SqlResponse(false, "数据库操作失败");
            response.setError(e.getMessage());
            response.setExecutionTime(System.currentTimeMillis() - startTime);
            return response;
        } catch (Exception e) {
            logger.error("执行SQL时发生未知错误", e);
            SqlResponse response = new SqlResponse(false, "执行SQL时发生未知错误");
            response.setError(e.getMessage());
            response.setExecutionTime(System.currentTimeMillis() - startTime);
            return response;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("关闭数据库连接失败", e);
                }
            }
        }
    }
    
    private DatabaseConfig.DatabaseConnection getDatabaseConnection(SqlRequest request) {
        // 优先检查环境变量配置
        if (databaseConfig.hasEnvConnection()) {
            logger.info("使用环境变量配置的数据库连接");
            return databaseConfig.createConnectionFromEnv();
        }
        
        // 如果指定了连接名称，使用配置的连接
        if (request.getConnectionName() != null && !request.getConnectionName().isEmpty()) {
            DatabaseConfig.DatabaseConnection config = databaseConfig.getConnection(request.getConnectionName());
            if (config == null) {
                throw new RuntimeException("未找到数据库连接配置: " + request.getConnectionName());
            }
            return config;
        }
        
        // 否则使用请求中的参数创建临时配置
        DatabaseConfig.DatabaseConnection config = new DatabaseConfig.DatabaseConnection();
        config.setHost(request.getHost());
        config.setPort(request.getPort());
        config.setDatabase(request.getDatabase());
        config.setUsername(request.getUsername());
        config.setPassword(request.getPassword());
        return config;
    }
    
    private String buildConnectionUrl(SqlRequest request, DatabaseConfig.DatabaseConnection dbConfig) {
        // 如果请求中提供了完整的连接信息，优先使用
        if (request.getHost() != null && request.getDatabase() != null) {
            return String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    request.getHost(), request.getPort(), request.getDatabase());
        }
        
        // 否则使用配置的连接
        return dbConfig.buildConnectionUrl();
    }
    
    private String getUsername(SqlRequest request, DatabaseConfig.DatabaseConnection dbConfig) {
        return request.getUsername() != null ? request.getUsername() : dbConfig.getUsername();
    }
    
    private String getPassword(SqlRequest request, DatabaseConfig.DatabaseConnection dbConfig) {
        return request.getPassword() != null ? request.getPassword() : dbConfig.getPassword();
    }
    
    private SqlResponse executeQuery(Connection connection, String sql, long startTime) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // 获取列名
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            
            // 处理结果集
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = columnNames.get(i - 1);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        }
        
        SqlResponse response = new SqlResponse(true, "查询执行成功", results);
        response.setExecutionTime(System.currentTimeMillis() - startTime);
        return response;
    }
    
    private SqlResponse executeUpdate(Connection connection, String sql, long startTime) throws SQLException {
        int affectedRows;
        
        try (Statement stmt = connection.createStatement()) {
            affectedRows = stmt.executeUpdate(sql);
        }
        
        SqlResponse response = new SqlResponse(true, "更新操作执行成功");
        response.setAffectedRows(affectedRows);
        response.setExecutionTime(System.currentTimeMillis() - startTime);
        return response;
    }
} 