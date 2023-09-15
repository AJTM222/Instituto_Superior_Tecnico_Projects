DROP TRIGGER IF EXISTS ri1 ON tem_outra;
DROP TRIGGER IF EXISTS ri4 ON evento_reposicao;
DROP TRIGGER IF EXISTS ri5 ON evento_reposicao;

CREATE OR REPLACE FUNCTION ri1() 
RETURNS TRIGGER AS $$
    DECLARE nome_cat VARCHAR(80) := NEW.super_categoria;
BEGIN
    LOOP
        IF nome_cat = NEW.categoria
        THEN
            RAISE EXCEPTION 'ERRO';
        END IF;

        IF nome_cat IN (SELECT categoria FROM tem_outra)
        THEN
            SELECT super_categoria INTO nome_cat FROM tem_outra WHERE categoria = nome_cat;
        ELSE
            EXIT;
        END IF;
    END LOOP;
    RETURN NEW;
END;
$$ LANGUAGE	plpgsql;
CREATE TRIGGER ri1 BEFORE INSERT OR UPDATE ON tem_outra
FOR EACH ROW EXECUTE PROCEDURE ri1();


CREATE OR REPLACE FUNCTION ri4() RETURNS TRIGGER AS $$
DECLARE unit INT;
BEGIN
    SELECT unidades INTO unit FROM planograma WHERE (ean = NEW.ean AND nro = NEW.nro AND num_serie = NEW.num_serie AND fabricante = NEW.fabricante);
    IF NEW.unidades > unit
    THEN
        RAISE EXCEPTION 'ERRO';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE    plpgsql;
CREATE TRIGGER ri4 BEFORE INSERT OR UPDATE ON evento_reposicao
FOR EACH ROW EXECUTE PROCEDURE ri4();


CREATE OR REPLACE FUNCTION ri5() RETURNS TRIGGER AS $$
DECLARE category VARCHAR(80);
BEGIN
    SELECT cat INTO category FROM produto WHERE NEW.ean = produto.ean; 
    IF category NOT IN (SELECT cat FROM produto NATURAL JOIN planograma WHERE nro = NEW.nro AND num_serie = NEW.num_serie AND fabricante = NEW.fabricante)
    THEN
        RAISE EXCEPTION 'ERRO';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE	plpgsql;
CREATE TRIGGER ri5 BEFORE INSERT OR UPDATE ON evento_reposicao
FOR EACH ROW EXECUTE PROCEDURE ri5();