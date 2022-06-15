package com.epam.esm.core.model.dto;

/**
 * The type Error info.
 */
public class ErrorInfo {
    private int code;
    private String message;

    /**
     * Instantiates a new Error info.
     *
     * @param code    the code
     * @param message the message
     */
    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
