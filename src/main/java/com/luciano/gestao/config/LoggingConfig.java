package com.luciano.gestao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuração para habilitar AspectJ (necessário para logging automático)
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingConfig {
    
}