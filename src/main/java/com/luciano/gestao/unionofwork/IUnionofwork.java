package com.luciano.gestao.unionofwork;

import com.luciano.gestao.repository.ITarefaRepository;

public interface IUnionofwork {
    ITarefaRepository GetTarefaRepository();
}
