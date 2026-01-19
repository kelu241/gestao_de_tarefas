package com.luciano.gestao.logging;

import org.springframework.stereotype.Component;

/**
 * Classe de exemplo demonstrando diferentes formas de usar o sistema de logging personalizado
 */
@Component
public class LoggingExampleService {
    
    /**
     * Exemplo 1: Logging manual básico
     */
    public void exemploLoggingManual() {
        CustomLogger.logInfo("Executando operação de exemplo");
        
        try {
            // Simula algum processamento
            Thread.sleep(100);
            CustomLogger.logDebug("Processamento executado com sucesso");
            
        } catch (Exception e) {
            CustomLogger.logError("Erro durante processamento", e);
        }
    }
    
    /**
     * Exemplo 2: Logging automático apenas em caso de erro
     */
    @LogExecution(onlyOnError = true)
    public String exemploLoggingApenasErro(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input não pode ser nulo");
        }
        return "Processado: " + input;
    }
    
    /**
     * Exemplo 3: Logging automático com parâmetros e resultado
     */
    @LogExecution(includeParameters = true, includeResult = true)
    public String exemploLoggingCompleto(String nome, int idade) {
        CustomLogger.logInfo("Processando dados do usuário: %s", nome);
        
        // Simula algum processamento
        String resultado = String.format("Usuário %s tem %d anos", nome, idade);
        
        CustomLogger.logDebug("Resultado gerado: %s", resultado);
        return resultado;
    }
    
    /**
     * Exemplo 4: Logging com contexto personalizado
     */
    public void exemploLoggingComContexto(String userId, String operation) {
        // Define contexto personalizado
        CustomLogger.setRequestContext(
            CustomLogger.generateRequestId(), 
            userId, 
            operation
        );
        
        try {
            CustomLogger.logInfo("Iniciando operação %s para usuário %s", operation, userId);
            
            // Simula processamento
            Thread.sleep(50);
            
            CustomLogger.logInfo("Operação %s concluída com sucesso", operation);
            
        } catch (Exception e) {
            CustomLogger.logError("Falha na operação %s", e, operation);
        } finally {
            // Limpa contexto
            CustomLogger.clearContext();
        }
    }
    
    /**
     * Exemplo 5: Método que pode gerar exceção (para testar logging de erro)
     */
    @LogExecution(includeParameters = true)
    public void exemploComExcecao(boolean gerarErro) {
        CustomLogger.logInfo("Parâmetro gerarErro: %s", gerarErro);
        
        if (gerarErro) {
            throw new RuntimeException("Erro simulado para teste de logging");
        }
        
        CustomLogger.logInfo("Método executado sem erros");
    }
}