@echo off
echo Starting MySQL MCP Server...
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or later
    pause
    exit /b 1
)

REM 检查jar文件是否存在
if not exist "mysql-mcp-1.0.0.jar" (
    echo Error: mysql-mcp-1.0.0.jar not found
    echo Please make sure the jar file is in the same directory as this script
    pause
    exit /b 1
)

REM 启动MCP服务器
echo Starting MCP server with stdio mode...
java -jar mysql-mcp-1.0.0.jar

pause 