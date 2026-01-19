# Configuração CORS para React

## Resumo das alterações realizadas

1. **Criado arquivo CorsConfig.java** - Configuração global de CORS
2. **Atualizado SecurityConfig.java** - Integração CORS com Spring Security

## Origens permitidas (URLs do React)

- `http://localhost:3000` - Porta padrão do React dev server
- `http://localhost:3001` - Porta alternativa comum
- `http://127.0.0.1:3000` - Variação do localhost
- `http://127.0.0.1:3001` - Variação do localhost

## Métodos HTTP permitidos

- GET, POST, PUT, DELETE, OPTIONS, HEAD

## Como consumir a API no React

### 1. Configuração base da API

```javascript
// api.js
const API_BASE_URL = 'http://localhost:8080/api';

const api = {
  // Função para fazer login e obter token
  login: async (credentials) => {
    const response = await fetch(`${API_BASE_URL}/usuario/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include', // Importante para CORS com credenciais
      body: JSON.stringify(credentials),
    });
    return response.json();
  },

  // Função para buscar projetos (requer autenticação)
  getProjetos: async (token) => {
    const response = await fetch(`${API_BASE_URL}/projetos`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`, // JWT token
      },
      credentials: 'include',
    });
    return response.json();
  },

  // Função para criar novo projeto
  createProjeto: async (projeto, token) => {
    const response = await fetch(`${API_BASE_URL}/projetos`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      credentials: 'include',
      body: JSON.stringify(projeto),
    });
    return response.json();
  },
};

export default api;
```

### 2. Exemplo de componente React

```javascript
// ProjetosList.js
import React, { useState, useEffect } from 'react';
import api from './api';

function ProjetosList() {
  const [projetos, setProjetos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProjetos = async () => {
      try {
        // Assumindo que você armazena o token no localStorage
        const token = localStorage.getItem('authToken');
        
        if (!token) {
          setError('Token não encontrado. Faça login novamente.');
          setLoading(false);
          return;
        }

        const data = await api.getProjetos(token);
        setProjetos(data);
      } catch (err) {
        setError('Erro ao carregar projetos: ' + err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchProjetos();
  }, []);

  if (loading) return <div>Carregando...</div>;
  if (error) return <div>Erro: {error}</div>;

  return (
    <div>
      <h2>Lista de Projetos</h2>
      <ul>
        {projetos.map((projeto) => (
          <li key={projeto.id}>
            <strong>{projeto.nome}</strong> - {projeto.descricao}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ProjetosList;
```

### 3. Gerenciamento de autenticação

```javascript
// auth.js
import api from './api';

export const authService = {
  login: async (username, password) => {
    try {
      const response = await api.login({ username, password });
      
      if (response.token) {
        localStorage.setItem('authToken', response.token);
        return { success: true, token: response.token };
      }
      
      return { success: false, message: 'Login falhou' };
    } catch (error) {
      return { success: false, message: error.message };
    }
  },

  logout: () => {
    localStorage.removeItem('authToken');
  },

  getToken: () => {
    return localStorage.getItem('authToken');
  },

  isAuthenticated: () => {
    const token = localStorage.getItem('authToken');
    return token !== null;
  }
};
```

## Testando CORS

Para testar se o CORS está funcionando:

1. **Inicie seu servidor Spring Boot**: `mvn spring-boot:run`
2. **Inicie seu app React**: `npm start` (geralmente na porta 3000)
3. **Faça uma requisição de teste** no console do navegador:

```javascript
fetch('http://localhost:8080/api/usuario/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  credentials: 'include',
  body: JSON.stringify({
    username: 'seu_usuario',
    password: 'sua_senha'
  })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Erro:', error));
```

## Troubleshooting

Se ainda tiver problemas de CORS:

1. **Verifique no Console do Navegador** se há erros relacionados a CORS
2. **Confirme a URL da API** - deve ser exatamente `http://localhost:8080/api`
3. **Verifique se o Spring Boot está rodando** na porta 8080
4. **Teste com diferentes navegadores** para descartar cache
5. **Use ferramentas como Postman** para testar endpoints sem CORS

## Configurações adicionais (se necessário)

Se precisar adicionar mais origens no futuro, edite o arquivo `CorsConfig.java`:

```java
// Adicione novas origens aqui
.allowedOrigins(
    "http://localhost:3000",
    "http://localhost:3001",
    "https://seu-dominio-producao.com" // Para produção
)
```