package com.epam.esm.core.exception;


public class ServiceException extends Exception {
    private final CustomErrorCode errorCode;


    public ServiceException(String message, CustomErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, CustomErrorCode errorCode, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public CustomErrorCode getErrorCode() {
        return errorCode;
    }

}
