package com.luciano.gestao.controller;

import com.luciano.gestao.model.Tarefa;
import com.luciano.gestao.pagination.PagedList;
import com.luciano.gestao.DTO.TarefaDTO;
import com.luciano.gestao.MetodoExtensao.TarefaExtensao;
import com.luciano.gestao.logging.LogExecution;
import com.luciano.gestao.logging.CustomLogger;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import com.luciano.gestao.unionofwork.IUnionofwork;
import com.luciano.gestao.usecases.ITarefaService;
import com.luciano.gestao.usecases.TarefaService;

@RestController
@RequestMapping("/tarefas")
@LogExecution(includeParameters = true, includeResult = false) // Log automático para toda a classe
@PreAuthorize("hasRole('USER')")
public class TarefaController {
    @Autowired
    private IUnionofwork unionofwork;

    @Autowired
    private ITarefaService tarefaService;

  

    @GetMapping
    @LogExecution(includeParameters = false, includeResult = true) // Log específico para este método
    public CompletableFuture<ResponseEntity<Iterable<TarefaDTO>>> getAllTarefasAsync() {
        CustomLogger.logInfo("Iniciando busca de todos os Tarefas");

        var TarefasFuture = unionofwork.GetTarefaRepository().findAllAsync()
                .thenApply(Tarefas -> {
                    long count = StreamSupport.stream(Tarefas.spliterator(), false).count();
                    CustomLogger.logInfo("Convertendo %d Tarefas para DTO", count);

                    return StreamSupport.stream(Tarefas.spliterator(), false)
                            .map(Tarefa -> TarefaExtensao.TarefaToDTO(Tarefa))
                            .collect(Collectors.toList());
                }).exceptionally(ex -> {
                    CustomLogger.logError("Erro ao buscar Tarefas: %s", ex.getMessage());
                    return null;
                });
        return TarefasFuture.thenApply(ResponseEntity::ok);
    }

    // @GetMapping
    // public CompletableFuture<Iterable<Tarefa>> getAllTarefasAsync() {
    // return unionofwork.GetTarefaRepository().findAllAsync();
    // }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<TarefaDTO>> getTarefaByIdAsync(@PathVariable Long id) {
        return unionofwork.GetTarefaRepository().findByIdAsync(id)
                .thenApply(Tarefa -> TarefaExtensao.TarefaToDTO(Tarefa))
                .thenApply(ResponseEntity::ok);
    }

    // @GetMapping("/{id}")
    // public CompletableFuture<Tarefa> getTarefaByIdAsync(@PathVariable Long id)
    // {
    // return unionofwork.GetTarefaRepository().findByIdAsync(id);
    // }

    @PostMapping
    public CompletableFuture<ResponseEntity<TarefaDTO>> addTarefaAsync(@RequestBody TarefaDTO TarefaDTO) {
        Tarefa Tarefa = TarefaExtensao.DTOtoTarefa(TarefaDTO);
        return unionofwork.GetTarefaRepository().saveAsync(Tarefa)
                .thenApply(TarefaSalvo -> TarefaExtensao.TarefaToDTO(TarefaSalvo))
                .thenApply(ResponseEntity::ok);
    }

    // @PostMapping
    // public CompletableFuture<Tarefa> addTarefaAsync(@RequestBody Tarefa
    // Tarefa) {
    // return unionofwork.GetTarefaRepository().saveAsync(Tarefa);
    // }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<TarefaDTO>> updateTarefaAsync(@PathVariable Long id,
            @RequestBody TarefaDTO TarefaDTO) {
        Tarefa Tarefa = TarefaExtensao.DTOtoTarefa(TarefaDTO);
        Tarefa.setId(id);
        return unionofwork.GetTarefaRepository().saveAsync(Tarefa)
                .thenApply(TarefaAtualizado -> TarefaExtensao.TarefaToDTO(TarefaAtualizado))
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteTarefaAsync(@PathVariable Long id) {
        return unionofwork.GetTarefaRepository().deleteByIdAsync(id)
                .thenApply(deleted -> ResponseEntity.noContent().build());
    }

    @GetMapping("/pagination")
    public CompletableFuture<ResponseEntity<PagedList<TarefaDTO>>> paginateTarefasAsync(@RequestParam int pageNumber,
            @RequestParam int pageSize) {

        var pagedList = unionofwork.GetTarefaRepository().paginateAsync(pageNumber, pageSize);
        return pagedList.thenApply(list -> {
            var dtoList = list.stream()
                    .map(TarefaExtensao::TarefaToDTO)
                    .collect(Collectors.toList());
            return new PagedList<TarefaDTO>(dtoList, list.getCurrentPage(), list.getPageSize(), pageSize);
        }).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/filter")
    public CompletableFuture<ResponseEntity<Iterable<TarefaDTO>>> searchTarefasAsync(
            @RequestParam String campo, // nome, descricao, etc.
            @RequestParam String valor) { // texto a buscar

        // Criar predicate baseado nos parâmetros
        Predicate<Tarefa> predicate = Tarefa -> {
            switch (campo.toLowerCase()) {
                case "nome":
                    return Tarefa.getNome().toLowerCase().contains(valor.toLowerCase());
                case "orcamento":
                    return String.valueOf(Tarefa.getOrcamento()).equals(valor);
                case "descricao":
                    return Tarefa.getDescricao().toLowerCase().contains(valor.toLowerCase());
                case "status":
                    return String.valueOf(Tarefa.getStatus()).equalsIgnoreCase(valor);
                case "datainicio":
                    return Tarefa.getDataInicio() != null && Tarefa.getDataInicio().toString().equals(valor);
                case "datafim":
                    return Tarefa.getDataFim() != null && Tarefa.getDataFim().toString().equals(valor);
                case "id":
                    return String.valueOf(Tarefa.getId()).equals(valor);
                default:
                    return Tarefa.getNome().toLowerCase().contains(valor.toLowerCase());
            }
        };

        var Tarefas = unionofwork.GetTarefaRepository().searchAsync(predicate);
        return Tarefas.thenApply(list -> StreamSupport.stream(list.spliterator(), false)
                .map(TarefaExtensao::TarefaToDTO)
                .collect(Collectors.toList())).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/atrasadas")
    public CompletableFuture<ResponseEntity<Iterable<Tarefa>>> getTarefasAtrasadas() {
       
        return tarefaService.tarefasAtrasadas();

    }

    @GetMapping("/concluidas")
    public CompletableFuture<ResponseEntity<Iterable<Tarefa>>> getTarefasConcluidas() {      
        return tarefaService.tarefasConcluidas();
    }

}