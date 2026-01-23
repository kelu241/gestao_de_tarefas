
const LoginAction = (Login, url) => {
  return fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(Login)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Erro ao fazer login');
      }
      return response.json();
    })
    .then(data => {
      // Supondo que o token JWT esteja na propriedade 'token' da resposta
      localStorage.setItem('authToken', data.accessToken);
      localStorage.setItem('refreshToken', JSON.stringify(data.refreshToken));
      console.log(data);
      document.location.href = "/";
      return data;
    });
};
 const logout = () => {
localStorage.removeItem('authToken');
};
const getAuthToken = () => {
return localStorage.getItem('authToken');
};

const getRefreshToken = () => {
  const token = localStorage.getItem('refreshToken');
  return token ? JSON.parse(token) : null;
}



export {  LoginAction, logout, getAuthToken, getRefreshToken };