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
    instante DATE NOT NULL,
    unidades INT NOT NULL,
    tin NUMERIC(13) NOT NULL,
    CONSTRAINT pk_evento_reposicao PRIMARY KEY(ean, nro, num_serie, fabricante, instante),
    CONSTRAINT fk_evento_reposicao_planograma FOREIGN KEY(ean, nro, num_serie, fabricante) 
        REFERENCES planograma(ean, nro, num_serie, fabricante),
    CONSTRAINT fk_evento_reposicao_retalhista FOREIGN KEY(tin) REFERENCES retalhista(tin),
    CHECK (unidades > 0));


INSERT INTO IVM VALUES (0494020048294, 0304817351445);
INSERT INTO IVM VALUES (0918491856738, 0213131231445);
INSERT INTO IVM VALUES (1903198740292, 1113123137372);
INSERT INTO IVM VALUES (8101302193109, 4234726913132);
INSERT INTO IVM VALUES (9834958728874, 0428432342069);
INSERT INTO IVM VALUES (1213516471921, 3198784924672);
INSERT INTO IVM VALUES (7817328713616, 3167465372049);


INSERT INTO retalhista VALUES (4567138173633, 'T.Mansos');
INSERT INTO retalhista VALUES (8947176121535, 'H.Ramos');
INSERT INTO retalhista VALUES (0398538209201, 'J.Pavao');
INSERT INTO retalhista VALUES (4029741625241, 'V.Santos');
INSERT INTO retalhista VALUES (4737463651525, 'D.Pereira');
INSERT INTO retalhista VALUES (6726251652414, 'P.Lopes');


INSERT INTO categoria VALUES ('Doces');
INSERT INTO categoria VALUES ('Chocolates');
INSERT INTO categoria VALUES ('Chocolate Branco');
INSERT INTO categoria VALUES ('Chocolate Preto');
INSERT INTO categoria VALUES ('Pastilhas');
INSERT INTO categoria VALUES ('Frutos');
INSERT INTO categoria VALUES ('Frutos Secos');
INSERT INTO categoria VALUES ('Frutos Carnosos');
INSERT INTO categoria VALUES ('Bolachas');
INSERT INTO categoria VALUES ('Bolachas de chocolate');
INSERT INTO categoria VALUES ('Bolachas Simples');


INSERT INTO ponto_de_retalho VALUES ('Campo Pequeno', 'Lisboa', 'Avenidas Novas');
INSERT INTO ponto_de_retalho VALUES ('Atrium', 'Lisboa', 'Avenidas Novas');
INSERT INTO ponto_de_retalho VALUES ('Vasco da Gama','Lisboa', 'Parque das Nacoes');
INSERT INTO ponto_de_retalho VALUES ('Colombo', 'Lisboa', 'Carnide');
INSERT INTO ponto_de_retalho VALUES ('Tecnico-Alameda', 'Lisboa', 'Areeiro');
INSERT INTO ponto_de_retalho VALUES ('Aeroporto', 'Lisboa', 'Olivais');

INSERT INTO responsavel_por VALUES ('Doces', 4567138173633, 0494020048294, 0304817351445);
INSERT INTO responsavel_por VALUES ('Chocolates', 8947176121535, 0918491856738, 0213131231445);
INSERT INTO responsavel_por VALUES ('Pastilhas', 0398538209201, 9834958728874, 0428432342069);
INSERT INTO responsavel_por VALUES ('Frutos Carnosos', 4029741625241, 1903198740292, 1113123137372);
INSERT INTO responsavel_por VALUES ('Frutos Secos', 4737463651525, 7817328713616, 3167465372049);
INSERT INTO responsavel_por VALUES ('Frutos', 8947176121535, 9834958728874, 0428432342069);
INSERT INTO responsavel_por VALUES ('Frutos', 4567138173633, 1213516471921, 3198784924672);
INSERT INTO responsavel_por VALUES ('Pastilhas', 4737463651525, 0918491856738, 0213131231445);
INSERT INTO responsavel_por VALUES ('Pastilhas', 4029741625241, 1213516471921, 3198784924672);
INSERT INTO responsavel_por VALUES ('Bolachas', 0398538209201, 8101302193109, 4234726913132);
INSERT INTO responsavel_por VALUES ('Bolachas de chocolate', 4567138173633, 8101302193109, 4234726913132);
INSERT INTO responsavel_por VALUES ('Bolachas Simples', 0398538209201, 1903198740292, 1113123137372);

INSERT INTO responsavel_por VALUES ('Chocolate Branco', 4567138173633, 0494020048294, 0304817351445);
INSERT INTO responsavel_por VALUES ('Chocolate Preto', 4567138173633, 0494020048294, 0304817351445);
INSERT INTO responsavel_por VALUES ('Pastilhas', 4567138173633, 0494020048294, 0304817351445);
INSERT INTO responsavel_por VALUES ('Frutos Secos', 4567138173633, 0494020048294, 0304817351445);
INSERT INTO responsavel_por VALUES ('Frutos Carnosos', 4567138173633, 0494020048294, 0304817351445);
INSERT INTO responsavel_por VALUES ('Bolachas Simples', 4567138173633, 0494020048294, 0304817351445);


INSERT INTO prateleira VALUES (3, 1213516471921, 3198784924672, 15.5, 'Chocolates');
INSERT INTO prateleira VALUES (2, 7817328713616, 3167465372049, 15.5, 'Chocolates');
INSERT INTO prateleira VALUES (1, 9834958728874, 0428432342069, 13.5, 'Pastilhas');
INSERT INTO prateleira VALUES (5, 8101302193109, 4234726913132, 13.5, 'Pastilhas');
INSERT INTO prateleira VALUES (2, 1903198740292, 1113123137372, 15, 'Bolachas Simples');
INSERT INTO prateleira VALUES (4, 0918491856738, 0213131231445, 15, 'Bolachas');
INSERT INTO prateleira VALUES (1, 0494020048294, 0304817351445, 13.3, 'Frutos');
INSERT INTO prateleira VALUES (3, 0918491856738, 0213131231445, 13.3, 'Doces');
INSERT INTO prateleira VALUES (1, 1903198740292, 1113123137372, 13.3, 'Bolachas de chocolate');
INSERT INTO prateleira VALUES (6, 7817328713616, 3167465372049, 10, 'Frutos Carnosos');


INSERT INTO produto VALUES (0001112223334 ,'Chocolates' , 'Chocolate Continente');
INSERT INTO produto VALUES (2135718965342 ,'Chocolates' , 'Chocolate Pingo Doce');
INSERT INTO produto VALUES (0192346471938 ,'Pastilhas' , 'Pastilhas de Mentol');
INSERT INTO produto VALUES (3728472462789 ,'Pastilhas' , 'Pastilhas de Morango');
INSERT INTO produto VALUES (0954985928470 ,'Frutos Secos' , 'AmÃªndoas');
INSERT INTO produto VALUES (0043929405010 ,'Frutos Secos' , 'Cajus');
INSERT INTO produto VALUES (1010239184019 ,'Frutos Carnosos' , 'Laranja');
INSERT INTO produto VALUES (8317382193764 ,'Frutos Carnosos' , 'Uva');
INSERT INTO produto VALUES (1010239184010 ,'Bolachas de chocolate' , 'Bolachas de chocolate Continente');
INSERT INTO produto VALUES (1010239184018 ,'Bolachas de chocolate' , 'Bolachas de chocolate Pingo Doce');
INSERT INTO produto VALUES (0395376232642 ,'Bolachas Simples' , 'Bolachas Simples Continente');
INSERT INTO produto VALUES (1392371735214 ,'Bolachas Simples' , 'Bolachas Simples Pingo Doce');


INSERT INTO tem_categoria VALUES (0001112223334 ,'Chocolates');
INSERT INTO tem_categoria VALUES (2135718965342 ,'Chocolates');
INSERT INTO tem_categoria VALUES (0192346471938 ,'Pastilhas');
INSERT INTO tem_categoria VALUES (3728472462789 ,'Pastilhas');
INSERT INTO tem_categoria VALUES (0954985928470 ,'Frutos Secos');
INSERT INTO tem_categoria VALUES (0043929405010 ,'Frutos Secos');
INSERT INTO tem_categoria VALUES (1010239184019 ,'Frutos Carnosos');
INSERT INTO tem_categoria VALUES (1392371735214 ,'Frutos Carnosos');
INSERT INTO tem_categoria VALUES (1010239184010 ,'Bolachas de chocolate');
INSERT INTO tem_categoria VALUES (1010239184018 ,'Bolachas de chocolate');
INSERT INTO tem_categoria VALUES (0395376232642 ,'Bolachas Simples');
INSERT INTO tem_categoria VALUES (8317382193764 ,'Bolachas Simples');


INSERT INTO planograma VALUES (0001112223334, 3, 1213516471921, 3198784924672, 1, 10, 'Colombo');
INSERT INTO planograma VALUES (2135718965342, 2, 7817328713616, 3167465372049, 2, 2, 'Colombo');
INSERT INTO planograma VALUES (0192346471938, 1, 9834958728874, 0428432342069, 4, 4, 'Tecnico-Alameda');
INSERT INTO planograma VALUES (3728472462789, 5, 8101302193109, 4234726913132, 3, 8, 'Vasco da Gama');
INSERT INTO planograma VALUES (0954985928470, 2, 1903198740292, 1113123137372, 1, 7, 'Vasco da Gama');
INSERT INTO planograma VALUES (0043929405010, 4, 0918491856738, 0213131231445, 2, 12, 'Vasco da Gama');
INSERT INTO planograma VALUES (1010239184019, 1, 0494020048294, 0304817351445, 4, 5, 'Atrium');
INSERT INTO planograma VALUES (8317382193764, 3, 0918491856738, 0213131231445, 1, 2, 'Campo Pequeno');
INSERT INTO planograma VALUES (1392371735214, 1, 1903198740292, 1113123137372, 1, 3, 'Campo Pequeno');
INSERT INTO planograma VALUES (0395376232642, 6, 7817328713616, 3167465372049, 2, 15, 'Aeroporto');


INSERT INTO evento_reposicao VALUES (0001112223334, 3, 1213516471921, 3198784924672, '2021-12-31', 5, 4567138173633);
INSERT INTO evento_reposicao VALUES (2135718965342, 2, 7817328713616, 3167465372049, '2021-11-23', 1, 8947176121535);
INSERT INTO evento_reposicao VALUES (0192346471938, 1, 9834958728874, 0428432342069, '2022-02-23', 3, 4029741625241);
INSERT INTO evento_reposicao VALUES (3728472462789, 5, 8101302193109, 4234726913132, '2022-05-21', 8, 4567138173633);
INSERT INTO evento_reposicao VALUES (0954985928470, 2, 1903198740292, 1113123137372, '2021-01-08', 7, 4029741625241);
INSERT INTO evento_reposicao VALUES (0043929405010, 4, 0918491856738, 0213131231445, '2020-12-25', 12, 4737463651525);
INSERT INTO evento_reposicao VALUES (1010239184019, 1, 0494020048294, 0304817351445, '2022-06-15', 5, 0398538209201);
INSERT INTO evento_reposicao VALUES (8317382193764, 3, 0918491856738, 0213131231445, '2021-06-15', 2, 0398538209201);
INSERT INTO evento_reposicao VALUES (1392371735214, 1, 1903198740292, 1113123137372, '2021-01-01', 2, 8947176121535);
INSERT INTO evento_reposicao VALUES (0395376232642, 6, 7817328713616, 3167465372049, '2022-03-04', 13, 8947176121535);
INSERT INTO evento_reposicao VALUES (0043929405010, 4, 0918491856738, 0213131231445, '2021-01-25', 10, 4029741625241);
INSERT INTO evento_reposicao VALUES (0192346471938, 1, 9834958728874, 0428432342069, '2022-04-19', 2, 4737463651525);
INSERT INTO evento_reposicao VALUES (1392371735214, 1, 1903198740292, 1113123137372, '2020-10-10', 1, 0398538209201);


INSERT INTO instalada_em VALUES (0494020048294, 0304817351445, 'Campo Pequeno');
INSERT INTO instalada_em VALUES (0918491856738, 0213131231445, 'Atrium');
INSERT INTO instalada_em VALUES (1903198740292, 1113123137372, 'Colombo');
INSERT INTO instalada_em VALUES (8101302193109, 4234726913132, 'Colombo');
INSERT INTO instalada_em VALUES (9834958728874, 0428432342069, 'Aeroporto');
INSERT INTO instalada_em VALUES (1213516471921, 3198784924672, 'Vasco da Gama');
INSERT INTO instalada_em VALUES (7817328713616, 3167465372049, 'Tecnico-Alameda');


INSERT INTO categoria_simples VALUES ('Chocolate Branco');
INSERT INTO categoria_simples VALUES ('Chocolate Preto');
INSERT INTO categoria_simples VALUES ('Pastilhas');
INSERT INTO categoria_simples VALUES ('Frutos Secos');
INSERT INTO categoria_simples VALUES ('Frutos Carnosos');
INSERT INTO categoria_simples VALUES ('Bolachas de chocolate');
INSERT INTO categoria_simples VALUES ('Bolachas Simples');


INSERT INTO super_categoria VALUES ('Chocolates');
INSERT INTO super_categoria VALUES ('Doces');
INSERT INTO super_categoria VALUES ('Frutos');
INSERT INTO super_categoria VALUES ('Bolachas');


INSERT INTO tem_outra VALUES ('Chocolates', 'Chocolate Branco');
INSERT INTO tem_outra VALUES ('Chocolates', 'Chocolate Preto');
INSERT INTO tem_outra VALUES ('Doces', 'Chocolates');
INSERT INTO tem_outra VALUES ('Doces', 'Pastilhas');
INSERT INTO tem_outra VALUES ('Frutos', 'Frutos Secos');
INSERT INTO tem_outra VALUES ('Frutos', 'Frutos Carnosos');
INSERT INTO tem_outra VALUES ('Bolachas', 'Bolachas de chocolate');
INSERT INTO tem_outra VALUES ('Bolachas', 'Bolachas Simples');

/*
INSERT INTO tem_outra VALUES ('Pastilhas', 'Doces');
INSERT INTO evento_reposicao VALUES (2135718965342, 2, 7817328713616, 3167465372049, '2021-11-24', 111, 8947176121535);
INSERT INTO evento_reposicao VALUES (2135718965342, 1, 9834958728874, 0428432342069, '2021/11/24', 1, 8947176121535);
*/