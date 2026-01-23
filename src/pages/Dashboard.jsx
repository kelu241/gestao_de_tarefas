import DataTable from 'react-data-table-component';
import { useState, useEffect } from 'react';
import { TarefaService } from '../services/TarefaService';

const Dashboard = () => {
  const [loading, setLoading] = useState(false);
  const [apiData, setApiData] = useState(null);
  const [tarefasAtrasadas, setTarefasAtrasadas] = useState(0);
  const [tarefasConcluidas, setTarefasConcluidas] = useState(0);

  // Debug: Log sempre que apiData muda  
  console.log('🔄 Dashboard re-renderizou. apiData:', apiData ? `${apiData.length} itens` : 'null', 'loading:', loading);

  // ✅ useEffect simplificado - sempre busca dados frescos
  useEffect(() => {
    console.log('🚀 useEffect executado - buscando dados frescos');
    let tarefasLoaded = false;
    let atrasadasLoaded = false;

    const checkLoading = () => {
      if (tarefasLoaded && atrasadasLoaded) {
        setLoading(false);
      }
    };


    setLoading(true);

    // Criar Observable
    const testeApi = TarefaService(
      'http://localhost:8080/api/tarefas',
      'GET',
      { 'Content-Type': 'application/json' },
      null
    );

    const atrasadas = TarefaService(
      'http://localhost:8080/api/tarefas/atrasadas',
      'GET',
      { 'Content-Type': 'application/json' },
      null
    );

    const concluidas = TarefaService(
      'http://localhost:8080/api/tarefas/concluidas',
      'GET',
      { 'Content-Type': 'application/json' },
      null
    );

    // Subscribe com referência para cleanup
    const subscription = testeApi.subscribe({
      next: (data) => {
        console.log('🎉 Dados recebidos da API:', data);
        console.log('📊 Tipo dos dados:', typeof data);
        console.log('📋 É array?', Array.isArray(data));
        console.log('📏 Quantidade de itens:', data?.length || 'N/A');

        // Atualizar com os dados recebidos
        if (data) {
          console.log('✅ Atualizando estado com dados frescos');
          setApiData(data);
          tarefasLoaded = true;
          checkLoading();
        } else {
          console.log('⚠️ Dados vazios ou nulos');
          setApiData([]);
        }

      },
      error: (err) => {
        console.error('❌ Erro na API:', err);
        console.error('🔍 Status do erro:', err.message);
        setApiData([]);
        checkLoading();
      }
    });


    const atrasadasSubscription = atrasadas.subscribe({
      next: (data) => {
        console.log('🎉 Tarefas atrasadas recebidas:', data);
        setTarefasAtrasadas(data);
        atrasadasLoaded = true;
        checkLoading();

      },
      error: (err) => {
        console.error('❌ Erro ao buscar tarefas atrasadas:', err);
        checkLoading();
      }
    });

    const concluidasSubscription = concluidas.subscribe({
      next: (data) => {
        console.log('🎉 Tarefas concluídas recebidas:', data);
        setTarefasConcluidas(data);
        concluidasLoaded = true;
        checkLoading();

      },
      error: (err) => {
        console.error('❌ Erro ao buscar tarefas concluídas:', err);
        checkLoading();
      }
    });

    // ✅ CLEANUP - MUITO IMPORTANTE!
    return () => {
      console.log('🧹 Cleanup executado - desmontando componente');
      subscription.unsubscribe();
      atrasadasSubscription.unsubscribe();
      concluidasSubscription.unsubscribe();
      console.log('Observable desconectado - sem vazamento!');
    };
  }, []); // ← Array vazio = executa só uma vez



  // Configuração das colunas
  const colunas = [
    {
      name: 'Id',
      selector: row => row.id,
      sortable: true,
      width: '120px',
    },
    {
      name: 'Nome',
      selector: row => row.nome,
      sortable: true,
      width: '150px',
    },
    {
      name: 'Orçamento',
      selector: row => row.orcamento,
      sortable: true,
      width: '150px',
    },
    {
      name: 'status',
      selector: row => row.dataInicio,
      sortable: true,
      width: '150px',
    },
    {
      name: 'Data de Início',
      selector: row => row.dataInicio,
      sortable: true,
      width: '150px',
    },
    {
      name: 'Data de Término',
      selector: row => row.dataFim,
      sortable: true,
      width: '150px',
    },
    {
      name: 'Descrição',
      selector: row => row.descricao,
      sortable: true,
      width: '200px',
    },
    // {
    //   name: 'Status',
    //   cell: row => (
    //     <span className={`badge ${row.status === 'success' ? 'bg-success' : 'bg-warning'}`}>
    //       {row.statusTexto}
    //     </span>
    //   ),
    //   width: '120px',
    // },
  ];

  // Estilos para combinar com Tabler
  const estilosTabler = {
    table: {
      style: {
        backgroundColor: 'transparent',
      },
    },
    headRow: {
      style: {
        backgroundColor: 'var(--tblr-bg-surface)',
        borderBottomColor: 'var(--tblr-border-color)',
        minHeight: '56px',
      },
    },
    headCells: {
      style: {
        color: 'var(--tblr-body-color)',
        fontSize: '0.875rem',
        fontWeight: '600',
        textTransform: 'uppercase',
        letterSpacing: '0.025em',
      },
    },
    rows: {
      style: {
        backgroundColor: 'transparent',
        borderBottomColor: 'var(--tblr-border-color)',
        minHeight: '48px',
        '&:hover': {
          backgroundColor: 'var(--tblr-bg-surface-secondary)',
        },
      },
    },
  };

  // ❌ CÓDIGO REMOVIDO - CAUSAVA BUG
  // Este useEffect estava resetando apiData para seu valor inicial (null)
  // useEffect(() => {
  //   setLoading(true);
  //   setTimeout(() => {
  //     setApiData(apiData); // ← ESTE ERA O PROBLEMA!
  //     setLoading(false);
  //   }, 500);
  // }, []);
  return (

    <>
      {/* Row para as métricas */}
      <div className="row mb-4">
        <div className="col-sm-6 col-lg-3">
          <div className="card h-100">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <div className="subheader">Atividades em Atraso (Hoje)</div>
                <div className="h1 mb-3">{tarefasAtrasadas.length}</div>
              </div>
              <div className="d-flex align-items-center mt-auto">
                <div className="text-green me-2">+8%</div>
                <small className="text-muted">vs ontem</small>
              </div>
            </div>
          </div>
        </div>
        <div className="col-sm-6 col-lg-3">
          <div className="card h-100">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <div className="subheader">Tarefas concluídas</div>
                <div className="h1 mb-3">{tarefasConcluidas.length}</div>
              </div>
              <div className="d-flex align-items-center mt-auto">
                <div className="text-red me-2">-2%</div>
                <small className="text-muted">vs semana passada</small>
              </div>
            </div>
          </div>
        </div>
        <div className="col-sm-6 col-lg-3">
          <div className="card h-100">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <div className="subheader">Tarefas em andamento</div>
                <div className="h1 mb-3">{tarefasConcluidas.length}</div>
              </div>
              <div className="d-flex align-items-center mt-auto">
                <div className="text-red me-2">-2%</div>
                <small className="text-muted">vs semana passada</small>
              </div>
            </div>
          </div>
        </div>
        <div className="col-sm-6 col-lg-3">
          <div className="card h-100">
            <div className="card-body d-flex flex-column justify-content-between">
              <div>
                <div className="subheader">Tarefas Canceladas</div>
                <div className="h1 mb-3">{tarefasConcluidas.length}</div>
              </div>
              <div className="d-flex align-items-center mt-auto">
                <div className="text-red me-2">-2%</div>
                <small className="text-muted">vs semana passada</small>
              </div>
            </div>
          </div>
        </div>
      </div>


      {/* Row para a tabela */}
      <div className="row">
        <div className="col-12">
          <div className="card">
            <div className="card-header">
              <h3 className="card-title">Últimas atividades</h3>
            </div>
            <DataTable
              columns={colunas}
              data={apiData || []}
              customStyles={estilosTabler}
              pagination
              paginationPerPage={5}
              paginationRowsPerPageOptions={[5, 10, 15]}
              highlightOnHover
              striped
              responsive
              progressPending={loading}
              progressComponent={
                <div className="d-flex justify-content-center p-4">
                  <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Carregando...</span>
                  </div>
                </div>
              }
              noDataComponent={
                <div className="text-center p-4 text-muted">
                  Nenhuma atividade encontrada
                </div>
              }
            />
          </div>
        </div>
      </div>
    </>
  );
}

export default Dashboard;