package com.luciano.gestao.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptador para logar automaticamente todas as requisições HTTP
 */
@Component
public class HttpRequestLoggingInterceptor implements HandlerInterceptor {
    
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        String requestId = CustomLogger.generateRequestId();
        
        // Armazena informações no request para uso posterior
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
        
        // Define contexto do MDC
        CustomLogger.setRequestContext(requestId, extractUserId(request), extractOperation(request));
        
        // Log da requisição
        CustomLogger.logHttpRequest(
            request.getMethod(),
            request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""),
            request.getHeader("User-Agent"),
            getRemoteAddress(request)
        );
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        
        if (startTime != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log da resposta
            CustomLogger.logHttpResponse(response.getStatus(), executionTime);
            
            // Se houver exceção, loga também
            if (ex != null) {
                CustomLogger.logError("Exceção não tratada na requisição", ex);
            }
        }
        
        // Limpa o contexto do MDC
        CustomLogger.clearContext();
    }
    
    /**
     * Extrai o ID do usuário da requisição (implementar conforme sua autenticação)
     */
    private String extractUserId(HttpServletRequest request) {
        // Implementar conforme seu sistema de autenticação
        // Exemplos:
        // - return SecurityContextHolder.getContext().getAuthentication().getName();
        // - return request.getHeader("X-User-ID");
        // - return extractFromJWT(request);
        
        return request.getHeader("X-User-ID"); // Placeholder
    }
    
    /**
     * Extrai a operação sendo executada da requisição
     */
    private String extractOperation(HttpServletRequest request) {
        return request.getMethod() + " " + request.getRequestURI();
    }
    
    /**
     * Obtém o endereço IP real do cliente
     */
    private String getRemoteAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
}