
CREATE OR REPLACE VIEW vendas(ean, cat, ano, trimestre, mes, dia_mes, dia_semana, distrito, concelho, unidades) AS
    SELECT ean, cat, EXTRACT(YEAR FROM instante), EXTRACT(QUARTER FROM instante), 
        EXTRACT(MONTH FROM instante), EXTRACT(DAY FROM instante), 
        EXTRACT(DOW FROM instante), distrito, concelho, unidades
    FROM evento_reposicao NATURAL JOIN produto NATURAL JOIN instalada_em NATURAL JOIN ponto_de_retalho;

