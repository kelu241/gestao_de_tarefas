# Sistema de Logging Personalizado - Spring Boot

Este projeto implementa um sistema de logging personalizado e estruturado para aplicações Spring Boot.

## 🚀 Características

- **Logging Estruturado**: Logs em formato JSON para melhor análise
- **Logging Automático**: Anotações para logging automático de métodos
- **Rastreamento de Requisições**: Interceptador para logar automaticamente todas as requisições HTTP
- **Contexto Personalizado**: MDC para rastreamento de contexto entre threads
- **Múltiplos Appenders**: Console, arquivo e arquivo de erro separado
- **Rotação de Logs**: Configuração automática de rotação por tamanho e tempo

## 📦 Dependências Adicionadas

```xml
<!-- AOP para logging automático -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

<!-- Encoder JSON para logs estruturados -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>

<!-- Jackson para serialização JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

## 🎯 Como Usar

### 1. Logging Manual

```java
import com.example.springcopylot.logging.CustomLogger;

@Service
public class MeuService {
    
    public void meuMetodo() {
        // Logs básicos
        CustomLogger.logInfo("Informação geral");
        CustomLogger.logDebug("Detalhes técnicos");
        CustomLogger.logWarn("Atenção necessária");
        
        try {
            // Seu código aqui
        } catch (Exception e) {
            CustomLogger.logError("Erro na operação", e);
        }
    }
}
```

### 2. Logging Automático com Anotações

```java
import com.example.springcopylot.logging.LogExecution;

@Service
public class MeuService {
    
    // Logging básico de entrada e saída
    @LogExecution
    public String processar(String input) {
        return "Processado: " + input;
    }
    
    // Logging com parâmetros e resultado
    @LogExecution(includeParameters = true, includeResult = true)
    public String processarCompleto(String input) {
        return "Processado: " + input;
    }
    
    // Logging apenas em caso de erro
    @LogExecution(onlyOnError = true)
    public void operacaoRiscos() {
        // Se houver exceção, será logada automaticamente
    }
}
```

### 3. Logging com Contexto Personalizado

```java
public void minhaOperacao(String userId) {
    // Define contexto
    CustomLogger.setRequestContext(
        CustomLogger.generateRequestId(),
        userId,
        "OPERACAO_ESPECIAL"
    );
    
    try {
        CustomLogger.logInfo("Executando operação para usuário %s", userId);
        // Sua lógica aqui
    } finally {
        CustomLogger.clearContext();
    }
}
```

### 4. Logging Automático para Classes Inteiras

```java
@LogExecution(includeParameters = true) // Aplica a todos os métodos da classe
@Service
public class MeuService {
    
    public void metodo1() {
        // Automaticamente logado
    }
    
    public void metodo2() {
        // Automaticamente logado
    }
}
```

## 🔧 Configuração

### Arquivo logback-spring.xml

O sistema está configurado para:
- **Console**: Logs estruturados em JSON
- **Arquivo**: `logs/application.log` com rotação diária
- **Arquivo de Erro**: `logs/error.log` apenas para erros
- **Rotação**: Arquivos de 10MB máximo, 30 dias de histórico

### Arquivo application.yml

```yaml
logging:
  level:
    com.example.springcopylot: INFO
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{requestId:-}] %logger{36} - %msg%n"
```

## 🧪 Testes

O sistema inclui endpoints de teste em `/api/logging/`:

```bash
# Teste logging manual
GET /api/logging/manual

# Teste logging automático
GET /api/logging/automatico?nome=João&idade=30

# Teste logging de erro
GET /api/logging/erro?gerarErro=true

# Teste logging com contexto
POST /api/logging/contexto?userId=123&operation=TESTE

# Teste diferentes níveis
GET /api/logging/niveis
```

## 📊 Estrutura dos Logs

### Exemplo de Log Estruturado:

```json
{
  "timestamp": "2024-01-15T10:30:45.123-03:00",
  "level": "INFO",
  "message": "Executando operação especial",
  "requestId": "a1b2c3d4",
  "userId": "user123",
  "operation": "OPERACAO_ESPECIAL",
  "app": "springcopylot",
  "env": "dev"
}
```

### Exemplo de Log de Método:

```json
{
  "timestamp": "2024-01-15T10:30:45.123-03:00",
  "level": "INFO",
  "event": "METHOD_ENTRY",
  "class": "ProjetoController",
  "method": "getAllProjetosAsync",
  "parameters": ["[parameters hidden]"]
}
```

## 📁 Estrutura de Arquivos

```
src/main/java/com/example/springcopylot/logging/
├── LogExecution.java           # Anotação para logging automático
├── LogLevel.java              # Enum para níveis de log
├── CustomLogger.java          # Utilitário principal de logging
├── LoggingAspect.java         # Aspect para interceptação automática
├── HttpRequestLoggingInterceptor.java  # Interceptador HTTP
└── LoggingExampleService.java # Exemplos de uso

src/main/resources/
├── logback-spring.xml         # Configuração do logback
└── application.yml            # Configurações da aplicação

logs/                          # Diretório de logs (criado automaticamente)
├── application.log           # Log principal
└── error.log                # Logs de erro
```

## ⚡ Performance

- **Logging Assíncrono**: Configurado para não bloquear a aplicação
- **Filtragem por Nível**: Logs de debug apenas em desenvolvimento
- **Rotação Automática**: Evita crescimento descontrolado de arquivos
- **Compressão**: Arquivos antigos são comprimidos automaticamente

## 🔐 Segurança

- **Parâmetros Sensíveis**: Por padrão, parâmetros não são logados
- **Configuração Flexível**: Controle fino sobre o que é logado
- **Sanitização**: Evita exposição de dados sensíveis nos logs

## 📈 Monitoramento

Os logs estruturados são compatíveis com:
- **ELK Stack** (Elasticsearch, Logstash, Kibana)
- **Splunk**
- **Fluentd**
- **Grafana Loki**

## 🎛️ Variáveis de Ambiente

```bash
export ENV=production          # Ambiente (dev, test, production)
export LOG_LEVEL=INFO          # Nível mínimo de log
export LOG_FILE_SIZE=10MB      # Tamanho máximo do arquivo
```

## 📞 Suporte

Para dúvidas ou problemas, consulte os logs em `logs/application.log` ou verifique os endpoints de teste em `/api/logging/`.