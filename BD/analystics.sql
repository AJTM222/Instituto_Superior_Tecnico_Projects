SELECT concelho, dia_semana, SUM(unidades) 
FROM vendas
WHERE ano = 2022 AND mes = 6 AND dia_mes < 30 AND dia_mes > 0
GROUP BY 
    GROUPING SETS((dia_semana), (concelho), ());
    
SELECT distrito, concelho, cat, dia_semana, SUM(unidades) 
FROM vendas
GROUP BY 
    GROUPING SETS((distrito), (concelho), (cat), (dia_semana), ());

CREATE INDEX rindex ON responsavel_por using hash(tin);
CREATE INDEX rindex_aux ON responsavel_por using hash(nome_cat);

CREATE INDEX pindex ON produto using hash(cat);
CREATE INDEX pindex_aux ON produto using btree(cat, descr);

