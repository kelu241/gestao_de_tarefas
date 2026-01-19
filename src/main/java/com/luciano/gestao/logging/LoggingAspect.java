package com.luciano.gestao.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect para interceptar métodos anotados com @LogExecution
 * e realizar logging automático
 */
@Aspect
@Component
public class LoggingAspect {
    
    @Around("@annotation(LogExecution)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogExecution logAnnotation = method.getAnnotation(LogExecution.class);
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Log de entrada (se não for apenas em caso de erro)
            if (!logAnnotation.onlyOnError()) {
                if (logAnnotation.includeParameters()) {
                    CustomLogger.logMethodEntry(className, methodName, args);
                } else {
                    CustomLogger.logMethodEntry(className, methodName, new Object[]{"[parameters hidden]"});
                }
            }
            
            // Executa o método
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log de saída (se não for apenas em caso de erro)
            if (!logAnnotation.onlyOnError()) {
                if (logAnnotation.includeResult()) {
                    CustomLogger.logMethodExit(className, methodName, result, executionTime);
                } else {
                    CustomLogger.logMethodExit(className, methodName, "[result hidden]", executionTime);
                }
            }
            
            return result;
            
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log de exceção
            CustomLogger.logMethodException(className, methodName, throwable, executionTime);
            
            throw throwable;
        }
    }
    
    /**
     * Aspect para classes inteiras anotadas com @LogExecution
     */
    @Around("@within(LogExecution)")
    public Object logClassExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        
        Class<?> targetClass = joinPoint.getTarget().getClass();
        LogExecution logAnnotation = targetClass.getAnnotation(LogExecution.class);
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = targetClass.getSimpleName();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Log de entrada
            if (!logAnnotation.onlyOnError()) {
                if (logAnnotation.includeParameters()) {
                    CustomLogger.logMethodEntry(className, methodName, args);
                } else {
                    CustomLogger.logMethodEntry(className, methodName, new Object[]{"[parameters hidden]"});
                }
            }
            
            // Executa o método
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log de saída
            if (!logAnnotation.onlyOnError()) {
                if (logAnnotation.includeResult()) {
                    CustomLogger.logMethodExit(className, methodName, result, executionTime);
                } else {
                    CustomLogger.logMethodExit(className, methodName, "[result hidden]", executionTime);
                }
            }
            
            return result;
            
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log de exceção
            CustomLogger.logMethodException(className, methodName, throwable, executionTime);
            
            throw throwable;
        }
    }
}