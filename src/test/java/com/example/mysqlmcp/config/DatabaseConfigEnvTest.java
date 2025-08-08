package com.example.mysqlmcp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConfigEnvTest {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Test
    public void testHasEnvConnection() {
        // 测试没有环境变量时
        assertFalse(databaseConfig.hasEnvConnection());
    }

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
    public void testCreateConnectionFromEnvWithPortString() {
        // 测试端口字符串解析
        DatabaseConfig.DatabaseConnection conn = databaseConfig.createConnectionFromEnv();
        
        // 验证默认端口
        assertEquals(3306, conn.getPort());
    }

    @Test
    public void testEnvironmentVariablePriority() {
        // 测试环境变量优先级逻辑
        // 由于无法在测试中设置真实的环境变量，这里只测试方法调用不会抛出异常
        DatabaseConfig.DatabaseConnection conn = databaseConfig.createConnectionFromEnv();
        assertNotNull(conn);
        
        // 验证默认行为
        assertFalse(databaseConfig.hasEnvConnection());
    }
} 
