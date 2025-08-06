package com.example.mysqlmcp.mcp;

import com.example.mysqlmcp.service.DatabaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.List;

@Component
public class McpServer {
    
    private static final Logger logger = LoggerFactory.getLogger(McpServer.class);
    
    @Autowired
    private DatabaseService databaseService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    private BufferedReader reader;
    private PrintWriter writer;
    
    @PostConstruct
    public void start() {
        logger.info("启动MySQL MCP服务器...");
        
        // 设置标准输入输出
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out, true);
        
        // 启动消息处理线程
        executorService.submit(this::processMessages);
    }
    
    private void processMessages() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    processMessage(line);
                } catch (Exception e) {
                    logger.error("处理消息时发生错误", e);
                    sendError(null, -32603, "内部错误", e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("读取输入流时发生错误", e);
        }
    }
    
    private void processMessage(String message) throws Exception {
        logger.debug("收到消息: {}", message);
        
        McpMessage mcpMessage = objectMapper.readValue(message, McpMessage.class);
        
        switch (mcpMessage.getMethod()) {
            case "initialize":
                handleInitialize(mcpMessage);
                break;
            case "tools/call":
                handleToolCall(mcpMessage);
                break;
            case "notifications/cancel":
                handleCancel(mcpMessage);
                break;
            default:
                logger.warn("未知的方法: {}", mcpMessage.getMethod());
                sendError(mcpMessage.getId(), -32601, "方法未找到", "未知方法: " + mcpMessage.getMethod());
        }
    }
    
    private void handleInitialize(McpMessage message) throws Exception {
        logger.info("处理初始化请求");
        
        McpMessage response = new McpMessage();
        response.setId(message.getId());
        response.setResult(Map.of(
            "protocolVersion", "2024-11-05",
            "capabilities", Map.of(
                "tools", Map.of()
            ),
            "serverInfo", Map.of(
                "name", "mysql-mcp-server",
                "version", "1.0.0"
            )
        ));
        
        sendResponse(response);
        
        // 发送工具列表
        sendToolsList();
    }
    
    private void sendToolsList() throws Exception {
        Map<String, Object> tool = new HashMap<>();
        tool.put("name", "execute_sql");
        tool.put("description", "执行MySQL数据库SQL语句。优先使用环境变量配置的数据库连接，其次使用connectionName或请求参数");
        tool.put("inputSchema", Map.of(
            "type", "object",
            "properties", Map.of(
                "connectionName", Map.of(
                    "type", "string",
                    "description", "数据库连接名称（可选，优先级：环境变量 > connectionName > 请求参数）"
                ),
                "sql", Map.of(
                    "type", "string",
                    "description", "要执行的SQL语句"
                ),
                "host", Map.of(
                    "type", "string",
                    "description", "数据库主机地址（可选，优先级最低）"
                ),
                "port", Map.of(
                    "type", "integer",
                    "description", "数据库端口（可选，默认3306）"
                ),
                "database", Map.of(
                    "type", "string",
                    "description", "数据库名称（可选，优先级最低）"
                ),
                "username", Map.of(
                    "type", "string",
                    "description", "用户名（可选，优先级最低）"
                ),
                "password", Map.of(
                    "type", "string",
                    "description", "密码（可选，优先级最低）"
                )
            ),
            "required", List.of("sql")
        ));
        
        McpMessage toolsMessage = new McpMessage();
        toolsMessage.setMethod("tools/list");
        toolsMessage.setResult(Map.of("tools", List.of(tool)));
        
        sendResponse(toolsMessage);
    }
    
    private void handleToolCall(McpMessage message) throws Exception {
        logger.info("处理工具调用请求");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) message.getParams();
        String name = (String) params.get("name");
        
        if ("execute_sql".equals(name)) {
            handleSqlExecution(message, params);
        } else {
            sendError(message.getId(), -32601, "方法未找到", "未知工具: " + name);
        }
    }
    
    private void handleSqlExecution(McpMessage message, Map<String, Object> params) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, Object> arguments = (Map<String, Object>) params.get("arguments");
        
        SqlRequest sqlRequest = objectMapper.convertValue(arguments, SqlRequest.class);
        
        // 执行SQL
        SqlResponse sqlResponse = databaseService.executeSql(sqlRequest);
        
        // 构建响应
        McpMessage response = new McpMessage();
        response.setId(message.getId());
        response.setResult(Map.of(
            "content", List.of(Map.of(
                "type", "text",
                "text", objectMapper.writeValueAsString(sqlResponse)
            ))
        ));
        
        sendResponse(response);
    }
    
    private void handleCancel(McpMessage message) {
        logger.info("处理取消请求");
        // 这里可以实现取消逻辑
    }
    
    private void sendResponse(McpMessage response) throws Exception {
        String jsonResponse = objectMapper.writeValueAsString(response);
        logger.debug("发送响应: {}", jsonResponse);
        writer.println(jsonResponse);
    }
    
    private void sendError(String id, int code, String message, String data) {
        try {
            McpMessage errorResponse = new McpMessage();
            errorResponse.setId(id);
            errorResponse.setError(Map.of(
                "code", code,
                "message", message,
                "data", data
            ));
            
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            logger.debug("发送错误响应: {}", jsonResponse);
            writer.println(jsonResponse);
        } catch (Exception e) {
            logger.error("发送错误响应失败", e);
        }
    }
} 