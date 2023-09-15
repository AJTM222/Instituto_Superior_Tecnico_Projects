DROP TABLE IF EXISTS tem_outra;
DROP TABLE IF EXISTS super_categoria;
DROP TABLE IF EXISTS categoria_simples;

DROP TABLE IF EXISTS instalada_em;
DROP TABLE IF EXISTS evento_reposicao;
DROP TABLE IF EXISTS planograma;

DROP TABLE IF EXISTS tem_categoria;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS prateleira;
DROP TABLE IF EXISTS responsavel_por;

DROP TABLE IF EXISTS ponto_de_retalho;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS retalhista;
DROP TABLE IF EXISTS IVM;

CREATE TABLE categoria
    (nome VARCHAR(80) NOT NULL,
    CONSTRAINT pk_categoria PRIMARY KEY(nome));

CREATE TABLE categoria_simples
    (nome VARCHAR(80) NOT NULL,
    CONSTRAINT pk_categoria_simples PRIMARY KEY(nome),
    CONSTRAINT fk_categoria_simples_categoria FOREIGN KEY(nome) REFERENCES categoria(nome));

CREATE TABLE super_categoria
    (nome VARCHAR(80) NOT NULL,
    CONSTRAINT pk_super_categoria PRIMARY KEY(nome),
    CONSTRAINT fk_super_categoria_categoria FOREIGN KEY(nome) REFERENCES categoria(nome));

CREATE TABLE tem_outra
    (super_categoria VARCHAR(80) NOT NULL,
    categoria VARCHAR(80) NOT NULL UNIQUE,
    CONSTRAINT pk_tem_outra PRIMARY KEY(categoria),
    CONSTRAINT fk_tem_outra_super_categoria FOREIGN KEY(super_categoria) REFERENCES super_categoria(nome),
    CONSTRAINT fk_tem_outra_categoria FOREIGN KEY(categoria) REFERENCES categoria(nome));

CREATE TABLE produto
    (ean VARCHAR(13) NOT NULL,
    cat VARCHAR(80) NOT NULL,
    descr VARCHAR(80) NOT NULL,
    CONSTRAINT pk_produto PRIMARY KEY(ean),
    CONSTRAINT fk_produto_categoria FOREIGN KEY(cat) REFERENCES categoria(nome));

CREATE TABLE tem_categoria
    (ean VARCHAR(13) NOT NULL,
    nome VARCHAR(80) NOT NULL,
    CONSTRAINT pk_tem_categoria PRIMARY KEY(ean, nome),
    CONSTRAINT fk_tem_categoria_produto FOREIGN KEY(ean) REFERENCES produto(ean),
    CONSTRAINT fk_tem_categoria_categoria FOREIGN KEY(nome) REFERENCES categoria(nome));

CREATE TABLE IVM
    (num_serie NUMERIC(13) NOT NULL,
    fabricante NUMERIC(13) NOT NULL,
    CONSTRAINT pk_IVM PRIMARY KEY(num_serie, fabricante));

CREATE TABLE ponto_de_retalho
    (nome VARCHAR(80) NOT NULL UNIQUE,
    distrito VARCHAR(80) NOT NULL,
    concelho VARCHAR(80) NOT NULL,
    CONSTRAINT pk_ponto_de_retalho PRIMARY KEY(nome));

CREATE TABLE instalada_em
    (num_serie NUMERIC(13) NOT NULL,
    fabricante NUMERIC(13) NOT NULL,
    local VARCHAR(80) NOT NULL,
    CONSTRAINT pk_instalada_em PRIMARY KEY(num_serie, fabricante),
    CONSTRAINT fk_instalada_IVM FOREIGN KEY(num_serie, fabricante) REFERENCES IVM(num_serie, fabricante),
    CONSTRAINT fk_instalada_ponto_de_retalho FOREIGN KEY(local) REFERENCES ponto_de_retalho(nome));

CREATE TABLE prateleira
    (nro INT NOT NULL,
    num_serie NUMERIC(13) NOT NULL,
    fabricante NUMERIC(13) NOT NULL,
    altura FLOAT NOT NULL,
    nome VARCHAR(80) NOT NULL,
    CONSTRAINT pk_prateleira PRIMARY KEY(nro, num_serie, fabricante),
    CONSTRAINT fk_prateleira_IVM FOREIGN KEY(num_serie, fabricante) REFERENCES IVM(num_serie, fabricante),
    CONSTRAINT fk_prateleira_categoria FOREIGN KEY(nome) REFERENCES categoria(nome),
    CHECK (altura > 0));

CREATE TABLE planograma
    (ean VARCHAR(13) NOT NULL, 
    nro INT NOT NULL,
    num_serie NUMERIC(13) NOT NULL,
    fabricante NUMERIC(13) NOT NULL,
    faces INT NOT NULL,
    unidades INT NOT NULL,
    loc VARCHAR(80) NOT NULL,
    CONSTRAINT pk_planograma PRIMARY KEY(ean, nro, num_serie, fabricante),
    CONSTRAINT fk_planograma_produto FOREIGN KEY(ean) REFERENCES produto(ean),
    CONSTRAINT fk_planograma_prateleira FOREIGN KEY(nro, num_serie, fabricante) REFERENCES prateleira(nro, num_serie, fabricante),
    CHECK (unidades > 0));

CREATE TABLE retalhista
    (tin NUMERIC(13) NOT NULL,
    nome VARCHAR(80) NOT NULL UNIQUE,
    CONSTRAINT pk_retalhista PRIMARY KEY(tin));

CREATE TABLE responsavel_por
    (nome_cat VARCHAR(80) NOT NULL,
    tin NUMERIC(13) NOT NULL,
    num_serie NUMERIC(13) NOT NULL,
    fabricante NUMERIC(13) NOT NULL,
    CONSTRAINT pk_responsavel_por PRIMARY KEY(num_serie, fabricante, nome_cat),
    CONSTRAINT fk_responsavel_por_IVM FOREIGN KEY(num_serie, fabricante) REFERENCES IVM(num_serie, fabricante),
    CONSTRAINT fk_responsavel_por_retalhista FOREIGN KEY(tin) REFERENCES retalhista(tin),
    CONSTRAINT fk_responsavel_por_categoria FOREIGN KEY(nome_cat) REFERENCES categoria(nome));

CREATE TABLE evento_reposicao
    (ean VARCHAR(13) NOT NULL,
    nro INT NOT NULL,
    num_serie NUMERIC(13) NOT NULL, 
    fabricante NUMERIC(13) NOT NULL,
    instante VARCHAR(19) NOT NULL,
    unidades INT NOT NULL,
    tin NUMERIC(13) NOT NULL,
    CONSTRAINT pk_evento_reposicao PRIMARY KEY(ean, nro, num_serie, fabricante, instante),
    CONSTRAINT fk_evento_reposicao_planograma FOREIGN KEY(ean, nro, num_serie, fabricante) REFERENCES planograma(ean, nro, num_serie, fabricante),
    CONSTRAINT fk_evento_reposicao_retalhista FOREIGN KEY(tin) REFERENCES retalhista(tin),
    CHECK (unidades > 0));