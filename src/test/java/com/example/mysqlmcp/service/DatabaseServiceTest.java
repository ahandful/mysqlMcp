package com.example.mysqlmcp.service;

import com.example.mysqlmcp.mcp.SqlRequest;
import com.example.mysqlmcp.mcp.SqlResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseServiceTest {

    @Autowired
    private DatabaseService databaseService;

    @Test
    public void testExecuteSqlWithInvalidConnection() {
        SqlRequest request = new SqlRequest(
            "SELECT 1",
            "invalid_db",
            "localhost",
            3306,
            "invalid_user",
            "invalid_password"
        );

        SqlResponse response = databaseService.executeSql(request);
        
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertTrue(response.getError().contains("Access denied") || 
                  response.getError().contains("Unknown database") ||
                  response.getError().contains("Communications link failure"));
    }

    @Test
    public void testExecuteSqlWithInvalidSql() {
        SqlRequest request = new SqlRequest(
            "INVALID SQL STATEMENT",
            "test",
            "localhost",
            3306,
            "root",
            "password"
        );

        SqlResponse response = databaseService.executeSql(request);
        
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
    }

    @Test
    public void testSqlRequestCreation() {
        SqlRequest request = new SqlRequest(
            "SELECT 1",
            "testdb",
            "localhost",
            3306,
            "user",
            "pass"
        );

        // 验证请求对象创建正确
        assertEquals("SELECT 1", request.getSql());
        assertEquals("testdb", request.getDatabase());
        assertEquals("localhost", request.getHost());
        assertEquals(3306, request.getPort());
        assertEquals("user", request.getUsername());
        assertEquals("pass", request.getPassword());
    }

    @Test
    public void testSqlResponseCreation() {
        SqlResponse response = new SqlResponse(true, "测试成功");
        
        assertTrue(response.isSuccess());
        assertEquals("测试成功", response.getMessage());
        assertNull(response.getError());
        assertNull(response.getData());
        assertEquals(0, response.getAffectedRows());
        assertEquals(0, response.getExecutionTime());
    }

    @Test
    public void testSqlResponseWithData() {
        SqlResponse response = new SqlResponse(true, "查询成功", null);
        
        assertTrue(response.isSuccess());
        assertEquals("查询成功", response.getMessage());
        assertNull(response.getData());
    }
} 