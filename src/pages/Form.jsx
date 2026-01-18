import { useState } from 'react';
import { createUser } from '../models/User';
import { AuthService } from '../services/AuthService';

const Form = () => {
  const URL = "http://localhost:8080/api/usuario/register"
  // Estado para os campos do formulário
  const [formData, setFormData] = useState(
    createUser()
  );

  // Estado para erros de validação
  const [errors, setErrors] = useState({});

  // Estado para loading
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));

    // Limpar erro quando o usuário digita
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: '',
      }));
    }
  };

  // Validação dos campos
  const validateForm = () => {
    const newErrors = {};

    if (!formData.username.trim()) {
      newErrors.username = 'Nome é obrigatório';
    }

    if (!formData.email.trim()) {
      newErrors.email = 'Email é obrigatório';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Email inválido';
    }

    if (!formData.senha) {
      newErrors.senha = 'Senha é obrigatória';
    } else if (formData.senha.length < 6) {
      newErrors.senha = 'Senha deve ter pelo menos 6 caracteres';
    }


    if (!formData.role.trim()) {
      newErrors.role = 'Cargo é obrigatório';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Submissão do formulário
  const handleSubmit = async (e) => {
    e.preventDefault();



    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      // Simular API call
      await AuthService.register(formData, URL);
      alert('Usuário cadastrado com sucesso!');

      // Limpar formulário
      setFormData(
        createUser()
      );
    } catch (error) {
      alert(error?.message ? `Erro ao cadastrar usuário: ${error.message}` : 'Erro ao cadastrar usuário');
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
      <div className="card" style={{ width: '800px', maxWidth: '90%', boxShadow: '0 10px 30px rgba(0, 0, 0, 0.3)' }}>
        <div className="card-header text-center">
          <h3 className="card-title">Criação de Conta de Usuário</h3>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="card-body">
            <div className="row">
              {/* Nome */}

              <div className="mb-3">
                <label className="form-label">Nome completo</label>
                <input
                  type="text"
                  name="username"
                  className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                  value={formData.username}
                  placeholder="Digite o nome completo"
                  onChange={handleChange}
                />
                {errors.username && (
                  <div className="invalid-feedback">{errors.username}</div>
                )}
              </div>


              {/* Email */}
              <div className="row">
                <div className="mb-3">
                  <label className="form-label">Email</label>
                  <input
                    type="email"
                    name="email"
                    className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                    value={formData.email}
                    placeholder="Digite o email"
                    onChange={handleChange}
                  />
                  {errors.email && (
                    <div className="invalid-feedback">{errors.email}</div>
                  )}
                </div>
              </div>

              {/* Senha */}
              <div className="col-lg-6">
                <div className="mb-3">
                  <label className="form-label">Senha</label>
                  <input
                    type="password"
                    name="senha"
                    className={`form-control ${errors.senha ? 'is-invalid' : ''}`}
                    value={formData.senha}
                    placeholder="Digite a senha"
                    onChange={handleChange}
                  />
                  {errors.senha && (
                    <div className="invalid-feedback">{errors.senha}</div>
                  )}
                </div>
              </div>


              {/* Cargo */}
              <div className="col-lg-6">
                <div className="mb-3">
                  <label className="form-label">Cargo</label>
                  <select
                    name="role"
                    className={`form-select ${errors.role ? 'is-invalid' : ''}`}
                    value={formData.role}
                    onChange={handleChange}
                  >
                    <option value="">Selecione um cargo</option>
                    <option value="USER">Usuário comum</option>
                    <option value="ADMIN">Administrador</option>

                  </select>
                  {errors.role && (
                    <div className="invalid-feedback">{errors.role}</div>
                  )}
                </div>
              </div>


            </div>
          </div>

          <div className="card-footer">
            <div className="d-flex justify-content-between align-items-center">
              <button
                type="button"
                className="btn btn-primary"
                onClick={() => window.location.href = '/login'}
              >
                Fazer Login
              </button>

              <div>
                <button
                  type="button"
                  className="btn btn-secondary me-2"
                  onClick={() => setFormData(createUser())}
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
                    'Salvar Usuário'
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

export default Form;