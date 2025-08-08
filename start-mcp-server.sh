#!/bin/bash

echo "Starting MySQL MCP Server..."
echo

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or later"
    exit 1
fi

# 检查jar文件是否存在
if [ ! -f "mysql-mcp-1.0.0.jar" ]; then
    echo "Error: mysql-mcp-1.0.0.jar not found"
    echo "Please make sure the jar file is in the same directory as this script"
    exit 1
fi

# 启动MCP服务器
echo "Starting MCP server with stdio mode..."
java -jar mysql-mcp-1.0.0.jar 