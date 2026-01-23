package com.luciano.gestao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.luciano.gestao.usecases.TarefaService;

@Configuration
public class UserCasesConfig {

    @Bean
    public TarefaService tarefaServiceUseCases() {
        return new TarefaService();
    }

    
}
