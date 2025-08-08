package com.example.mysqlmcp.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseConfig {
    
    public static class DatabaseConnection {
        private String host = "localhost";
        private int port = 3306;
        private String database;
        private String username;
        private String password;
        private Map<String, String> properties = new HashMap<>();
        
        // Getters and Setters
        public String getHost() {
            return host;
        }
        
        public void setHost(String host) {
            this.host = host;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
        
        public String getDatabase() {
            return database;
        }
        
        public void setDatabase(String database) {
            this.database = database;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public Map<String, String> getProperties() {
            return properties;
        }
        
        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
        
        public String buildConnectionUrl() {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(database);
            urlBuilder.append("?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
            
            // 添加自定义属性
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            
            return urlBuilder.toString();
        }
    }
    
    /**
     * 从环境变量创建数据库连接配置
     */
    public DatabaseConnection createConnectionFromEnv() {
        DatabaseConnection connection = new DatabaseConnection();
        
        // 从环境变量读取配置
        String host = System.getenv("host");
        String portStr = System.getenv("port");
        String database = System.getenv("database");
        String username = System.getenv("user");
        String password = System.getenv("password");
        
        if (host != null) {
            connection.setHost(host);
        }
        
        if (portStr != null) {
            try {
                connection.setPort(Integer.parseInt(portStr));
            } catch (NumberFormatException e) {
                // 使用默认端口
            }
        }
        
        if (database != null) {
            connection.setDatabase(database);
        }
        
        if (username != null) {
            connection.setUsername(username);
        }
        
        if (password != null) {
            connection.setPassword(password);
        }
        
        return connection;
    }
    
    /**
     * 检查是否通过环境变量配置了数据库连接
     */
    public boolean hasEnvConnection() {
        return System.getenv("host") != null || 
               System.getenv("database") != null || 
               System.getenv("user") != null;
    }
} 