# Claude Desktop 配置指南

## 概述

这个MySQL MCP服务器完全兼容Claude Desktop，允许您通过自然语言与MySQL数据库进行交互。

## 安装步骤

### 1. 编译项目

```bash
mvn clean package -DskipTests
```

编译完成后，您会在 `target/` 目录下找到 `mysql-mcp-1.0.0.jar` 文件。

### 2. 准备文件

1. 将 `target/mysql-mcp-1.0.0.jar` 复制到一个固定目录（例如：`C:\mcp-servers\mysql-mcp-1.0.0.jar`）
2. 确保该目录路径在Claude Desktop配置中正确引用

### 3. 配置Claude Desktop

#### 方法1：通过Claude Desktop界面配置

1. 打开Claude Desktop
2. 进入设置（Settings）
3. 找到 "Model Context Protocol" 或 "MCP" 设置
4. 添加新的MCP服务器配置

#### 方法2：直接编辑配置文件

在Claude Desktop的配置文件中添加以下内容：

```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": [
        "-jar",
        "C:\\mcp-servers\\mysql-mcp-1.0.0.jar"
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

### 4. 环境变量配置

您可以通过环境变量配置数据库连接：

| 环境变量 | 说明 | 示例 |
|---------|------|------|
| `host` | 数据库主机地址 | `localhost` |
| `port` | 数据库端口 | `3306` |
| `user` | 数据库用户名 | `root` |
| `password` | 数据库密码 | `your_password` |
| `database` | 数据库名称 | `your_database` |

## 使用示例

### 基本查询

在Claude Desktop中，您可以直接使用自然语言：

```
请帮我查询用户表中的所有数据
```

Claude会自动生成并执行相应的SQL查询。

### 复杂查询

```
请查询最近一周注册的用户，按注册时间倒序排列，只显示前10条记录
```

### 数据操作

```
请插入一条新用户记录，用户名为"张三"，邮箱为"zhangsan@example.com"
```

```
请将用户ID为123的用户状态更新为"活跃"
```

```
请删除30天前注册但从未登录的用户
```

## 环境切换

### 开发环境

```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": ["-jar", "C:\\mcp-servers\\mysql-mcp-1.0.0.jar"],
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
      "args": ["-jar", "C:\\mcp-servers\\mysql-mcp-1.0.0.jar"],
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
      "args": ["-jar", "C:\\mcp-servers\\mysql-mcp-1.0.0.jar"],
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

## 故障排除

### 1. Java版本问题

确保您的系统安装了Java 17或更高版本：

```bash
java -version
```

### 2. 权限问题

确保Claude Desktop有权限执行jar文件。

### 3. 数据库连接问题

检查数据库连接参数是否正确：
- 主机地址
- 端口号
- 用户名和密码
- 数据库名称

### 4. 日志查看

如果遇到问题，可以查看Claude Desktop的日志文件来获取更多信息。

## 安全注意事项

1. **密码安全**：不要在配置文件中明文存储生产环境的密码
2. **网络访问**：确保数据库服务器只允许必要的网络访问
3. **用户权限**：为MCP服务器创建专用的数据库用户，只授予必要的权限
4. **SSL连接**：在生产环境中使用SSL连接

## 高级配置

### 自定义JVM参数

如果需要调整JVM参数，可以修改启动命令：

```json
{
  "mcpServers": {
    "mysql-mcp": {
      "command": "java",
      "args": [
        "-Xmx512m",
        "-jar",
        "C:\\mcp-servers\\mysql-mcp-1.0.0.jar"
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

### 多数据库配置

如果需要连接多个数据库，可以配置多个MCP服务器：

```json
{
  "mcpServers": {
    "mysql-dev": {
      "command": "java",
      "args": ["-jar", "C:\\mcp-servers\\mysql-mcp-1.0.0.jar"],
      "env": {
        "host": "localhost",
        "port": "3306",
        "user": "dev_user",
        "password": "dev_password",
        "database": "dev_db"
      }
    },
    "mysql-prod": {
      "command": "java",
      "args": ["-jar", "C:\\mcp-servers\\mysql-mcp-1.0.0.jar"],
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

## 支持的功能

- ✅ SELECT查询
- ✅ INSERT插入
- ✅ UPDATE更新
- ✅ DELETE删除
- ✅ 复杂SQL查询
- ✅ 多表关联查询
- ✅ 聚合函数
- ✅ 子查询
- ✅ 事务支持
- ✅ 错误处理

## 联系支持

如果您在使用过程中遇到问题，请检查：
1. Java版本是否正确
2. 数据库连接参数是否正确
3. 网络连接是否正常
4. 数据库用户权限是否足够
