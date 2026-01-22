package com.luciano.gestao.unionofwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luciano.gestao.repository.ITarefaRepository;
import com.luciano.gestao.repository.TarefaRepository;

@Repository

public class Unionofwork implements IUnionofwork {
    @Autowired
    private ITarefaRepository _TarefaRepository;

    @Override
    public ITarefaRepository GetTarefaRepository() {
        // TODO Auto-generated method stub
        return (_TarefaRepository != null)?_TarefaRepository:new TarefaRepository();
    }

}
