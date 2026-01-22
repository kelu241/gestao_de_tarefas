package com.luciano.gestao.usecases;


import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;

import com.luciano.gestao.model.Tarefa;

public interface ITarefaService {
    CompletableFuture<ResponseEntity<Iterable<Tarefa>>> tarefasAtrasadas();
    CompletableFuture<ResponseEntity<Iterable<Tarefa>>> tarefasConcluidas();
    
}
