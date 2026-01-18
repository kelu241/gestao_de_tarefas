import { useState } from 'react';
import { createLogin } from './../models/Login';
import { LoginAction } from '../services/LoginAction';

const LoginForm = () => {

  const URL = "http://localhost:8080/api/usuario/login";

  // Criar nova instância do login
  const login = createLogin();

  // Estado para os campos do formulário
  const [formData, setFormData] = useState(login);

  // Estado para erros de validação
  const [errors, setErrors] = useState({});

  // Estado para loading
  const [loading, setLoading] = useState(false);

  // Função para atualizar campos
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Limpar erro quando o usuário digita
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  // Validação dos campos
  const validateForm = () => {
    const newErrors = {};

    if (!formData.username.trim()) {
      newErrors.username = 'Nome do usuário é obrigatório';
    }



    if (!formData.senha) {
      newErrors.senha = 'Senha é obrigatória';
    } else if (formData.senha.length < 6) {
      newErrors.senha = 'Senha deve ter pelo menos 6 caracteres';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Submissão do formulário
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      alert('Por favor, corrija os erros no formulário.');
      return;
    }

    setLoading(true);

    try {

      LoginAction(formData, URL);

      alert('Login feito com suecesso');
      document.location.href = "/";

      // Limpar formulário
      setFormData(createLogin());

    } catch (error) {
      alert('Erro ao fazer login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="d-flex justify-content-center align-items-center min-vh-100"
      style={{
        backgroundImage: 'linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url("https://images.unsplash.com/photo-1557804506-669a67965ba0?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80")',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
      }}
    >
      <div className="card" style={{ width: '400px', maxWidth: '90%', boxShadow: '0 10px 30px rgba(0, 0, 0, 0.3)' }}>
        <div className="card-header text-center">
          <h3 className="card-title">Login de Usuário</h3>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="card-body">
            {/* username */}
            <div className="mb-3">
              <label className="form-label">Nome Usuário</label>
              <input
                type="text"
                name="username"
                className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                value={formData.username}
                onChange={handleChange}
                placeholder="Digite o nome de usuário"
              />
              {errors.username && (
                <div className="invalid-feedback">{errors.username}</div>
              )}
            </div>

            {/* Senha */}
            <div className="mb-3">
              <label className="form-label">Senha</label>
              <input
                type="password"
                name="senha"
                className={`form-control ${errors.senha ? 'is-invalid' : ''}`}
                value={formData.senha}
                onChange={handleChange}
                placeholder="Digite a senha"
              />
              {errors.senha && (
                <div className="invalid-feedback">{errors.senha}</div>
              )}
            </div>
          </div>

          <div className="card-footer">
            <div className="d-flex justify-content-between align-items-center">
              <button
                type="button"
                className="btn btn-primary"
                onClick={() => window.location.href = '/register'}
              >
                Fazer Cadastro
              </button>

              <div>
                <button
                  type="button"
                  className="btn btn-secondary me-2"
                  onClick={() => setFormData(createLogin())}
                >
                  Limpar
                </button>

                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={loading}
                >
                  {loading ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2" />
                      Salvando...
                    </>
                  ) : (
                    'Fazer Login'
                  )}
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;