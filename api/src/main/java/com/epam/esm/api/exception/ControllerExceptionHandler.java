package com.epam.esm.api.exception;

import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.ErrorInfo;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Controller exception handler.
 */
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    /**
     * Instantiates a new Controller exception handler.
     *
     * @param messageSource the message source
     */
    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        CustomErrorCode errorCode = CustomErrorCode.HANDLER_NOT_FOUND;
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(errorCode.getMessage(),
                new String[]{request.getDescription(false)}, locale);
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), message);
        return new ResponseEntity<>(errorInfo, errorCode.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        CustomErrorCode errorCode = CustomErrorCode.METHOD_NOT_SUPPORTED;
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(errorCode.getMessage(),
                new String[]{ex.getMethod()}, locale);
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), message);
        return new ResponseEntity<>(errorInfo, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        CustomErrorCode errorCode = CustomErrorCode.MESSAGE_NOT_READABLE;
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(errorCode.getMessage(), null, locale);
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), message);
        return new ResponseEntity<>(errorInfo, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        CustomErrorCode errorCode = CustomErrorCode.CONSTRAINT_VIOLATION;
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe == null ? "[null]" : fe.getField()
                        + ":[" + fe.getRejectedValue() + "]: "
                        + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), errorMessage);
        return new ResponseEntity<>(errorInfo, errorCode.getHttpStatus());
    }

    /**
     * Handle service exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorInfo> handleServiceException(ServiceException ex) {
        CustomErrorCode errorCode = ex.getErrorCode();
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(errorCode.getMessage(), new Object[]{ex.getMessage()}, locale);
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), message);
        return new ResponseEntity<>(errorInfo, errorCode.getHttpStatus());
    }

    /**
     * Handle method argument type mismatch exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        CustomErrorCode errorCode = CustomErrorCode.TYPE_MISMATCH;
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(errorCode.getMessage(),
                new Object[]{ex.getParameter().getParameterName(),
                        ex.getParameter().getParameter().getType().getName()},
                locale);
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), message);
        return new ResponseEntity<>(errorInfo, errorCode.getHttpStatus());
    }

    /**
     * Handle constraint violation exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorInfo> handleConstraintViolationException(ConstraintViolationException ex) {
        CustomErrorCode errorCode = CustomErrorCode.CONSTRAINT_VIOLATION;
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        String message = constraintViolations.stream()
                .map(cv -> cv == null ? "[null]" : "[" + cv.getInvalidValue() + "]: " + cv.getMessage())
                .collect(Collectors.joining("; "));
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), message);
        return new ResponseEntity<>(errorInfo, errorCode.getHttpStatus());
    }

    /**
     * Handle exception internal response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex) {
        CustomErrorCode errorCode = CustomErrorCode.INTERNAL_EXCEPTION;
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(errorCode.getMessage(), null, locale);
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getCode(), message);
        return new ResponseEntity<>(errorInfo, errorCode.getHttpStatus());
    }

}
