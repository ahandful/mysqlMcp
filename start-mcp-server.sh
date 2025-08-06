#!/bin/bash

echo "启动MySQL MCP服务器..."
echo

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java运行时环境"
    echo "请确保已安装Java 17或更高版本"
    exit 1
fi

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven"
    echo "请确保已安装Maven 3.6或更高版本"
    exit 1
fi

echo "编译项目..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "编译失败"
    exit 1
fi

echo
echo "启动MCP服务器..."
echo "按Ctrl+C停止服务器"
echo

java -jar target/mysql-mcp-1.0.0.jar 