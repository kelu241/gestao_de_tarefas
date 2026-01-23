import { useState, useEffect } from 'react';
import { ApiService } from '../services/TarefaService';

const ExemploComInterceptor = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // ✅ Token será adicionado automaticamente pelo interceptor
    const subscription = ApiService.get('/api/users').subscribe({
      next: (data) => {
        setUsers(data);
        setLoading(false);
      },
      error: (err) => {
        console.error('Erro:', err);
        setLoading(false);
      }
    });

    return () => subscription.unsubscribe();
  }, []);

  const criarUsuario = () => {
    // ✅ Token também será adicionado automaticamente
    const novoUsuario = { name: 'João', email: 'joao@email.com' };
    
    ApiService.post('/api/users', novoUsuario).subscribe({
      next: (data) => {
        console.log('Usuário criado:', data);
        // Recarregar lista
      },
      error: (err) => console.error('Erro ao criar:', err)
    });
  };

  return (
    <div>
      {loading && <p>Carregando...</p>}
      
      <button onClick={criarUsuario}>Criar Usuário</button>
      
      <ul>
        {users.map(user => (
          <li key={user.id}>{user.name}</li>
        ))}
      </ul>
    </div>
  );
};

export default ExemploComInterceptor;