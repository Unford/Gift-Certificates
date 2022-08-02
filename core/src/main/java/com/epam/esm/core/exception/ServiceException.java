package com.epam.esm.core.exception;



/**
 * It's a custom exception class that extends the Exception class and adds a custom error code
 */
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


    /**
     * This function method the error code of the exception
     *
     * @return The error code.
     */
    public CustomErrorCode getErrorCode() {
        return errorCode;
    }

}
