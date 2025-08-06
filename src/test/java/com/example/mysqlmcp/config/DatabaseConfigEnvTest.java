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
        
        // 设置环境变量后测试
        try {
            System.setProperty("host", "test-host");
            System.setProperty("database", "test-db");
            System.setProperty("user", "test-user");
            
            // 注意：System.setProperty不会影响System.getenv()，这里只是演示逻辑
            // 实际测试需要在真实的环境变量环境中进行
        } finally {
            // 清理测试环境
            System.clearProperty("host");
            System.clearProperty("database");
            System.clearProperty("user");
        }
    }

    @Test
    public void testCreateConnectionFromEnv() {
        DatabaseConfig.DatabaseConnection conn = databaseConfig.createConnectionFromEnv();
        
        // 如果没有设置环境变量，应该使用默认值
        assertNotNull(conn);
        assertEquals("localhost", conn.getHost());
        assertEquals(3306, conn.getPort());
    }

    @Test
    public void testConnectionPriority() {
        // 测试连接优先级逻辑
        DatabaseConfig.DatabaseConnection conn = databaseConfig.getConnection("dev");
        assertNotNull(conn);
        
        // 如果没有环境变量，应该返回配置的连接
        if (!databaseConfig.hasEnvConnection()) {
            assertEquals("dev_db", conn.getDatabase());
        }
    }
} 