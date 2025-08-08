@echo off
echo Testing MySQL MCP Server...
echo.

REM 检查jar文件是否存在
if not exist "target\mysql-mcp-1.0.0.jar" (
    echo Error: mysql-mcp-1.0.0.jar not found in target directory
    echo Please run: mvn clean package -DskipTests
    pause
    exit /b 1
)

echo Starting MCP server test...
echo Press Ctrl+C to stop the server
echo.

REM 启动MCP服务器进行测试
java -jar target\mysql-mcp-1.0.0.jar

pause
