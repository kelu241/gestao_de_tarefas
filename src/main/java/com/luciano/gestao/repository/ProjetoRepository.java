package com.luciano.gestao.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luciano.gestao.model.Tarefa;



@Repository
public class ProjetoRepository extends GenericRepository<Tarefa> implements IProjetoRepository {

    @Autowired
    private ProjetoJpaRepository projetoJpaRepository;

    @Override
    protected JpaRepository<Tarefa, Long> getRepository() {
        return projetoJpaRepository;
    }

}

