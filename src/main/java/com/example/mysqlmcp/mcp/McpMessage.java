package com.example.mysqlmcp.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class McpMessage {
    @JsonProperty("jsonrpc")
    private String jsonrpc = "2.0";
    
    private String id;
    private String method;
    private Object params;
    private Object result;
    private Object error;

    public McpMessage() {}

    public McpMessage(String method, Object params) {
        this.method = method;
        this.params = params;
    }

    public McpMessage(String id, String method, Object params) {
        this.id = id;
        this.method = method;
        this.params = params;
    }

    // Getters and Setters
    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
} 