package com.luciano.gestao.usecases;

import java.util.concurrent.CompletableFuture;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.luciano.gestao.model.Tarefa;
import com.luciano.gestao.unionofwork.IUnionofwork;

@Service
public class TarefaService implements ITarefaService {

    @Autowired
    private IUnionofwork unionofwork;

    /**
     * Remove tarefas repetidas da mesma lista, considerando o campo {@code id}.
     * Mantém a ordem original.
     *
     * Observação: como {@code Tarefa} não implementa {@code equals/hashCode},
     * esta é a forma mais segura de deduplicar quando os objetos vêm do banco.
     */
   

    public CompletableFuture<ResponseEntity<Iterable<Tarefa>>> tarefasAtrasadas() {

        var tarefasAtrasadas = unionofwork.GetTarefaRepository().findAllAsync()
                .thenApply(tarefas -> {
                    // Se precisar deduplicar antes de filtrar:
                    // tarefas = diferentesPorId(tarefas);
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

    public CompletableFuture<ResponseEntity<Iterable<Tarefa>>> tarefasConcluidas() {

        var tarefasConcluidas = unionofwork.GetTarefaRepository().findAllAsync()
                .thenApply(tarefas -> {
                    // Se precisar deduplicar antes de filtrar:
                    // tarefas = diferentesPorId(tarefas);
                    var concluidas = new java.util.ArrayList<Tarefa>();
                    tarefas.forEach(t -> {
                        if (isConcluida(t)) {
                            concluidas.add(t);
                        }
                    });
                    return concluidas;
                })
                .thenApply(concluidas -> (Iterable<Tarefa>) concluidas)
                .thenApply(ResponseEntity::ok);

        return tarefasConcluidas;

    }

    public boolean isConcluida(Tarefa tarefa) {
        return "concluida".equalsIgnoreCase(tarefa.getStatus());
    }






}