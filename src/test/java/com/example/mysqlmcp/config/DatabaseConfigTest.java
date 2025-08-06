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
    public void testDefaultConnection() {
        DatabaseConfig.DatabaseConnection defaultConn = databaseConfig.getDefaultConnection();
        assertNotNull(defaultConn);
        assertEquals("localhost", defaultConn.getHost());
        assertEquals(3306, defaultConn.getPort());
        assertEquals("test", defaultConn.getDatabase());
        assertEquals("root", defaultConn.getUsername());
        assertEquals("password", defaultConn.getPassword());
    }

    @Test
    public void testNamedConnections() {
        DatabaseConfig.DatabaseConnection devConn = databaseConfig.getConnection("dev");
        assertNotNull(devConn);
        assertEquals("localhost", devConn.getHost());
        assertEquals("dev_db", devConn.getDatabase());
        assertEquals("dev_user", devConn.getUsername());

        DatabaseConfig.DatabaseConnection testConn = databaseConfig.getConnection("test");
        assertNotNull(testConn);
        assertEquals("test-server", testConn.getHost());
        assertEquals("test_db", testConn.getDatabase());
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
    }

    @Test
    public void testGetConnectionWithNullName() {
        DatabaseConfig.DatabaseConnection conn = databaseConfig.getConnection(null);
        assertNotNull(conn);
        assertEquals(databaseConfig.getDefaultConnection(), conn);
    }

    @Test
    public void testGetConnectionWithEmptyName() {
        DatabaseConfig.DatabaseConnection conn = databaseConfig.getConnection("");
        assertNotNull(conn);
        assertEquals(databaseConfig.getDefaultConnection(), conn);
    }
} 