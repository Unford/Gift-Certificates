package com.epam.esm.core.exception;

import org.springframework.http.HttpStatus;

/**
 * The enum Custom error code.
 */
public enum CustomErrorCode {
    /**
     * Resource not found custom error code.
     */
    RESOURCE_NOT_FOUND(40401, "exception.resource.not.found.message", HttpStatus.NOT_FOUND),
    /**
     * Handler not found custom error code.
     */
    HANDLER_NOT_FOUND(40402, "exception.handler.not.found.message", HttpStatus.NOT_FOUND),
    /**
     * Constraint violation custom error code.
     */
    CONSTRAINT_VIOLATION(40000, HttpStatus.BAD_REQUEST),
    /**
     * Type mismatch custom error code.
     */
    TYPE_MISMATCH(40001, "exception.type.mismatch.message", HttpStatus.BAD_REQUEST),
    /**
     * Method not supported custom error code.
     */
    METHOD_NOT_SUPPORTED(40505,"exception.method.not.allowed.message", HttpStatus.METHOD_NOT_ALLOWED),
    /**
     * Forbidden operation custom error code.
     */
    CONFLICT_DELETE(40300, "exception.conflict.delete.operation.message", HttpStatus.CONFLICT),
    /**
     * Resource already exist custom error code.
     */
    RESOURCE_ALREADY_EXIST(40909, "exception.resource.already.exist.message", HttpStatus.CONFLICT),
    /**
     * Internal exception custom error code.
     */
    INTERNAL_EXCEPTION(50000, "exception.internal.server.message", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * Message not readable custom error code.
     */
    MESSAGE_NOT_READABLE(40001, "exception.message.not.readable.message", HttpStatus.BAD_REQUEST);

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = "%s";
    }

    CustomErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
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
     * Gets http status.
     *
     * @return the http status
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
