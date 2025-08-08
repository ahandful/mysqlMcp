# MySQL MCP Server 使用说明

基于Spring Boot的MySQL数据库MCP服务器

## 快速配置

### 1. 使用环境变量配置（推荐）

在您的MCP客户端配置文件中添加：

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

### 2. 使用SQL

配置完成后，您可以直接执行SQL：

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

## 配置优先级

1. **环境变量**（最高优先级）
   - `host`: 数据库主机
   - `port`: 数据库端口
   - `user`: 用户名
   - `password`: 密码
   - `database`: 数据库名

2. **请求参数**（备用）
   - 在请求中直接指定连接参数

## 安全建议

1. 使用环境变量存储敏感信息
2. 避免在代码中硬编码数据库凭据
3. 使用强密码和适当的数据库权限
4. 考虑使用SSL连接

## 示例

### 查询数据
```json
{
  "sql": "SELECT id, name, email FROM users WHERE status = 'active'"
}
```

### 插入数据
```json
{
  "sql": "INSERT INTO users (name, email) VALUES ('John Doe', 'john@example.com')"
}
```

### 更新数据
```json
{
  "sql": "UPDATE users SET status = 'inactive' WHERE last_login < '2024-01-01'"
}
```

### 删除数据
```json
{
  "sql": "DELETE FROM users WHERE status = 'deleted'"
}
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