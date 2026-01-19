# Solução para Problema de 401 após Reiniciar o Banco

## 🚨 Problema Identificado

Quando você reinicia o banco de dados SQL Server, as tabelas são recriadas vazias. Como não existem usuários cadastrados, qualquer tentativa de login retorna **401 Unauthorized**.

## ✅ Soluções Implementadas

### 1. **DataLoader Automático** 
- **Arquivo**: `src/main/java/com/example/springcopylot/config/DataLoader.java`
- **Funcionalidade**: Cria usuários padrão automaticamente quando a aplicação inicia e não há usuários no banco

#### Usuários Padrão Criados:
| Username | Senha    | Role  | Email            |
|----------|----------|-------|------------------|
| `admin`  | `admin123` | ADMIN | admin@example.com |
| `user`   | `user123`  | USER  | user@example.com  |

### 2. **Configuração Melhorada do Banco**
- **Arquivo**: `src/main/resources/application.yml`
- **Melhorias**:
  - Pool de conexões otimizado (10 conexões máximas)
  - Timeouts ajustados para maior estabilidade
  - `trustServerCertificate=true` para evitar problemas de SSL
  - Configurações de performance do Hibernate

## 🔧 Como Usar

### Após reiniciar o banco:

1. **Inicie a aplicação**:
   ```bash
   mvn spring-boot:run
   ```

2. **Observe o console** - você verá:
   ```
   === INICIALIZANDO DADOS PADRÃO ===
   ✅ Usuários padrão criados:
      👤 Admin - Username: admin, Senha: admin123
      👤 User  - Username: user,  Senha: user123
   =====================================
   ```

3. **Faça login** com qualquer um dos usuários criados:
   ```bash
   curl -X POST http://localhost:8080/api/usuario/login \
   -H "Content-Type: application/json" \
   -d '{"username": "admin", "senha": "admin123"}'
   ```

### Para criar novos usuários:

```bash
curl -X POST http://localhost:8080/api/usuario/register \
-H "Content-Type: application/json" \
-d '{
  "username": "novouser",
  "email": "novo@example.com",
  "senha": "senha123"
}'
```

## 🔒 Considerações de Segurança

⚠️ **IMPORTANTE**: As senhas estão em texto plano apenas para desenvolvimento. Para produção, implemente:

1. **Hash de senhas** usando BCrypt:
   ```java
   @Autowired
   private BCryptPasswordEncoder passwordEncoder;
   
   // No DataLoader:
   admin.setSenha(passwordEncoder.encode("admin123"));
   ```

2. **Senhas como variáveis de ambiente**:
   ```yaml
   # application.yml
   app:
     default:
       admin-password: ${ADMIN_PASSWORD:admin123}
       user-password: ${USER_PASSWORD:user123}
   ```

## 🔄 Estratégias de DDL Alternativas

Se você quiser controle total sobre o schema:

### Opção 1: `ddl-auto: create-drop` (dados temporários)
```yaml
hibernate:
  ddl-auto: create-drop  # Recria tabelas a cada restart
```

### Opção 2: `ddl-auto: none` + scripts SQL
```yaml
hibernate:
  ddl-auto: none  # Não gerencia schema automaticamente
sql:
  init:
    mode: always
    schema-locations: classpath:schema.sql
    data-locations: classpath:data.sql
```

### Opção 3: Flyway para migrações
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

## 🐛 Troubleshooting

### Se ainda tiver problemas:

1. **Verifique os logs** da aplicação no startup
2. **Teste a conexão** com o banco:
   ```sql
   SELECT COUNT(*) FROM usuarios;
   ```
3. **Limpe o cache** do Hibernate:
   ```bash
   mvn clean compile
   ```

### Logs úteis para debugging:
```yaml
logging:
  level:
    com.example.springcopylot.config.DataLoader: DEBUG
    org.hibernate.SQL: DEBUG
    org.springframework.boot.autoconfigure.orm.jpa: DEBUG
```

## 📝 Resumo

Agora toda vez que você reiniciar o banco, a aplicação automaticamente:
1. ✅ Detecta que não há usuários
2. ✅ Cria usuários padrão (admin/user)
3. ✅ Permite login imediato sem erro 401
4. ✅ Mantém configuração estável do banco

**Nunca mais erro 401 após reiniciar o banco!** 🎉