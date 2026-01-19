package com.luciano.gestao.unionofwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luciano.gestao.repository.IProjetoRepository;
import com.luciano.gestao.repository.ProjetoRepository;

@Repository

public class Unionofwork implements IUnionofwork {
    @Autowired
    private IProjetoRepository _projetoRepository;

    @Override
    public IProjetoRepository GetProjetoRepository() {
        // TODO Auto-generated method stub
        return (_projetoRepository != null)?_projetoRepository:new ProjetoRepository();
    }

}
