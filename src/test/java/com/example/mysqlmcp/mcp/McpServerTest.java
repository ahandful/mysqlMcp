package com.example.mysqlmcp.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class McpServerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testMcpMessageSerialization() throws Exception {
        McpMessage message = new McpMessage("1", "initialize", new HashMap<>());
        
        String json = objectMapper.writeValueAsString(message);
        McpMessage deserialized = objectMapper.readValue(json, McpMessage.class);
        
        assertEquals("2.0", deserialized.getJsonrpc());
        assertEquals("1", deserialized.getId());
        assertEquals("initialize", deserialized.getMethod());
    }

    @Test
    public void testSqlRequestSerialization() throws Exception {
        SqlRequest request = new SqlRequest(
            "SELECT * FROM users",
            "testdb",
            "localhost",
            3306,
            "user",
            "pass"
        );
        
        String json = objectMapper.writeValueAsString(request);
        SqlRequest deserialized = objectMapper.readValue(json, SqlRequest.class);
        
        assertEquals("SELECT * FROM users", deserialized.getSql());
        assertEquals("testdb", deserialized.getDatabase());
        assertEquals("localhost", deserialized.getHost());
        assertEquals(3306, deserialized.getPort());
        assertEquals("user", deserialized.getUsername());
        assertEquals("pass", deserialized.getPassword());
    }

    @Test
    public void testSqlResponseSerialization() throws Exception {
        SqlResponse response = new SqlResponse(true, "查询成功");
        response.setExecutionTime(123L);
        response.setAffectedRows(5);
        
        String json = objectMapper.writeValueAsString(response);
        SqlResponse deserialized = objectMapper.readValue(json, SqlResponse.class);
        
        assertTrue(deserialized.isSuccess());
        assertEquals("查询成功", deserialized.getMessage());
        assertEquals(123L, deserialized.getExecutionTime());
        assertEquals(5, deserialized.getAffectedRows());
    }

    @Test
    public void testToolCallMessageStructure() throws Exception {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("sql", "SELECT 1");
        arguments.put("host", "localhost");
        arguments.put("port", 3306);
        arguments.put("database", "test");
        arguments.put("username", "root");
        arguments.put("password", "password");
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", "execute_sql");
        params.put("arguments", arguments);
        
        McpMessage message = new McpMessage("1", "tools/call", params);
        
        String json = objectMapper.writeValueAsString(message);
        assertNotNull(json);
        assertTrue(json.contains("execute_sql"));
        assertTrue(json.contains("SELECT 1"));
    }
} 