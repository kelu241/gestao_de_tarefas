package com.luciano.gestao.unionofwork;

import com.luciano.gestao.repository.IProjetoRepository;

public interface IUnionofwork {
    IProjetoRepository GetProjetoRepository();
}
