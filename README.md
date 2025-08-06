# MySQL MCP Server

这是一个基于Spring Boot的Model Context Protocol (MCP) 服务器，用于执行MySQL数据库SQL操作。

## 功能特性

- 支持MySQL数据库连接和SQL执行
- 基于Stdio模式的MCP协议通信
- 支持SELECT查询和UPDATE/INSERT/DELETE操作
- 完整的错误处理和日志记录
- 执行时间统计
- **多数据库连接配置支持**：支持配置多个数据库连接，可通过连接名称快速切换
- **灵活的连接参数**：支持通过配置文件或请求参数指定数据库连接信息
- **环境变量支持**：支持使用环境变量配置敏感信息（如密码）
- **环境变量优先**：优先使用环境变量配置的数据库连接，提供最大的灵活性

## 系统要求

- Java 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 5.7 或更高版本

## 快速开始

### 1. 编译项目

```bash
mvn clean package
```

### 2. 运行MCP服务器

```bash
java -jar target/mysql-mcp-1.0.0.jar
```

### 3. 配置MCP客户端

在您的MCP客户端配置文件中添加：

**使用环境变量配置数据库连接（推荐）：**
```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": [
        "-jar",
        "path/to/mysql-mcp-1.0.0.jar"
      ],
      "env": {
        "host": "localhost",
        "port": "3306",
        "user": "root",
        "password": "your_password",
        "database": "your_database"
      }
    }
  }
}
```

**基本配置（使用默认连接）：**
```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": [
        "-jar",
        "path/to/mysql-mcp-1.0.0.jar"
      ]
    }
  }
}
```

## 支持的MCP方法

### initialize
初始化MCP服务器连接。

### tools/call
执行SQL操作的工具调用。

#### execute_sql 工具

**参数优先级：环境变量 > connectionName > 请求参数**

参数：
- `sql`: SQL语句（必需）
- `connectionName`: 数据库连接名称（可选，优先级：环境变量 > connectionName > 请求参数）
- `host`: 数据库主机地址（可选，优先级最低）
- `port`: 数据库端口（可选，默认3306）
- `database`: 数据库名称（可选，优先级最低）
- `username`: 用户名（可选，优先级最低）
- `password`: 密码（可选，优先级最低）

**环境变量配置：**
- `host`: 数据库主机地址
- `port`: 数据库端口
- `user`: 用户名
- `password`: 密码
- `database`: 数据库名称

示例请求：

**使用环境变量配置（推荐）：**
```json
{
  "jsonrpc": "2.0",
  "id": "1",
  "method": "tools/call",
  "params": {
    "name": "execute_sql",
    "arguments": {
      "sql": "SELECT * FROM users LIMIT 10"
    }
  }
}
```

**使用配置的连接名称：**
```json
{
  "jsonrpc": "2.0",
  "id": "1",
  "method": "tools/call",
  "params": {
    "name": "execute_sql",
    "arguments": {
      "sql": "SELECT * FROM users LIMIT 10",
      "connectionName": "dev"
    }
  }
}
```

**使用完整的连接参数：**
```json
{
  "jsonrpc": "2.0",
  "id": "1",
  "method": "tools/call",
  "params": {
    "name": "execute_sql",
    "arguments": {
      "sql": "SELECT * FROM users LIMIT 10",
      "host": "localhost",
      "port": 3306,
      "database": "test",
      "username": "root",
      "password": "password"
    }
  }
}
```

## 响应格式

### 成功响应
```json
{
  "jsonrpc": "2.0",
  "id": "1",
  "result": {
    "content": [
      {
        "type": "text",
        "text": "{\"success\":true,\"message\":\"查询执行成功\",\"data\":[...],\"executionTime\":123}"
      }
    ]
  }
}
```

### 错误响应
```json
{
  "jsonrpc": "2.0",
  "id": "1",
  "error": {
    "code": -32603,
    "message": "内部错误",
    "data": "具体错误信息"
  }
}
```

## 配置说明

### 应用配置 (application.yml)

```yaml
spring:
  application:
    name: mysql-mcp-server

logging:
  level:
    com.example.mysqlmcp: DEBUG

# 数据库配置
database:
  # 默认连接配置
  default:
    host: localhost
    port: 3306
    database: test
    username: root
    password: password
    properties:
      useSSL: "false"
      serverTimezone: "UTC"
      allowPublicKeyRetrieval: "true"
  
  # 多个数据库连接配置
  connections:
    # 开发环境数据库
    dev:
      host: localhost
      port: 3306
      database: dev_db
      username: dev_user
      password: dev_password
      properties:
        useSSL: "false"
        serverTimezone: "UTC"
    
    # 测试环境数据库
    test:
      host: test-server
      port: 3306
      database: test_db
      username: test_user
      password: test_password
      properties:
        useSSL: "true"
        serverTimezone: "UTC"
    
    # 生产环境数据库
    prod:
      host: prod-server
      port: 3306
      database: prod_db
      username: prod_user
      password: ${DB_PASSWORD:prod_password}
      properties:
        useSSL: "true"
        serverTimezone: "UTC"
        connectionTimeout: "30000"
        socketTimeout: "60000"
```

## 安全注意事项

1. 在生产环境中，请使用环境变量或安全的配置管理来存储数据库凭据
2. 确保数据库用户具有适当的权限限制
3. 考虑使用SSL连接以提高安全性
4. 定期更新依赖项以修复安全漏洞

## 开发

### 项目结构

```
src/main/java/com/example/mysqlmcp/
├── MySqlMcpApplication.java    # Spring Boot主应用类
├── mcp/
│   ├── McpMessage.java         # MCP消息模型
│   ├── McpServer.java          # MCP服务器核心逻辑
│   ├── SqlRequest.java         # SQL请求模型
│   └── SqlResponse.java        # SQL响应模型
└── service/
    └── DatabaseService.java    # 数据库服务
```

### 添加新功能

1. 在`DatabaseService`中添加新的数据库操作方法
2. 在`McpServer`中添加对应的工具处理方法
3. 更新MCP服务器配置以包含新工具

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。 