package ru.neoflex.deal.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* ru.neoflex.deal.service..*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* ru.neoflex.deal.service..*(..)) ")
    public void serviceMethods() {}

    @Pointcut("controllerMethods() || serviceMethods()")
    public void combinedPointcut() {}

    @Before("combinedPointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Method {} called with arguments: {}", methodName, args);
    }

    @AfterReturning(pointcut = "combinedPointcut()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} returned with result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "combinedPointcut()", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Method {} thrown exception: {}", methodName, exception.getMessage());
    }
}
