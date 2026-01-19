package com.luciano.gestao.model;

import java.sql.Date;
import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "tarefas")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "duracao_horas", nullable = false)
    private BigDecimal duracao;
    
    @Column(name = "orcamento", precision = 10, scale = 2, nullable = false)
    private BigDecimal orcamento;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "data_inicio", nullable = false)
    private Date dataInicio;
    @Column(name = "data_fim", nullable = false)
    private Date dataFim;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(BigDecimal orcamento) {
        this.orcamento = orcamento;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getDuracao() {
        return duracao;
    }
    public void setDuracao(BigDecimal duracao) {
        this.duracao = duracao;
    }

    // Construtor padrão obrigatório para JPA
    public Tarefa() {
    }

   

    public Tarefa(Long id, String nome, String descricao, BigDecimal orcamento, BigDecimal duracao, Boolean status, Date dataInicio, Date dataFim) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.orcamento = orcamento;
        this.duracao = duracao;
        this.status = status;
        this.dataInicio = dataInicio;       
        this.dataFim = dataFim;

    }





}
