// Factory function para criar tarefas
// Interface para Tarefa baseada no entity Java
export const createTarefa = (dados = {}) => ({
  id: dados.id || null,
  nome: dados.nome || '',
  descricao: dados.descricao || '',
  duracao: dados.duracao || 0, // BigDecimal -> number
  orcamento: dados.orcamento || 0, // BigDecimal -> number
  status: dados.status || '',
  dataInicio: dados.dataInicio ? new Date(dados.dataInicio) : null,
  dataFim: dados.dataFim ? new Date(dados.dataFim) : null
});

// Tipo para Tarefa
export interface Tarefa {
  id: number | null;
  nome: string;
  descricao: string;
  duracao: number; // horas
  orcamento: number;
  status: string;
  dataInicio: Date | null;
  dataFim: Date | null;
}

