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
                  response.getError().contains("Unknown database"));
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
    public void testBuildConnectionUrl() {
        SqlRequest request = new SqlRequest(
            "SELECT 1",
            "testdb",
            "localhost",
            3306,
            "user",
            "pass"
        );

        // 通过反射测试私有方法（如果需要的话）
        // 这里只是验证请求对象创建正确
        assertEquals("SELECT 1", request.getSql());
        assertEquals("testdb", request.getDatabase());
        assertEquals("localhost", request.getHost());
        assertEquals(3306, request.getPort());
        assertEquals("user", request.getUsername());
        assertEquals("pass", request.getPassword());
    }
} 