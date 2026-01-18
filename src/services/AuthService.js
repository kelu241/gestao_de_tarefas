// Utilitário para gerenciar autenticação
export const AuthService = {
  
  // Fazer login e salvar token
  async login(email, password) {
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      
      const data = await response.json();
      
      if (data.token) {
        // ✅ Salvar token - interceptor pegará automaticamente
        localStorage.setItem('authToken', data.token);
        console.log('🔐 Token salvo - todas as requisições terão Authorization header');
        return data;
      }
      
      throw new Error('Token não recebido');
    } catch (error) {
      console.error('Erro no login:', error);
      throw error;
    }
  },
  
  // Logout
  logout() {
    // ✅ Remover token - interceptor parará de adicionar header
    localStorage.removeItem('authToken');
    console.log('🚪 Token removido - requisições sem Authorization');
    window.location.href = '/login';
  },
  
  // Verificar se está logado
  isAuthenticated() {
    return !!localStorage.getItem('authToken');
  },
  
  // Pegar token atual
  getToken() {
    return localStorage.getItem('authToken');
  },

register: async (userData, URL) => {
  const response = await fetch(URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });

  // Tenta ler o corpo (pode vir json ou texto)
  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json')
    ? await response.json().catch(() => null)
    : await response.text().catch(() => '');

  if (!response.ok) {
    // Se o backend manda "message", ótimo; senão usa status
    const msg =
      (body && typeof body === 'object' && body.message) ||
      (typeof body === 'string' && body) ||
      `Erro HTTP ${response.status}`;
    throw new Error(msg);
  }

  return body; // pode ser objeto ou string dependendo do backend
}
};