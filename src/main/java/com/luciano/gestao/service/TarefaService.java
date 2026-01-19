package com.luciano.gestao.service;


import java.util.concurrent.CompletableFuture;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;  

import com.luciano.gestao.model.Tarefa;
import com.luciano.gestao.unionofwork.IUnionofwork;        
@Service
public class TarefaService {    

    @Autowired
    private IUnionofwork unionofwork;

    public CompletableFuture<ResponseEntity<Iterable<Tarefa>>> tarefasAtrasadas(Tarefa tarefa) {

        var tarefasAtrasadas = unionofwork.GetProjetoRepository().findAllAsync()
            .thenApply(tarefas -> {
                var atrasadas = new java.util.ArrayList<Tarefa>();
                tarefas.forEach(t -> {
                    if (t.getDataFim() != null && t.getDataFim().toLocalDate().isBefore(LocalDate.now())) {
                        atrasadas.add(t);
                    }
                });
                return atrasadas;
            })
            .thenApply(atrasadas -> (Iterable<Tarefa>) atrasadas)
            .thenApply(ResponseEntity::ok);

        return tarefasAtrasadas;
    }    
}   

