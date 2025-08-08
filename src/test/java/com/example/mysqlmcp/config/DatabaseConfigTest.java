package com.example.mysqlmcp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConfigTest {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Test
    public void testCreateConnectionFromEnv() {
        DatabaseConfig.DatabaseConnection conn = databaseConfig.createConnectionFromEnv();
        
        // 如果没有设置环境变量，应该使用默认值
        assertNotNull(conn);
        assertEquals("localhost", conn.getHost());
        assertEquals(3306, conn.getPort());
        assertNull(conn.getDatabase()); // 没有环境变量时应该为null
        assertNull(conn.getUsername()); // 没有环境变量时应该为null
        assertNull(conn.getPassword()); // 没有环境变量时应该为null
    }

    @Test
    public void testHasEnvConnection() {
        // 测试没有环境变量时
        assertFalse(databaseConfig.hasEnvConnection());
    }

    @Test
    public void testConnectionUrlBuilding() {
        DatabaseConfig.DatabaseConnection conn = new DatabaseConfig.DatabaseConnection();
        conn.setHost("localhost");
        conn.setPort(3306);
        conn.setDatabase("testdb");
        conn.getProperties().put("useSSL", "false");
        conn.getProperties().put("serverTimezone", "UTC");

        String url = conn.buildConnectionUrl();
        assertTrue(url.contains("jdbc:mysql://localhost:3306/testdb"));
        assertTrue(url.contains("useSSL=false"));
        assertTrue(url.contains("serverTimezone=UTC"));
        assertTrue(url.contains("allowPublicKeyRetrieval=true"));
    }

    @Test
    public void testDatabaseConnectionSettersAndGetters() {
        DatabaseConfig.DatabaseConnection conn = new DatabaseConfig.DatabaseConnection();
        
        conn.setHost("test-host");
        conn.setPort(3307);
        conn.setDatabase("test-db");
        conn.setUsername("test-user");
        conn.setPassword("test-pass");
        
        assertEquals("test-host", conn.getHost());
        assertEquals(3307, conn.getPort());
        assertEquals("test-db", conn.getDatabase());
        assertEquals("test-user", conn.getUsername());
        assertEquals("test-pass", conn.getPassword());
    }
} 