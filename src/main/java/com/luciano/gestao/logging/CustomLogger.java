package com.luciano.gestao.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Utilitário para logging estruturado e personalizado
 */
@Component
public class CustomLogger {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogger.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Gera um ID único para rastreamento de requisições
     */
    public static String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Define o contexto da requisição no MDC
     */
    public static void setRequestContext(String requestId, String userId, String operation) {
        MDC.put("requestId", requestId);
        MDC.put("userId", userId != null ? userId : "anonymous");
        MDC.put("operation", operation);
        MDC.put("timestamp", LocalDateTime.now().format(formatter));
    }

    /**
     * Limpa o contexto do MDC
     */
    public static void clearContext() {
        MDC.clear();
    }

    /**
     * Log estruturado de informação
     */
    public static void logInfo(String message, Object... params) {
        logger.info(buildStructuredMessage("INFO", message, null, params));
    }

    /**
     * Log estruturado de debug
     */
    public static void logDebug(String message, Object... params) {
        logger.debug(buildStructuredMessage("DEBUG", message, null, params));
    }

    /**
     * Log estruturado de warning
     */
    public static void logWarn(String message, Object... params) {
        logger.warn(buildStructuredMessage("WARN", message, null, params));
    }

    /**
     * Log estruturado de erro
     */
    public static void logError(String message, Throwable throwable, Object... params) {
        logger.error(buildStructuredMessage("ERROR", message, throwable, params));
    }

    /**
     * Log de entrada de método
     */
    public static void logMethodEntry(String className, String methodName, Object[] parameters) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "METHOD_ENTRY");
        logData.put("class", className);
        logData.put("method", methodName);
        logData.put("parameters", parameters);

        logger.info(toJson(logData));
    }

    /**
     * Log de saída de método
     */
    public static void logMethodExit(String className, String methodName, Object result, long executionTime) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "METHOD_EXIT");
        logData.put("class", className);
        logData.put("method", methodName);
        logData.put("result", result);
        logData.put("executionTimeMs", executionTime);

        logger.info(toJson(logData));
    }

    /**
     * Log de exceção em método
     */
    public static void logMethodException(String className, String methodName, Throwable throwable,
            long executionTime) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "METHOD_EXCEPTION");
        logData.put("class", className);
        logData.put("method", methodName);
        logData.put("exception", throwable.getClass().getSimpleName());
        logData.put("message", throwable.getMessage());
        logData.put("executionTimeMs", executionTime);

        logger.error(toJson(logData), throwable);
    }

    /**
     * Log de requisição HTTP
     */
    public static void logHttpRequest(String method, String uri, String userAgent, String remoteAddr) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "HTTP_REQUEST");
        logData.put("httpMethod", method);
        logData.put("uri", uri);
        logData.put("userAgent", userAgent);
        logData.put("remoteAddr", remoteAddr);

        logger.info(toJson(logData));
    }

    /**
     * Log de resposta HTTP
     */
    public static void logHttpResponse(int status, long executionTime) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "HTTP_RESPONSE");
        logData.put("statusCode", status);
        logData.put("executionTimeMs", executionTime);

        logger.info(toJson(logData));
    }

    /**
     * Constrói mensagem estruturada
     */
    private static String buildStructuredMessage(String level, String message, Throwable throwable, Object... params) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("level", level);
        logData.put("message", String.format(message, params));
        logData.put("timestamp", LocalDateTime.now().format(formatter));

        if (throwable != null) {
            logData.put("exception", throwable.getClass().getSimpleName());
            logData.put("exceptionMessage", throwable.getMessage());
        }

        // Adiciona contexto do MDC se disponível
        String requestId = MDC.get("requestId");
        if (requestId != null) {
            logData.put("requestId", requestId);
        }

        String userId = MDC.get("userId");
        if (userId != null) {
            logData.put("userId", userId);
        }

        String operation = MDC.get("operation");
        if (operation != null) {
            logData.put("operation", operation);
        }

        return toJson(logData);
    }

    /**
     * Converte objeto para JSON
     */
    private static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{\"error\":\"Failed to serialize log message\",\"original\":\"" + obj.toString() + "\"}";
        }
    }

    public static void logError(String string, String message) {
        // TODO Auto-generated method stub
        logger.error(buildStructuredMessage("ERROR", String.format(string, message), null));
    }
}