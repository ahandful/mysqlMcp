# MySQL MCP Server

这是一个基于Spring Boot的Model Context Protocol (MCP) 服务器，用于执行MySQL数据库SQL操作。支持通过stdio模式与Claude Desktop等MCP客户端通信。

## 功能特性

- 支持MySQL数据库连接和SQL执行
- 基于Stdio模式的MCP协议通信
- 支持SELECT查询和UPDATE/INSERT/DELETE操作
- 完整的错误处理和日志记录
- 执行时间统计
- **环境变量配置**：支持通过环境变量配置数据库连接信息
- **请求参数配置**：支持在请求中直接指定连接参数
- **Claude Desktop兼容**：完全兼容Claude Desktop的MCP协议

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

### 3. 配置Claude Desktop

#### 方法1：使用配置文件

1. 将编译好的jar文件复制到Claude Desktop的配置目录
2. 在Claude Desktop的配置文件中添加：

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

#### 方法2：使用环境变量配置（推荐）

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

## 支持的MCP方法

### initialize
初始化MCP服务器连接。

### tools/call
执行SQL操作的工具调用。

#### execute_sql 工具

**配置优先级：环境变量 > 请求参数**

参数：
- `sql` (必需): 要执行的SQL语句
- `host` (可选): 数据库主机，默认localhost
- `port` (可选): 数据库端口，默认3306
- `database` (可选): 数据库名
- `username` (可选): 用户名
- `password` (可选): 密码

**环境变量配置：**
- `host`: 数据库主机
- `port`: 数据库端口
- `user`: 用户名
- `password`: 密码
- `database`: 数据库名

## 使用示例

### 1. 使用环境变量配置（推荐）

**Claude Desktop配置：**
```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": ["-jar", "path/to/mysql-mcp-1.0.0.jar"],
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

**在Claude Desktop中执行SQL：**
```
请帮我查询用户表中的所有数据
```

### 2. 使用请求参数配置

**在Claude Desktop中执行SQL：**
```
请连接到数据库 localhost:3306/test，用户名为 root，密码为 password，然后执行查询：SELECT * FROM users LIMIT 10
```

## 环境切换

### 开发环境
```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": ["-jar", "path/to/mysql-mcp-1.0.0.jar"],
      "env": {
        "host": "localhost",
        "port": "3306",
        "user": "dev_user",
        "password": "dev_password",
        "database": "dev_db"
      }
    }
  }
}
```

### 测试环境
```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": ["-jar", "path/to/mysql-mcp-1.0.0.jar"],
      "env": {
        "host": "test-server",
        "port": "3306",
        "user": "test_user",
        "password": "test_password",
        "database": "test_db"
      }
    }
  }
}
```

### 生产环境
```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": ["-jar", "path/to/mysql-mcp-1.0.0.jar"],
      "env": {
        "host": "prod-server",
        "port": "3306",
        "user": "prod_user",
        "password": "prod_password",
        "database": "prod_db"
      }
    }
  }
}
```

## 应用配置 (application.yml)

```yaml
spring:
  application:
    name: mysql-mcp-server

  # DataSource configuration for Spring Boot auto-configuration
  datasource:
    url: jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  # JPA configuration
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

logging:
  level:
    com.example.mysqlmcp: DEBUG

server:
  port: 8080
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
├── config/
│   └── DatabaseConfig.java     # 数据库配置管理
├── mcp/
│   ├── McpMessage.java         # MCP消息模型
│   ├── McpServer.java          # MCP服务器核心逻辑
│   ├── SqlRequest.java         # SQL请求模型
│   └── SqlResponse.java        # SQL响应模型
└── service/
    └── DatabaseService.java    # 数据库操作服务
```

### 配置说明

项目采用简化的配置方式：

1. **环境变量优先**：如果设置了环境变量，优先使用环境变量配置
2. **请求参数备用**：如果没有环境变量，使用请求中的参数
3. **移除复杂配置**：不再支持dev/test/prod预定义环境，简化配置管理

### 环境变量说明

- `host`: 数据库主机地址
- `port`: 数据库端口号
- `user`: 数据库用户名
- `password`: 数据库密码
- `database`: 数据库名称

## Claude Desktop集成

### 安装步骤

1. **编译项目**
   ```bash
   mvn clean package
   ```

2. **复制jar文件**
   将 `target/mysql-mcp-1.0.0.jar` 复制到Claude Desktop的配置目录

3. **配置Claude Desktop**
   在Claude Desktop的设置中添加MCP服务器配置

4. **重启Claude Desktop**
   重启应用以使配置生效

### 使用方式

配置完成后，您可以在Claude Desktop中直接使用自然语言与数据库交互：

- "查询用户表中的所有数据"
- "插入一条新用户记录"
- "更新用户状态为活跃"
- "删除过期数据"

Claude会自动调用相应的SQL操作并返回结果。 