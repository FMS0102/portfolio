## 💻 Portfolio API - Backend

API desenvolvida em Spring Boot 3 e Java 21, projetada para ser o backend de um portfólio pessoal dinâmico. O foco principal deste projeto é demonstrar proficiência em Arquitetura de Software, Segurança (seguindo o padrão OAuth2 Resource Server) e Qualidade de Código.

Front-end Associado: https://fms-dev.com.br/

## Destaques de Arquitetura & Segurança
Este projeto foi construído sobre uma arquitetura limpa, com ênfase nas seguintes decisões técnicas de alto nível:

### 1. Sistema de Autenticação JWT (Padrão Ouro)
    
O módulo de segurança foi implementado utilizando o padrão oficial Spring Security OAuth2 Resource Server, garantindo um sistema de autenticação `Stateless` e escalável.

- **Chaves RSA para Assinatura:** O Access Token é assinado e validado com chaves RSA (Assinatura Assimétrica). Isso é mais seguro e escalável do que o HMAC-Secret, pois a chave privada para assinar tokens é mantida em segredo no servidor.

- **Refresh Tokens Seguros:** O fluxo de renovação de tokens (Refresh Token) foi implementado com as melhores práticas de segurança:
    *  **Mitigação XSS (HttpOnly Cookie):** O Refresh Token é entregue via header Set-Cookie com a flag HttpOnly ativada, tornando-o inacessível por scripts JavaScript e prevenindo roubo via ataques XSS.

    * **Revogação e Hashing do Refresh Token:** O Refresh Token é invalidado após cada uso (revogado e substituído). Além disso, o seu segredo é armazenado como um hash no banco de dados, protegendo contra vazamentos de tokens em caso de comprometimento do DB.


### 2. Qualidade de Código e Arquitetura

- **Mapeamento de Alto Desempenho (MapStruct):** Utilizado para conversão entre Entidades e DTOs, garantindo a máxima performance (zero reflection em tempo de execução) e segurança na checagem de tipos.

- **Separação de Responsabilidades:** Rigorosa separação entre a camada de Service (lógica pura de negócio) e Controller (responsável exclusiva pela formatação HTTP, incluindo status codes e headers).

- **Gerenciamento Profissional de Exceções:** Implementação de um Global Exception Handler e handlers customizados para retornar respostas profissionais e com o status code correto (ex: 401 e 403 personalizados).

- **Transacionalidade (ACID):** Uso estratégico da anotação @Transactional no AuthService para garantir que as operações críticas de segurança (revogação e criação de tokens) sejam atômicas e mantenham a integridade dos dados no banco.

## 🛠️ Tecnologias Utilizadas

 - Linguagem: Java 21
 - Framework: Spring Boot 3
 - Segurança: Spring Security (OAuth2 Resource Server)
 - Banco de Dados: PostgreSQL
 - Mapeamento: MapStruct

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
 - JDK 21 ou superior
 - PostgreSQL (pode ser via Docker)
 - Cliente HTTP (Postman ou cURL)

### Configuração
1. Clone o repositório:

```
git clone 
cd portfolio-api
```

2. Crie os arquivos de Chaves:

    Este projeto requer chaves RSA para segurança. Gere seus arquivos private.key e public.key e coloque-os na pasta src/main/resources (ou no caminho que você configurou no application.yml).

3. Configuração de Ambiente:

    Crie o arquivo .env na raiz do projeto com as credenciais do seu banco de dados e as variáveis do usuário administrador.

4. Execução:
    
    Inicie a aplicação via sua IDE ou com o Maven:

```
./mvnw spring-boot:run
```

A API estará rodando em http://localhost:8080.



## 📌 Endpoints Principais

| Método |	Endpoint | Descrição | Segurança |
|--------|-----------|-----------|-----------|
|POST	 |`/auth/login`|	Autentica o usuário. Retorna Access Token no corpo e Refresh Token no HttpOnly Cookie.	| Público
|POST	 |`/auth/refresh`|	Renova o Access Token. Requer o Refresh Token no HttpOnly Cookie.	|Público (Via Cookie)
|GET	 |`/users`	|Exemplo de recurso protegido.	|Requer Access Token
|


## Contato
**Felipe Moreira Simões** -
[Linkedin](https://www.linkedin.com/in/felipe-m-sim%C3%B5es-70465416b/)