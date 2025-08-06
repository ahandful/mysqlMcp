@echo off
echo 启动MySQL MCP服务器...
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Java运行时环境
    echo 请确保已安装Java 17或更高版本
    pause
    exit /b 1
)

REM 检查Maven是否安装
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Maven
    echo 请确保已安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo 编译项目...
call mvn clean package -DskipTests

if errorlevel 1 (
    echo 编译失败
    pause
    exit /b 1
)

echo.
echo 启动MCP服务器...
echo 按Ctrl+C停止服务器
echo.

java -jar target/mysql-mcp-1.0.0.jar

pause 