package com.epam.esm.core.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * It's an aspect that logs method calls and their arguments, and the return value or exception thrown
 */
@Aspect
@Component
public class Logging {
    private static final Logger logger = LogManager.getLogger();

    @Pointcut("execution(* com.epam.esm.core.*.*.*(..))")
    public void executeLogging() {}

    /**
     * It logs the method name, arguments, return value, and any exceptions that are thrown
     *
     * @param joinPoint The join point is the method that is being intercepted.
     * @return The return value of the method being called.
     */
    @Around("executeLogging()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder builder = new StringBuilder("Method: ")
                .append(joinPoint.getSignature().getDeclaringTypeName());
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            builder.append(" args=[ ");
            Arrays.stream(args).forEach(arg -> builder.append(arg).append(" | "));
            builder.append("]");
        }
        try {
            Object returnValue = joinPoint.proceed();
            builder.append(", returning: ").append(returnValue);

            logger.info(builder);
            return returnValue;
        } catch (Throwable e) {
            builder.append(", error: ").append(e.getClass())
                    .append(" ").append(e.getMessage());
            logger.error(builder.toString());
            throw e;
        }
    }

}
