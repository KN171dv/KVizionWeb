# KVizionWeb

Sistema web de vendas e orçamentos para lojas de varejo (Projeto Integrador Senac).
Back-end em Java com Spring Boot (API REST) e JDBC; front-end em HTML, CSS e JavaScript.

## Tecnologias

- Java 21, Spring Boot 3 (Spring Web), Maven
- MySQL 8 com JDBC (classes DAO)
- HTML, CSS e JavaScript puros (pasta `src/main/resources/static`)
- JUnit 5 para os testes unitários

## Organização do código

- `com.kvizion.model` – classes do domínio (Cliente, Produto, Pedido, Venda, Orcamento, ItemPedido, Usuario)
- `com.kvizion.dao` – acesso ao banco com JDBC
- `com.kvizion.service` – validações e regras de negócio
- `com.kvizion.controller` – rotas da API REST (`/api/...`)
- `com.kvizion.dto` – dados recebidos das páginas
- `com.kvizion.config` – configuração (conexão com o banco e exigência de login)

## Como rodar

1. No MySQL, executar o script `sql/kvizionDB.sql` (cria o banco `kvizion` com dados de exemplo).
2. Copiar `config.exemplo.properties` para `config.properties` e colocar a senha do MySQL.
3. Abrir o projeto no NetBeans e executar (F6).
4. Abrir http://localhost:8080 no navegador.

Usuários de exemplo: `admin` / `admin123` (administrador) e `vendedor` / `vendedor123`.

## Testes

Botão direito no projeto → **Test** (ou `mvn test`). Os testes unitários não precisam do banco.
