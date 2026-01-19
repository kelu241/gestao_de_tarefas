package com.luciano.gestao.config;

import com.luciano.gestao.logging.HttpRequestLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServiceConfig implements WebMvcConfigurer {
    
    @Autowired
    private HttpRequestLoggingInterceptor httpRequestLoggingInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpRequestLoggingInterceptor)
                .addPathPatterns("/api/**") // Aplica apenas às rotas da API
                .excludePathPatterns("/api/health", "/api/actuator/**"); // Exclui endpoints de health check
    }
}