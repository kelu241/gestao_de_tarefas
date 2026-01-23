
import { from } from 'rxjs';
import httpInterceptor from '../utils/httpInterceptor';

// ✅ Service que usa interceptor (como HttpClient do Angular)
export const TarefaService = (url, method = 'GET', headers = {}, body = null) => {
  
  // Configurar headers padrão
  const defaultHeaders = {
    'Content-Type': 'application/json',
    ...headers
  };
  
  // Configurar opções do fetch
  const fetchOptions = {
    method,
    headers: defaultHeaders
  };
  
  // Adicionar body se não for GET
  if (method !== 'GET' && body) {
    fetchOptions.body = JSON.stringify(body);
  }
  
  // ✅ Usar interceptor ao invés de fetch direto
  return from(
    httpInterceptor.fetch(url, fetchOptions).then(response => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      return response.json();
    })
  );
};

// ✅ Métodos específicos (como Angular HttpClient)
export const ApiService = {
  
  get: (url, headers = {}) => {
    return TarefaService(url, 'GET', headers);
  },
  
  post: (url, data, headers = {}) => {
    return TarefaService(url, 'POST', headers, data);
  },
  
  put: (url, data, headers = {}) => {
    return TarefaService(url, 'PUT', headers, data);
  },
  
  delete: (url, headers = {}) => {
    return TarefaService(url, 'DELETE', headers);
  }
};



