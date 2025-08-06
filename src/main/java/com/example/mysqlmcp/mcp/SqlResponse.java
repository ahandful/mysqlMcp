package com.example.mysqlmcp.mcp;

import java.util.List;
import java.util.Map;

public class SqlResponse {
    private boolean success;
    private String message;
    private List<Map<String, Object>> data;
    private int affectedRows;
    private long executionTime;
    private String error;

    public SqlResponse() {}

    public SqlResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public SqlResponse(boolean success, String message, List<Map<String, Object>> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
} 