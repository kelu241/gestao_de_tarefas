package com.luciano.gestao.DTO;

public record ProjetoDTO(Long id, String nome, String descricao, String orcamento, Boolean status, String dataInicio,
        String dataFim) {
}
