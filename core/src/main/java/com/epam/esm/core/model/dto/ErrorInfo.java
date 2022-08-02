package com.epam.esm.core.model.dto;

/**
 * It's a simple POJO that contains two properties: code and message
 */
public class ErrorInfo {
    private int code;
    private String message;

    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
