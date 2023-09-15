#Artur Monteiro IST 199182

def eh_tabuleiro(tab):
    # tabuleiro -> logico
    """
    Esta funcao recebe um valor de de qualquer tipo
    e devolve True caso este seja um tabuleiro
    e False caso nao seja
    """
    res = False
    if isinstance(tab, tuple):
        if len(tab) == 3:
            for e in range(len(tab)):
                if isinstance(tab[0],tuple) and isinstance(tab[1],tuple) and \
                isinstance(tab[2],tuple):
                    if len(tab[0])==3 and len(tab[1])==3 and len(tab[2])==3:
                        for i in range(len(tab[e])):
                            if type(tab[e][i]) == int:
                                res = True
                            else:
                                return False
                            if tab[e][i] not in (-1, 0, 1):
                                return False    
    return res


def eh_posicao(n):
    # posicao -> logico
    """
    Esta funcao recebe um argumento de qualquer tipo
    devolvendo True se for uma posicao (inteiro de 1 a 9)
    ou False caso nao seja
    """
    if type(n) == int:
        if 1 <= n <= 9:
            return True
    return False


def obter_coluna(tab, i):
    # tabuleiro x int -> coluna
    """
    Esta funcao recebe um tabuleiro e um inteiro de 1 a 3
    e devolve a coluna i pertencente ao tabuleiro
    ou devolve um ValueError caso um dos argumentos seja invalido
    """
    res = ()
    if type(i) == int and 1 <= i <= 3:
        if eh_tabuleiro(tab):
            return (tab[0][i-1],) + (tab[1][i-1],) + (tab[2][i-1],)
    raise ValueError ('obter_coluna: algum dos argumentos e invalido')


def obter_linha(tab, i):
    # tabuleiro x int -> linha
    """
    Esta funcao recebe um tabuleiro e um inteiro de 1 a 3
    e devolve a linha i pertencente ao tabuleiro
    ou devolve um ValueError caso um dos argumentos seja invalido
    """    
    if type(i) == int and 1 <= i <= 3:
        if eh_tabuleiro(tab):
            return tab[i-1]
    raise ValueError ('obter_linha: algum dos argumentos e invalido')


def obter_diagonal(tab, i):
    # tabuleiro x int -> diagonal
    """
    Esta funcao recebe um tabuleiro e um inteiro de 1 a 2
    e devolve a diagonal i pertencente ao tabuleiro
    ou devolve um ValueError caso um dos argumentos seja invalido
    """    
    if type(i) == int and 1 <= i <= 2:
        if eh_tabuleiro(tab):
            if i == 1:
                return (tab[0][0],) + (tab[1][1],) + (tab[2][2],)
            else:
                return (tab[2][0],) + (tab[1][1],) + (tab[0][2],)
    raise ValueError ('obter_diagonal: algum dos argumentos e invalido')


def tabuleiro_str(tab):
    # tabuleiro -> cadeia de caracteres
    """
    Esta funcao recebe um tabuleiro 
    e devolve a cadeia de caracteres correspondente
    ou um ValueError caso o argumento seja invalido
    """
    linha = ''
    res = ''
    if eh_tabuleiro(tab):
        for e in range(len(tab)):
            for i in range(len(tab[e])):
                if tab[e][i] == -1:
                    sin = ' O '
                elif tab[e][i] == 0:
                    sin = '   '
                else:
                    sin = ' X '
                linha = linha + sin
                if i < len(tab[e]) -1 :
                    linha = linha + '|'
            res = res + linha 
            if e < len(tab) - 1:
                res = res + '\n-----------\n'
            linha = ''
        return res
    else:
        raise ValueError ('tabuleiro_str: o argumento e invalido')
            
        
def eh_posicao_livre(tab, pos):
    # tabuleiro x posicao -> logico
    """
    Esta funcao recebe um tabuleiro e uma posicao
    devolvendo True caso esta seja livre, ou seja,
    caso nessa posicao esteja um 0
    se pelo menos um dos argumentos for invalido
    a funcao devolve um ValueError
    """
    if eh_tabuleiro(tab):
        if eh_posicao(pos):
            if pos<= 3:
                i = 0 
                pos = pos -1
            elif 3< pos <= 6:
                pos = pos -4
                i = 1
            else :
                pos = pos - 7
                i = 2
            if tab[i][pos] == 0:
                return True
            else:
                return False
    raise ValueError ('eh_posicao_livre: algum dos argumentos e invalido')


def obter_posicoes_livres(tab):
    # tabuleiro -> tuplo
    """
    Esta funcao recebe um tabuleiro
    e devolve um tuplo contendo todas as
    posicoes livres existentes no tabuleiro
    caso o argumento seja invalido devolve um ValueError
    """
    if eh_tabuleiro(tab):
        i = 9
        res = ()
        while i > 0:
            if eh_posicao_livre(tab, i):
                res = (i,) + res
            i = i-1
        return res
    raise ValueError ('obter_posicoes_livres: o argumento e invalido')
            

def jogador_ganhador(tab):
    # tabuleiro -> inteiro
    """
    Esta funcao recebe um tabuleiro 
    e devolve um inteiro representante do vencedor,
    1 caso X tenha vencido, -1 caso O tenha vencido
    ou 0 caso seja um empate
    se o argumento for invalido devolve um ValueError
    """
    if eh_tabuleiro(tab):
        for i in range(1,4):
            for e in range(1,3):
                if obter_coluna(tab, i) == (-1, -1, -1):
                    return -1
                elif obter_linha(tab, i) == (-1, -1, -1):
                    return -1
                elif obter_diagonal(tab, e) == (-1, -1, -1):
                    return -1
                if obter_coluna(tab, i) == (1,1,1):
                    return 1
                elif obter_linha(tab, i) == (1,1,1):
                    return 1
                elif obter_diagonal(tab, e) == (1,1,1):
                    return 1
        return 0
    raise ValueError ('jogador_ganhador: o argumento e invalido')


def marcar_posicao(tab, i, pos):
    # tabuleiro x inteiro x posicao -> tabuleiro
    """
    A funcao recebe um tabuleiro, um inteiro representante
    do jogador e uma posicao e devolve um tabuleiro novo
    com o inteiro fornecido na posicao escolhida
    caso os argumentos nao sejam validos
    ou a posicao nao seja uma posicao livre
    devolve um ValueError
    """
    if eh_tabuleiro(tab):
        if i == 1 or i == -1:
            if type(i) == int:
                if eh_posicao(pos):
                    if eh_posicao_livre(tab, pos):
                        if pos<= 3:
                            el = 0 
                            pos = pos -1
                        elif 3< pos <= 6:
                            pos = pos -4
                            el = 1
                        else :
                            pos = pos - 7
                            el = 2
                        # neste caso foi alterado o tabuleiro para uma lista
                        # para se poder alterar a posicao escolhida
                        l1 = list(tab)
                        l2 = list(tab[el])
                        l2[pos] = i
                        tup2 = tuple(l2)
                        l1[el] = tup2
                        tab = tuple(l1)
                        return tab
    raise ValueError ('marcar_posicao: algum dos argumentos e invalido')
                    
                    
def escolher_posicao_manual(tab):
    # tabuleiro -> posicao
    """
    A funcao recebe um tabuleiro e devolve uma posicao
    escolhida pelo utilizador atraves de um eval(input)
    caso o argumento seja invalido ou a posicao escolhida
    nao seja uma posicao livre e levantado um ValueError
    """
    if eh_tabuleiro(tab):
        pos = eval(input('Turno do jogador. Escolha uma posicao livre: '))
        if eh_posicao(pos):
            if eh_posicao_livre(tab, pos):
                return pos
        raise ValueError('escolher_posicao_manual: a posicao introduzida e invalida')
    raise ValueError ('escolher_posicao_manual: o argumento e invalido')


def escolher_posicao_auto(tab, i, cad):
    # tabuleiro x inteiro x cadeia de caracters -> posicao
    """
    Esta funcao recebe um tabuleiro,
    um inteiro representante do jogador
    e uma cadeia de caracteres que represnta a dificuldade
    e devolve uma posicao escolhida automaticamente
    em funcao da dificuldade
    caso um dos argumentos seja invalido devolve um Value Error
    """
    if eh_tabuleiro(tab):
        if i == 1 or i == -1:
            if type(i) == int:
                # cada dificuldade tem uma funcao associada
                if cad == 'basico':
                    return basico(tab, i)
                if cad == 'normal':
                    return normal(tab, i)
                if cad == 'perfeito':
                    return perfeito(tab, i)
    raise ValueError('escolher_posicao_auto: algum dos argumentos e invalido')


def centro_livre(tab):
    # tabuleiro -> logico
    """
    Esta funcao recebe um tabuleiro
    e devolve True se a posicao 5 for livre
    e False caso nao seja
    """
    if eh_posicao_livre(tab,5):
        return True
    return False


def canto_livre(tab):
    # tabuleiro -> posicao
    """
    A funcao recebe um tabuleiro
    e o canto (1,3,7,9) cuja posicao tem o menor valor
    e e uma posicao livre
    """
    for i in (1,3,7,9):
            if eh_posicao_livre(tab, i):
                return i


def lado_livre(tab):
    # tabuleiro -> posicao
    """
    A funcao recebe um tabuleiro
    e o lado (2,4,6,8) cuja posicao tem o menor valor
    e e uma posicao livre
    """    
    for i in (2,4,6,8):
        if eh_posicao_livre(tab, i):
            return i

        
def soma_linha(lin):
    # tuplo -> inteiro
    """
    Esta funcao recebe uma linha, coluna ou diagonal
    e devolve o valor da soma de todos os elementos dessa linha
    """
    soma = 0
    for i in range(len(lin)):
        soma = soma + lin[i]
    return soma


def vitoria_bloqueio(tab, res):
    # tabuleiro x inteiro -> posicao
    """
    Esta funcao recebe um tabuleiro e um resultado
    e devolve uma posicao livre cuja soma de elemntos
    da linha, diagonal ou coluna onde esta
    seja igual ao resultado fornecido
    """
    for e in range(1,4):
        for m in range(1,3):
            # neste caso o resultado sera igual a 2i ou -2i
            # dependendo se o objetivo e vencer ou bloquear
            if soma_linha(obter_coluna(tab,e)) == res:
                for n in range(0, 3):
                    pos = n*3 + e
                    if eh_posicao_livre(tab, pos):
                        return pos
            if soma_linha(obter_linha(tab,e)) == res:
                for n in range(0, 3):
                    pos = n + 3 * (e -1) + 1
                    if eh_posicao_livre(tab, pos):
                        return pos
            if soma_linha(obter_diagonal(tab,m)) == res:
                if m == 1:
                    for n in (1, 5, 9):
                        if eh_posicao_livre(tab, n):
                            return n
                if m == 2:
                    for n in (3, 5, 7):
                        if eh_posicao_livre(tab, n):
                            return n    


def canto_oposto(tab, i):
    # tabuleiro x inteiro -> pos
    """
    A funcao recebe um tabuleiro e um inteiro
    representante do jogador e da uma posicao livre
    que esteja no canto oposto a uma posicao ocupada 
    pelo adversario, priorizando posicoes de valor menor
    """
    for e in (1,3,7,9):
        if tab[(e-1)//3][(e-1)%3] == -i:
            # 10 - e corresponde ao canto oposto
            if eh_posicao_livre(tab, 10 - e):
                return 10-e
            
     
def all_bifurcacoes(tab, i):
    # tabuleiro x inteiro -> tuplo
    """
    A funcao recebe um tabuleiro 
    e um inteiro representante do jogador e devolve
    um tuplo com todas as posicoes que causam uma bifurcacao
    """
    res = ()
    for pos in range(1, 10):
        if eh_posicao_livre(tab, pos):
            soma = 0
            # caso a soma dos elementos da linha seja i,
            # significa que a linha apenas tem um elemento
            # visto que pelo menos uma posicao esta livre
            if soma_linha(obter_linha(tab, (pos-1)//3 +1)) == i:
                # a soma representa o numero de linhas que
                # ficavam em estado de vitoria
                # precisando de ser 2 ou mais para ser uma bifurcacao
                soma = soma + 1
            if soma_linha(obter_coluna(tab, (pos-1)%3 +1)) == i:
                soma = soma +1
            if pos in (1, 5, 9):
                if soma_linha(obter_diagonal(tab, 1)) == i:
                    soma = soma +1
            if pos in (3, 5, 7):
                if soma_linha(obter_diagonal(tab, 2)) == i:
                    soma = soma +1
            if soma >= 2:
                res = res + (pos,)
    return res


def bifurcacao(tab, i):
    # tabuleiro x inteiro -> pos
    """
    A funcao recebe um tabuleiro
    e um inteiro representante de um jogador
    e devolve ,caso exista pelo menos uma,
    uma posicao livre que gere uma bifurcacao
    a favor do jogador i
    """
    if len(all_bifurcacoes(tab, i)) >= 1:
        return all_bifurcacoes(tab, i)[0]
    

def bloqueio_bifurcacao(tab, i):
    # tabuleiro x inteiro -> pos
    """
    A funcao recebe um tabuleiro
    e um inteiro representante de um jogador
    e devolve a posicao livre que geraria uma bifurcacao
    para o jogador -i, caso so exista uma,
    ou uma posicao que gere um dois em linha
    e que obrigue o jogador adversario a jogar numa
    posicao que nao gere uma bifurcacao
    """
    if len(all_bifurcacoes(tab, -i)) == 1:
        return all_bifurcacoes(tab, -i)[0]
    elif len(all_bifurcacoes(tab, -i)) > 1:
        for pos in range(1,10):
            if eh_posicao_livre(tab, pos):
                # perceber se jogar na posicao escolhida gera um 2 em linha
                if vitoria_bloqueio(marcar_posicao(tab, i, pos), 2*i):
                    x = vitoria_bloqueio(marcar_posicao(tab, i, pos), 2*i)
                    # perceber se a posicao onde o adversario
                    # e obrigado a jogar gera uma bifurcacao
                    if x not in all_bifurcacoes(tab, -i):
                        return pos
    
                        
def basico(tab,i):
    # tabuleiro x inteiro -> posicao
    """
    A funcao recebe um tabuleiro
    e um inteiro representante de um jogador
    e devolve uma posicao de acordo com as funcoes
    anteriormente definidas e com a ordem em que
    aparecem no enunciado do projeto na dificuldade basica
    """
    if centro_livre(tab):
        return 5
    if canto_livre(tab):
        return canto_livre(tab)
    if lado_livre(tab):
        return lado_livre(tab)

    
def normal(tab,i):
    # tabuleiro x inteiro -> posicao
    """
    A funcao recebe um tabuleiro
    e um inteiro representante de um jogador
    e devolve uma posicao de acordo com as funcoes
    anteriormente definidas e com a ordem em que
    aparecem no enunciado do projeto na dificuldade normal
    """    
    if vitoria_bloqueio(tab, 2* i):
        return vitoria_bloqueio(tab, 2* i)
    # a segunda funcao representa o bloqueio
    # pelo que o resultado pretendido tem de ser -2i
    if vitoria_bloqueio(tab, -2* i):
        return vitoria_bloqueio(tab, -2* i)
    if centro_livre(tab):
        return 5
    if canto_oposto(tab,i):
        return canto_oposto(tab,i)
    if canto_livre(tab):
        return canto_livre(tab)
    if lado_livre(tab):
        return lado_livre(tab)
    
    
def perfeito(tab ,i):
    # tabuleiro x inteiro -> posicao
    """
    A funcao recebe um tabuleiro
    e um inteiro representante de um jogador
    e devolve uma posicao de acordo com as funcoes
    anteriormente definidas e com a ordem em que
    aparecem no enunciado do projeto na dificuldade perfeita
    """    
    if vitoria_bloqueio(tab, 2* i):
        return vitoria_bloqueio(tab, 2* i)
    # a segunda funcao representa o bloqueio
    # pelo que o resultado pretendido tem de ser -2i    
    if vitoria_bloqueio(tab, -2* i):
        return vitoria_bloqueio(tab, -2* i)
    if bifurcacao(tab, i):
        return bifurcacao(tab, i)
    if bloqueio_bifurcacao(tab, i):
        return bloqueio_bifurcacao(tab, i)
    if centro_livre(tab):
        return 5
    if canto_oposto(tab,i):
        return canto_oposto(tab,i)
    if canto_livre(tab):
        return canto_livre(tab)
    if lado_livre(tab):
        return lado_livre(tab)  


def simbolo_vencedor(tab):
    # tabuleiro -> cadeia de caracteres
    """
    Recebe um tabuleiro e devolve,
    atraves da funcao jogador_ganhador
    o simbolo do jogador que venceu a partida
    ou no caso de empate a cadeia de caracteres EMPATE
    """
    if jogador_ganhador(tab) == 1:
        return 'X'
    if jogador_ganhador(tab) == -1:
        return 'O' 
    if jogador_ganhador(tab) == 0:
        return 'EMPATE'    


def jogo_do_galo(simb, dif):
    # simbolo x dificuldade -> cadeia de caracteres representantes do jogo
    """
    Esta funcao recebe duas cadeias de caracteres,
    uma representa o simbolo do jogador 
    e a outra a dificuldade do jogo.
    Atraves de todas as funcoes anteriormente apresentadas
    esta funcao cria um jogo do galo jogavel
    com uma representacao agradavel para os nossos olhos.
    Caso um dos argumentos seja invalido
    e levantado um ValueError.
    """
    if simb == 'X' or simb == 'O':
        if dif == 'basico' or  dif == 'normal' or dif == 'perfeito':            
            print('Bem-vindo ao JOGO DO GALO.')
            print('O jogador joga com', "'"+simb+"'"'.')
            if simb == 'O':
                simb = -1
                adv = 1
            elif simb == 'X':
                simb = 1
                adv = -1
            tab = ((0,0,0),(0,0,0),(0,0,0))
            while len(obter_posicoes_livres(tab)) != 0:
                if simb == 1:
                    tab=marcar_posicao(tab,simb,escolher_posicao_manual(tab))
                    print(tabuleiro_str(tab))
                    # verificar se alguem ganhou ao fim de cada jogada
                    if len(obter_posicoes_livres(tab))==0:
                        return simbolo_vencedor(tab)
                    if jogador_ganhador(tab) != 0:
                        return simbolo_vencedor(tab)
                    print('Turno do computador', '('+(dif)+'):')
                    tab=marcar_posicao(tab,adv,escolher_posicao_auto(tab,adv,dif))
                    print(tabuleiro_str(tab))
                    if jogador_ganhador(tab) != 0:
                        # nao e preciso verificar o caso de empate
                        # visto que isso acontcera antes de chegar a este ponto
                        # uma vez acontecera num numero impar de jogadas
                        return simbolo_vencedor(tab)
                if simb == -1:
                    print('Turno do computador', '('+(dif)+'):')
                    tab=marcar_posicao(tab,adv,escolher_posicao_auto(tab,adv,dif))
                    print(tabuleiro_str(tab))
                    if len(obter_posicoes_livres(tab))==0:
                        return simbolo_vencedor(tab)
                    if jogador_ganhador(tab) != 0:
                        return simbolo_vencedor(tab)
                    tab=marcar_posicao(tab,simb,escolher_posicao_manual(tab))
                    print(tabuleiro_str(tab))
                    if jogador_ganhador(tab) != 0:
                        return simbolo_vencedor(tab)                                    
    raise ValueError('jogo_do_galo: algum dos argumentos e invalido')