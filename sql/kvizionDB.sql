-- =====================================================================
-- KVizion - banco de dados versão 3 (script COMPLETO)
-- ATENÇÃO: este script APAGA e recria todas as tabelas do banco kvizion.
-- Para manter os dados que você já tem, use o atualizar_para_v3.sql.
--
-- Usuários criados:  admin / admin123  (perfil ADMIN)
--                    vendedor / vendedor123  (perfil VENDEDOR)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS kvizion DEFAULT CHARACTER SET utf8mb4;
USE kvizion;

DROP TABLE IF EXISTS item_venda;
DROP TABLE IF EXISTS item_orcamento;
DROP TABLE IF EXISTS venda;
DROP TABLE IF EXISTS orcamento;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
    id     INT          NOT NULL AUTO_INCREMENT,
    nome   VARCHAR(100) NOT NULL,
    login  VARCHAR(50)  NOT NULL,
    senha  CHAR(64)     NOT NULL,              -- hash SHA-256 da senha
    perfil VARCHAR(20)  NOT NULL DEFAULT 'VENDEDOR',  -- ADMIN ou VENDEDOR
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_login (login)
);

CREATE TABLE cliente (
    id       INT          NOT NULL AUTO_INCREMENT,
    nome     VARCHAR(100) NOT NULL,
    endereco VARCHAR(150) DEFAULT NULL,
    telefone VARCHAR(20)  DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE produto (
    id             INT           NOT NULL AUTO_INCREMENT,
    nome           VARCHAR(100)  NOT NULL,
    preco          DECIMAL(10,2) NOT NULL,
    quantidade     INT           NOT NULL DEFAULT 0,
    estoque_minimo INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

CREATE TABLE venda (
    id         INT           NOT NULL AUTO_INCREMENT,
    cliente_id INT           NOT NULL,
    total      DECIMAL(10,2) NOT NULL,
    data_venda DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);

CREATE TABLE item_venda (
    id         INT           NOT NULL AUTO_INCREMENT,
    venda_id   INT           NOT NULL,
    produto_id INT           NOT NULL,
    quantidade INT           NOT NULL,
    subtotal   DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_item_venda_venda   FOREIGN KEY (venda_id)   REFERENCES venda (id),
    CONSTRAINT fk_item_venda_produto FOREIGN KEY (produto_id) REFERENCES produto (id)
);

CREATE TABLE orcamento (
    id             INT           NOT NULL AUTO_INCREMENT,
    cliente_id     INT           NOT NULL,
    total          DECIMAL(10,2) NOT NULL,
    data_orcamento DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);

CREATE TABLE item_orcamento (
    id           INT           NOT NULL AUTO_INCREMENT,
    orcamento_id INT           NOT NULL,
    produto_id   INT           NOT NULL,
    quantidade   INT           NOT NULL,
    subtotal     DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_item_orcamento_orcamento FOREIGN KEY (orcamento_id) REFERENCES orcamento (id),
    CONSTRAINT fk_item_orcamento_produto   FOREIGN KEY (produto_id)   REFERENCES produto (id)
);

-- ------------------------- Dados iniciais -------------------------

INSERT INTO usuario (nome, login, senha, perfil) VALUES
('Administrador', 'admin',    SHA2('admin123', 256),    'ADMIN'),
('Vendedor',      'vendedor', SHA2('vendedor123', 256), 'VENDEDOR');

INSERT INTO cliente (id, nome, endereco, telefone) VALUES
(1, 'Carlos Silva', 'Rua A', '99999-1111'),
(2, 'Maria Souza',  'Rua B', '99999-2222'),
(3, 'João Pedro',   'Rua C', '99999-3333');

INSERT INTO produto (id, nome, preco, quantidade, estoque_minimo) VALUES
(1, 'Cimento', 40.00, 100, 20),
(2, 'Areia',   20.00,  50, 10),
(3, 'Tijolo',   1.50, 500, 100),
(4, 'Brita',   35.00,  30, 10);

INSERT INTO venda (id, cliente_id, total, data_venda) VALUES
(1, 1, 80.00, '2026-03-17 22:08:47');
INSERT INTO item_venda (venda_id, produto_id, quantidade, subtotal) VALUES
(1, 1, 2, 80.00);

INSERT INTO orcamento (id, cliente_id, total, data_orcamento) VALUES
(1, 1, 40.00, '2026-03-17 22:48:30');
INSERT INTO item_orcamento (orcamento_id, produto_id, quantidade, subtotal) VALUES
(1, 2, 2, 40.00);
