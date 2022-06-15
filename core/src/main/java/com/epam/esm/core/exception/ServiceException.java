package com.epam.esm.core.exception;


/**
 * The type Service exception.
 */
public class ServiceException extends Exception {
    private final CustomErrorCode errorCode;


    /**
     * Instantiates a new Service exception.
     *
     * @param message   the message
     * @param errorCode the error code
     */
    public ServiceException(String message, CustomErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Service exception.
     *
     * @param message   the message
     * @param errorCode the error code
     * @param throwable the throwable
     */
    public ServiceException(String message, CustomErrorCode errorCode, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    /**
     * Gets error code.
     *
     * @return the error code
     */
    public CustomErrorCode getErrorCode() {
        return errorCode;
    }

}
