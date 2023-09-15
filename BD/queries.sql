
SELECT nome
FROM (SELECT tin, COUNT(DISTINCT(nome_cat)) AS R FROM responsavel_por GROUP BY tin) AS L NATURAL JOIN retalhista 
WHERE R IN (SELECT MAX(C) FROM (SELECT tin, COUNT(DISTINCT(nome_cat)) AS C FROM responsavel_por GROUP BY tin) AS LL);

SELECT nome
FROM retalhista AS R
WHERE NOT EXISTS ((SELECT nome FROM categoria_simples AS C)
    EXCEPT
    (SELECT nome_cat FROM responsavel_por RP WHERE R.tin = RP.tin));

SELECT ean 
FROM produto
WHERE ean NOT IN (SELECT ean FROM evento_reposicao);

SELECT ean
FROM (SELECT ean, COUNT(DISTINCT(tin)) AS C FROM evento_reposicao GROUP BY ean) AS B
WHERE C = 1;