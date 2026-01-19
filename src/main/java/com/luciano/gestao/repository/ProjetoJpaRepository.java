package com.luciano.gestao.repository;

import com.luciano.gestao.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoJpaRepository extends JpaRepository<Tarefa, Long> {
    // Spring Data cria automaticamente a implementação
}