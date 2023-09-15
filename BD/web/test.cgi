#!/usr/bin/python3
from wsgiref.handlers import CGIHandler
from flask import Flask
from flask import render_template, request, redirect, url_for
import psycopg2
import psycopg2.extras

## SGBD configs
DB_HOST = "db.tecnico.ulisboa.pt"
DB_USER = "ist199100"
DB_DATABASE = DB_USER
DB_PASSWORD = "1535388249"
DB_CONNECTION_STRING = "host=%s dbname=%s user=%s password=%s" % (
    DB_HOST,
    DB_DATABASE,
    DB_USER,
    DB_PASSWORD,
)

app = Flask(__name__)



@app.route('/')
def index():
  try:
    return render_template("index.html", params=request.args)
  except Exception as e:
    return str(e)

@app.route('/menu')
def menu():
  try:
    return render_template("index.html", params=request.args)
  except Exception as e:
    return str(e)

@app.route("/categorias_simples")
def list_categorias_simples():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT * FROM categoria_simples;"
        cursor.execute(query)
        return render_template("categorias_simples.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route("/categorias_simples/remove")
def remove_categorias_simples():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "CREATE TABLE produtos_eliminar AS SELECT ean FROM tem_categoria WHERE nome = %s;\
            CREATE TABLE planograma_eliminar AS SELECT nro, num_serie, fabricante FROM prateleira WHERE nome = %s;\
            DELETE FROM tem_outra WHERE super_categoria = %s OR categoria = %s;\
            DELETE FROM categoria_simples WHERE nome = %s;\
            DELETE FROM evento_reposicao WHERE (nro, num_serie, fabricante) IN (SELECT * FROM planograma_eliminar) OR ean IN (SELECT * FROM produtos_eliminar);\
            DELETE FROM planograma WHERE (nro, num_serie, fabricante) IN (SELECT * FROM planograma_eliminar) OR ean IN (SELECT * FROM produtos_eliminar);\
            DELETE FROM tem_categoria WHERE nome = %s;\
            DELETE FROM produto WHERE ean IN (SELECT * FROM produtos_eliminar);\
            DELETE FROM prateleira WHERE nome = %s;\
            DELETE FROM responsavel_por WHERE nome_cat = %s;\
            DELETE FROM categoria WHERE nome = %s;\
            DROP TABLE produtos_eliminar;\
            DROP TABLE planograma_eliminar;"
        data = (request.args['categoria'], request.args['categoria'], \
                request.args['categoria'], request.args['categoria'], \
                request.args['categoria'], request.args['categoria'], \
                request.args['categoria'], request.args['categoria'], request.args['categoria'])
        cursor.execute(query, data)
        return redirect(url_for('list_categorias_simples'))
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()


@app.route("/categorias_simples/insert_form")
def categorias_simples_insert_form():
    try:
        return render_template("categorias_simples_insert.html", params=request.args)
    except Exception as e:
        return str(e)


@app.route('/categorias_simples/insert', methods=["POST"])
def categorias_simples_insert():
  dbConn=None
  cursor=None
  try:
    dbConn = psycopg2.connect(DB_CONNECTION_STRING)
    cursor = dbConn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    query = "INSERT INTO categoria VALUES (%s); INSERT INTO categoria_simples VALUES (%s);"
    data = (request.form["nome"], request.form["nome"])
    cursor.execute(query,data)
    return redirect(url_for('list_categorias_simples'))
  except Exception as e:
    return str(e) 
  finally:
    dbConn.commit()
    cursor.close()
    dbConn.close()


@app.route("/super_categorias/insert_form")
def super_categoria_insert_form():
    try:
        return render_template("super_categorias_insert.html", params=request.args)
    except Exception as e:
        return str(e)


@app.route('/super_categorias/insert', methods=["POST"])
def super_categoria_insert():
  dbConn=None
  cursor=None
  try:
    dbConn = psycopg2.connect(DB_CONNECTION_STRING)
    cursor = dbConn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    query = "INSERT INTO categoria VALUES (%s); INSERT INTO super_categoria VALUES (%s);"
    data = (request.form["nome"], request.form["nome"])
    cursor.execute(query,data)
    return redirect(url_for('list_super_categorias'))
  except Exception as e:
    return str(e) 
  finally:
    dbConn.commit()
    cursor.close()
    dbConn.close()

@app.route("/super_categorias/remove")
def remove_super_categorias():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "CREATE TABLE produtos_eliminar AS SELECT ean FROM tem_categoria WHERE nome = %s;\
            CREATE TABLE planograma_eliminar AS SELECT nro, num_serie, fabricante FROM prateleira WHERE nome = %s;\
            DELETE FROM tem_outra WHERE super_categoria = %s OR categoria = %s;\
            DELETE FROM super_categoria WHERE nome = %s;\
            DELETE FROM evento_reposicao WHERE (nro, num_serie, fabricante) IN (SELECT * FROM planograma_eliminar) OR ean IN (SELECT * FROM produtos_eliminar);\
            DELETE FROM planograma WHERE (nro, num_serie, fabricante) IN (SELECT * FROM planograma_eliminar) OR ean IN (SELECT * FROM produtos_eliminar);\
            DELETE FROM tem_categoria WHERE nome = %s;\
            DELETE FROM produto WHERE ean IN (SELECT * FROM produtos_eliminar);\
            DELETE FROM prateleira WHERE nome = %s;\
            DELETE FROM responsavel_por WHERE nome_cat = %s;\
            DELETE FROM categoria WHERE nome = %s;\
            DROP TABLE produtos_eliminar;\
            DROP TABLE planograma_eliminar;"
        data = (request.args['categoria'], request.args['categoria'], \
                request.args['categoria'], request.args['categoria'], \
                request.args['categoria'], request.args['categoria'], \
                request.args['categoria'], request.args['categoria'], request.args['categoria'])
        cursor.execute(query, data)
        return redirect(url_for('list_super_categorias'))
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()

@app.route('/super_categorias/insert_sub', methods=["POST"])
def super_categoria_insert_sub():
  dbConn=None
  cursor=None
  try:
    dbConn = psycopg2.connect(DB_CONNECTION_STRING)
    cursor = dbConn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    query = "INSERT INTO tem_outra VALUES (%s,%s);"
    data = (request.form["categoria"], request.form["subcategoria"])
    cursor.execute(query,data)
    return redirect(url_for('list_tem_outra'))
  except Exception as e:
    return str(e) 
  finally:
    dbConn.commit()
    cursor.close()
    dbConn.close()


@app.route("/super_categorias/insert_sub_form")
def super_categoria_insert_sub_form():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT * FROM categoria WHERE nome <> %s;"
        data = (request.args["categoria"],)
        cursor.execute(query, data)
        return render_template("super_categoria_insert_sub.html", cursor=cursor, params=request.args)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route('/super_categorias/remove_sub', methods=["POST"])
def super_categoria_remove_sub():
  dbConn=None
  cursor=None
  try:
    dbConn = psycopg2.connect(DB_CONNECTION_STRING)
    cursor = dbConn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    query = "DELETE FROM tem_outra WHERE categoria = %s;"
    data = (request.form["subcategoria"],)
    cursor.execute(query,data)
    return redirect(url_for('list_tem_outra'))
  except Exception as e:
    return str(e) 
  finally:
    dbConn.commit()
    cursor.close()
    dbConn.close()


@app.route("/super_categorias/remove_sub_form")
def super_categoria_remove_sub_form():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT categoria FROM tem_outra WHERE super_categoria = %s;"
        data = (request.args["categoria"],)
        cursor.execute(query, data)
        return render_template("super_categoria_remove_sub.html", cursor=cursor, params=request.args)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route("/super_categorias")
def list_super_categorias():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT * FROM super_categoria;"
        cursor.execute(query)
        return render_template("super_categorias.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()

@app.route("/super_categorias/listar_sub_categorias")
def list_sub_categorias():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "WITH RECURSIVE sub_cat AS \
            (SELECT categoria as c FROM tem_outra WHERE super_categoria = %s \
                UNION ALL \
            SELECT categoria FROM sub_cat, tem_outra WHERE sub_cat.c = tem_outra.super_categoria)\
            SELECT * FROM sub_cat;"
        data = (request.args["categoria"],)
        cursor.execute(query, data)
        return render_template("listar_sub_categorias.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()

@app.route("/tem_outra")
def list_tem_outra():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT * FROM tem_outra;"
        cursor.execute(query)
        return render_template("tem_outra.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()

@app.route("/ivms")
def list_ivm():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT * FROM IVM;"
        cursor.execute(query)
        return render_template("ivms.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route("/eventos_reposicao")
def list_eventos_reposicao():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT cat, SUM(unidades) AS Soma \
            FROM evento_reposicao INNER JOIN produto ON evento_reposicao.ean = produto.ean \
            WHERE num_serie = %s AND fabricante = %s GROUP BY cat;"
        data = (request.args['num_serie'], request.args['fabricante'])
        cursor.execute(query, data)
        return render_template("eventos_reposicao.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route("/retalhistas")
def list_retalhistas():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT * FROM retalhista;"
        cursor.execute(query)
        return render_template("retalhistas.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()

@app.route("/retalhistas/remove")
def remove_retalhistas():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "DELETE FROM evento_reposicao WHERE tin = %s; DELETE FROM responsavel_por WHERE tin = %s; DELETE FROM retalhista WHERE tin = %s;"
        data = (request.args['tin1'], request.args['tin2'], request.args['tin3'])
        cursor.execute(query, data)
        return redirect(url_for('list_retalhistas'))
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()


@app.route("/retalhistas/insert_form")
def retalhistas_insert_form():
    try:
        return render_template("retalhistas_insert.html", params=request.args)
    except Exception as e:
        return str(e)


@app.route('/retalhistas/insert', methods=["POST"])
def retalhistas_insert():
  dbConn=None
  cursor=None
  try:
    dbConn = psycopg2.connect(DB_CONNECTION_STRING)
    cursor = dbConn.cursor(cursor_factory = psycopg2.extras.DictCursor)
    query = "INSERT INTO retalhista VALUES (%s, %s);"
    data = (request.form["tin"], request.form["nome"])
    cursor.execute(query,data)
    return redirect(url_for('list_retalhistas'))
  except Exception as e:
    return str(e) 
  finally:
    dbConn.commit()
    cursor.close()
    dbConn.close()


@app.route("/responsavel_por")
def list_responsavel_por():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        query = "SELECT * FROM responsavel_por;"
        cursor.execute(query)
        return render_template("responsavel_por.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()



CGIHandler().run(app)
