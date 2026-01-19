package com.luciano.gestao.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para ativar logging automático em métodos
 * 
 * Uso:
 * @LogExecution
 * public void meuMetodo() { ... }
 * 
 * @LogExecution(includeParameters = true, includeResult = true)
 * public String outroMetodo(String param) { ... }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {
    
    /**
     * Se deve incluir os parâmetros do método no log
     */
    boolean includeParameters() default false;
    
    /**
     * Se deve incluir o resultado do método no log
     */
    boolean includeResult() default false;
    
    /**
     * Nível de log personalizado
     */
    LogLevel level() default LogLevel.INFO;
    
    /**
     * Mensagem personalizada para o log
     */
    String message() default "";
    
    /**
     * Se deve logar apenas em caso de erro
     */
    boolean onlyOnError() default false;
}