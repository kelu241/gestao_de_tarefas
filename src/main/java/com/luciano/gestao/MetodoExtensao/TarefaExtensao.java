package com.luciano.gestao.MetodoExtensao;

import com.luciano.gestao.DTO.TarefaDTO;
import com.luciano.gestao.model.Tarefa;

public  class TarefaExtensao {

    public static Tarefa DTOtoTarefa(TarefaDTO dto) {
        return new Tarefa(dto.id(), dto.nome(), dto.descricao(), new java.math.BigDecimal(dto.orcamento()), java.math.BigDecimal.ZERO,
            dto.status(), java.sql.Date.valueOf(dto.dataInicio()), java.sql.Date.valueOf(dto.dataFim()));
    }   



    public static TarefaDTO TarefaToDTO(Tarefa tarefa) {
        return new TarefaDTO(tarefa.getId(), tarefa.getNome(), tarefa.getDescricao(), tarefa.getOrcamento().toString(), tarefa.getStatus(),
                tarefa.getDataInicio().toString(), tarefa.getDataFim().toString());
    }




    }
