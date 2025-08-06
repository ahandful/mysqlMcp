package com.example.mysqlmcp.mcp;

public class SqlRequest {
    private String sql;
    private String connectionName; // 新增：连接名称
    private String database;
    private String host;
    private int port;
    private String username;
    private String password;

    public SqlRequest() {}

    public SqlRequest(String sql) {
        this.sql = sql;
    }

    public SqlRequest(String sql, String database, String host, int port, String username, String password) {
        this.sql = sql;
        this.database = database;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public SqlRequest(String sql, String connectionName) {
        this.sql = sql;
        this.connectionName = connectionName;
    }

    // Getters and Setters
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

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
} 