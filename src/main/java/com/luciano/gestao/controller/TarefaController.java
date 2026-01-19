package com.luciano.gestao.controller;

import com.luciano.gestao.model.Tarefa;
import com.luciano.gestao.pagination.PagedList;
import com.luciano.gestao.MetodoExtensao.TarefaExtensao;
import com.luciano.gestao.logging.LogExecution;
import com.luciano.gestao.logging.CustomLogger;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.luciano.gestao.DTO.ProjetoDTO;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import com.luciano.gestao.unionofwork.IUnionofwork;

@RestController
@RequestMapping("/projetos")
@LogExecution(includeParameters = true, includeResult = false) // Log automático para toda a classe
@PreAuthorize("hasRole('USER')")
public class TarefaController {
    @Autowired
    private IUnionofwork unionofwork;

    @GetMapping
    @LogExecution(includeParameters = false, includeResult = true) // Log específico para este método
    public CompletableFuture<ResponseEntity<Iterable<ProjetoDTO>>> getAllProjetosAsync() {
        CustomLogger.logInfo("Iniciando busca de todos os projetos");
        
        var projetosFuture = unionofwork.GetProjetoRepository().findAllAsync()
                .thenApply(projetos -> {
                    long count = StreamSupport.stream(projetos.spliterator(), false).count();
                    CustomLogger.logInfo("Convertendo %d projetos para DTO", count);
                    
                    return StreamSupport.stream(projetos.spliterator(), false)
                            .map(projeto -> TarefaExtensao.ProjetoToDTO(projeto))
                            .collect(Collectors.toList());
                }).exceptionally(ex -> {
                    CustomLogger.logError("Erro ao buscar projetos: %s", ex.getMessage());
                    return null;
                }); 
        return projetosFuture.thenApply(ResponseEntity::ok);
    }

    // @GetMapping
    // public CompletableFuture<Iterable<Projeto>> getAllProjetosAsync() {
    // return unionofwork.GetProjetoRepository().findAllAsync();
    // }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProjetoDTO>> getProjetoByIdAsync(@PathVariable Long id) {
        return unionofwork.GetProjetoRepository().findByIdAsync(id)
                .thenApply(projeto -> TarefaExtensao.ProjetoToDTO(projeto))
                .thenApply(ResponseEntity::ok);
    }

    // @GetMapping("/{id}")
    // public CompletableFuture<Projeto> getProjetoByIdAsync(@PathVariable Long id)
    // {
    // return unionofwork.GetProjetoRepository().findByIdAsync(id);
    // }

    @PostMapping
    public CompletableFuture<ResponseEntity<ProjetoDTO>> addProjetoAsync(@RequestBody ProjetoDTO projetoDTO) {
        Tarefa projeto = TarefaExtensao.DTOtoProjeto(projetoDTO);
        return unionofwork.GetProjetoRepository().saveAsync(projeto)
                .thenApply(projetoSalvo -> TarefaExtensao.ProjetoToDTO(projetoSalvo))
                .thenApply(ResponseEntity::ok);
    }

    // @PostMapping
    // public CompletableFuture<Projeto> addProjetoAsync(@RequestBody Projeto
    // projeto) {
    // return unionofwork.GetProjetoRepository().saveAsync(projeto);
    // }


    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProjetoDTO>> updateProjetoAsync(@PathVariable Long id, @RequestBody ProjetoDTO projetoDTO) {
        Tarefa projeto = TarefaExtensao.DTOtoProjeto(projetoDTO);
        projeto.setId(id);
        return unionofwork.GetProjetoRepository().saveAsync(projeto)
                .thenApply(projetoAtualizado -> TarefaExtensao.ProjetoToDTO(projetoAtualizado))
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteProjetoAsync(@PathVariable Long id) {
        return unionofwork.GetProjetoRepository().deleteByIdAsync(id)
                .thenApply(deleted -> ResponseEntity.noContent().build());
    }

    @GetMapping("/pagination")
    public CompletableFuture<ResponseEntity<PagedList<ProjetoDTO>>> paginateProjetosAsync(@RequestParam int pageNumber,
            @RequestParam int pageSize) {

         var pagedList = unionofwork.GetProjetoRepository().paginateAsync(pageNumber, pageSize);
         return pagedList.thenApply(list -> {
             var dtoList = list.stream()
                     .map(TarefaExtensao::ProjetoToDTO)
                     .collect(Collectors.toList());
             return new PagedList<ProjetoDTO>(dtoList, list.getCurrentPage(), list.getPageSize(), pageSize);
         }).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/filter")
    public CompletableFuture<ResponseEntity<Iterable<ProjetoDTO>>> searchProjetosAsync(
            @RequestParam String campo, // nome, descricao, etc.
            @RequestParam String valor) { // texto a buscar

        // Criar predicate baseado nos parâmetros
        Predicate<Tarefa> predicate = projeto -> {
            switch (campo.toLowerCase()) {
                case "nome":
                    return projeto.getNome().toLowerCase().contains(valor.toLowerCase());
                case "orcamento":
                    return String.valueOf(projeto.getOrcamento()).equals(valor);
                case "descricao":
                    return projeto.getDescricao().toLowerCase().contains(valor.toLowerCase());
                case "status":
                    return String.valueOf(projeto.getStatus()).equalsIgnoreCase(valor);
                case "datainicio":
                    return projeto.getDataInicio() != null && projeto.getDataInicio().toString().equals(valor);
                case "datafim":
                    return projeto.getDataFim() != null && projeto.getDataFim().toString().equals(valor);
                case "id":
                    return String.valueOf(projeto.getId()).equals(valor);   
                default:
                    return projeto.getNome().toLowerCase().contains(valor.toLowerCase());
            }
        };

        var projetos =  unionofwork.GetProjetoRepository().searchAsync(predicate);
        return projetos.thenApply(list -> StreamSupport.stream(list.spliterator(), false)
                .map(TarefaExtensao::ProjetoToDTO)
                .collect(Collectors.toList())).thenApply(ResponseEntity::ok);
    }

}