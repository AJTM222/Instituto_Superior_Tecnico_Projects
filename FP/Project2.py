#Artur Monteiro IST 199182

# TAD posicao -> lista de um elemento ( inteiro de 1 a 9)
def cria_posicao(c,l):
    # string x string -> posicao
    """
    A funcao recebe como argumentos duas 
    cadeias de carateres que representam a coluna
    e a linha, respetivamente, de uma posicao
    e devolve essa mesma posicao.
    Caso um dos argumentos esteja errado
    a funcao gera um ValueError
    """
    if type(c) == str and c in ('a', 'b' ,'c'):
        if type(l) == str and l in ('1', '2' ,'3'):
            if c == 'a':
                return [int(l) * 3 - 2]
            if c == 'b':
                return [int(l) * 3 - 1]           
            return [int(l) * 3]  
    raise ValueError('cria_posicao: argumentos invalidos')

def cria_copia_posicao(pos):
    # posicao -> posicao
    """
    Esta funcao recebe uma posicao e
    devolve uma copia da mesma
    """
    res = []
    res.append(pos[0])
    return res
        
def obter_pos_c(pos):
    # posicao -> string
    """
    Esta funcao recebe uma posicao
    e devolve a sua componente coluna
    """
    if pos[0] % 3 == 1:
        return 'a'
    if pos[0] % 3 == 2:
        return 'b'
    return 'c'

def obter_pos_l(pos):
    # posicao -> string
    """
    Esta funcao recebe uma posicao
    e devolve a sua componente linha
    """    
    if pos[0]< 4:
        return '1'
    if pos[0]< 7:
        return '2'
    return '3'

def eh_posicao(pos):
    # universal -> booleano
    """
    Recebe como argumento um valor universal
    e devolve True caso seja um TAD posicao
    e False caso nao o seja
    """
    if type(pos) == list:
        if len(pos) == 1:
            if type(pos[0]) == int:
                if 0<pos[0]<10:
                    return True
    return False

def posicoes_iguais(pos1,pos2):
    # posicao x posicao -> booleano
    """
    A funcao recebe duas posicoes e 
    devolve True caso sejam posicoes iguais
    e False caso contrario
    """
    if eh_posicao(pos1) and eh_posicao(pos2):
        return pos1[0] == pos2[0]
    return False

def posicao_para_str(pos):
    # posicao -> string
    """
    Esta funcao recbe uma posicao e 
    devolve a cadeia de caracteres corresondente
    a juncao da coluna e da linha dessa posicao
    """    
    return '' + obter_pos_c(pos) + obter_pos_l(pos)

def pos_int(pos):
    # posicao -> inteiro
    """
    Esta funca recebe uma posicao e devolve
    o inteiro correspondente a essa posicao
    """
    if obter_pos_c(pos) == 'a':
        return int(obter_pos_l(pos)) * 3 - 2
    if obter_pos_c(pos) == 'b':
        return int(obter_pos_l(pos)) * 3 - 1
    if obter_pos_c(pos) == 'c':
        return int(obter_pos_l(pos)) * 3

def int_pos(inteiro):
    # inteiro -> posicao
    """
    Esta funcao recebe um inteiro
    e devolve a posicao correspondente
    """
    if inteiro in range(1,10):
        if inteiro % 3 == 1:
            c = 'a'
        elif inteiro % 3 == 2:
            c = 'b'
        else:
            c = 'c'
        l = str(((inteiro - 1) // 3) + 1)
        return cria_posicao(c,l)
        
def obter_posicoes_adjacentes(pos):
    # posicao -> tuplo
    """
    A funcao recebe uma posicao e devolve
    um tuplo com todas as posicoes adjacentes
    a essa posicao, pela ordem em que 
    aparecem no tabuleiro
    """
    res = () 
    for e in (-4,-3,-2,-1,1,2,3,4):
        adj = int_pos(pos_int(pos)+ e)
        if eh_posicao(adj):
            # escolhem-se apenas as posicoes que sejam da mesma coluna,
            # linha ou sejam a posicao central que e adjacente de todas            
            if obter_pos_c(adj) == obter_pos_c(pos)\
            or pos_int(pos) == 5 or pos_int(pos) + e == 5\
            or obter_pos_l(adj) == obter_pos_l(pos):
                # para evitar ter pecas que nao sao adjacentes
                # apenas se pode somar/subtrair valores pares
                # caso o resultado seja a posicao central
                # ou essa seja a posicao de partida
                if (e!= -4 and e!= -2 and e !=2 and e !=4 )\
                or pos_int(pos) + e == 5 or pos_int(pos) == 5:
                    res += (adj,)
    return res

# TAD peca -> lista de um elemento (1, -1 ou 0)                  
def cria_peca(jog):
    # string -> peca
    """
    Recebe uma cadeia de caracteres que 
    corresponde a um jogador ou a uma peca vazia
    e devolve a peca correspondente.
    Caso o argumento nao esteja correto
    a funcao gera um ValueError
    """
    if type(jog) == str:
        if jog == 'X':
            return [1]
        if jog == 'O':
            return [-1]
        if jog == ' ':
            return [0]
    raise ValueError('cria_peca: argumento invalido')

def cria_copia_peca(peca):
    # peca -> peca
    """
    Recebe um apeca e devolve
    uma copia da mesma
    """
    copia = []
    copia.append(peca[0])
    return copia

def eh_peca(peca):
    # universal -> booleano
    """
    Recebe um valor universal e devolve
    True caso este seja um TAD peca
    e False caso contrerio
    """
    if type(peca) == list:
        if len(peca) == 1:
            if type(peca[0]) == int:
                if peca[0] == -1 or peca[0] == 1 or peca[0] == 0:
                    return True
    return False

def pecas_iguais(peca1, peca2):
    # peca x peca -> booleano
    """
    Recebe duas pecas (peca1 e peca2)
    e devolve True apenas se forem iguais 
    """
    if eh_peca(peca1) and eh_peca(peca2):
        return peca1 == peca2
    return False

def peca_para_str(peca):
    # peca -> string
    """
    Recebe uma peca e devolve a cadeia de caracteres
    que representa o jogador ('[X]','[O]','[ ]')
    """
    if peca == [1]:
        return '[X]'
    if peca == [-1]:
        return '[O]'
    if peca == [0]:
        return '[ ]'
    
def peca_para_inteiro(peca):
    # peca -> inteiro
    """
    Recebe uma peca e devolve
    o inteiro que a representa
    (1 para 'X', -1 para 'O' e 0 para ' ')
    """
    if peca_para_str(peca) == '[X]':
        return 1
    if peca_para_str(peca) == '[O]':
        return -1
    if peca_para_str(peca) == '[ ]':
        return 0    

# TAD tabuleiro -> lista de listas com elementos respresentantes
# das pecas que preenchem essas posicoes ( 0, -1, 1)
def cria_tabuleiro():
    # {} -> tabuleiro
    """
    Devolve um tabuleiro do 
    jogo do moinho vazio
    """
    return [[0,0,0],[0,0,0],[0,0,0]]
    
def cria_copia_tabuleiro(tab):
    # tabuleiro -> tabuleiro
    """
    Recebe um tabuleiro e devolve uma 
    copia desse tabuleiro
    """
    copia = []
    linha = []
    for lin in range (0,3):
        for el in range (0,3):
            linha.append(tab[lin][el])
        copia.append(linha)
        linha = []
    return copia

def obter_peca(tab,pos):
    # tabuleiro x posicao -> peca
    """
    Recebe um tabuleiro e uma posicao
    e devolve a peca que se encontra
    nessa posicao desse tabuleiro
    """
    if obter_pos_c(pos) == 'a':
        value = tab[int(obter_pos_l(pos)) - 1][0]
    elif obter_pos_c(pos) == 'b':
        value = tab[int(obter_pos_l(pos)) - 1][1]
    else:
        value = tab[int(obter_pos_l(pos)) - 1][2]
    if value == 1:
        return cria_peca('X')
    if value == -1:
        return cria_peca('O')
    return cria_peca(' ')

def obter_vetor(tab, vetor):
    # tabuleiro x string -> tuplo
    """
    Recebe um tabuleiro e uma cadeia
    de caracteres representante de uma 
    linha ou coluna e devolve um tuplo
    contendo todas as pecas nessa linha 
    ou coluna desse tabuleiro
    """
    res = ()
    for e in ('1', '2', '3'):
        for i in ('a','b','c'):
            if e == vetor:
                res += (obter_peca(tab,cria_posicao(i,e)),)
            if i == vetor:
                res += (obter_peca(tab,cria_posicao(i,e)),)
    return res

def coloca_peca(tab,peca,pos):
    # tabuleiro x peca x posicao -> tabuleiro
    """
    Recebe um tabuleiro, uma peca e uma 
    posicao e devolve um novo tabuleiro
    com a peca na posicao escolhida
    """
    if obter_pos_c(pos) == 'a':
        tab[int(obter_pos_l(pos)) - 1][0] = peca_para_inteiro(peca)
    elif obter_pos_c(pos) == 'b':
        tab[int(obter_pos_l(pos)) - 1][1] = peca_para_inteiro(peca)
    else:
        tab[int(obter_pos_l(pos)) - 1][2] = peca_para_inteiro(peca)
    return tab

def remove_peca(tab, pos):
    # tabuleiro x posicao -> tabuleiro
    """
    Recebe um tabuleiro e uma posicao
    e deveolve um novo tabuleiro
    resultante da remocao da peca
    que se encontra na posicao escolhida
    """
    if obter_pos_c(pos) == 'a':
        tab[int(obter_pos_l(pos)) - 1][0] = 0
    elif obter_pos_c(pos) == 'b':
        tab[int(obter_pos_l(pos)) - 1][1] = 0
    else:
        tab[int(obter_pos_l(pos)) - 1][2] = 0
    return tab   

def move_peca(tab, inicial, final):
    # tabuleiro x posicao x posicao -> tabuleiro
    """
    Recebe um tabuleiro e duas posicoes
    uma final e uma inicial, e devolve 
    um novo tebuleiro resultante da
    deslocacao da peca que se encontra na 
    posicao inicial para a posicao final
    """
    peca = obter_peca(tab,inicial)
    remove_peca(tab, inicial)
    coloca_peca(tab, peca, final)
    return tab

def vetor_pecas_iguais(vetor, peca):
    # vetor x peca -> booleano
    """
    A funcao recebe um vetor e uma peca
    e devolve True caso esse vetor tenha
    todos os elementos iguais ao TAD peca
    e False caso contrario
    """
    if pecas_iguais(vetor[0], peca) and pecas_iguais(vetor[1], peca) and\
       pecas_iguais(vetor[2], peca):
        return True
    return False

def eh_tabuleiro(arg):
    # universal -> booleano
    """
    Recebe um argumento universal e
    devolve True caso seja um TAD tabuleiro
    e False caso contrario
    """
    res = False
    soma = 0
    jogX = 0
    jogO = 0
    if type(arg) == list and len(arg) == 3:
        for e in range(len(arg)):
            if type(arg[0]) == list and type(arg[1]) == list and \
               type(arg[2]) == list:
                if len(arg[0])==3 and len(arg[1])==3 and len(arg[2])==3:
                    for i in range(len(arg[e])):
                        if type(arg[e][i]) == int:
                            res = True
                        else:
                            return False
                        if arg[e][i] not in (-1, 0, 1):
                            return False  
        for e in ('1','2','3'):
            for i in ('a', 'b', 'c'):
            # verifica se existem mais de tres pecas de algum jogador
                if pecas_iguais(obter_peca(arg, cria_posicao(i,e))\
                                ,cria_peca('X')):
                    jogX = jogX + 1
                if pecas_iguais(obter_peca(arg, cria_posicao(i,e))\
                                ,cria_peca('O')):
                    jogO = jogO + 1
        if jogO > 3 or jogX > 3:
            return False
                    # verifica se algum jogador tem 
                    # duas pecas a mais que o outro
        if (jogO < jogX and jogO + 1 < jogX) or\
           (jogO > jogX + 1 and jogO > jogX):
            return False
            # verifica se existe mais que um vencedor
        for vetor in ('1', '2', '3', 'a', 'b', 'c'):
            if vetor_pecas_iguais(obter_vetor(arg,vetor), cria_peca('O')) or\
               vetor_pecas_iguais(obter_vetor(arg,vetor), cria_peca('X')):
                soma += 1                       
        if soma == 2:
            return False                                
    return res

def eh_posicao_livre(tab, pos):
    # tabuleiro x posicao -> booleano
    """
    Recebe um tabuleiro e uma posicao
    e devolve True caso essa seja uma 
    posicao livre e False caso contrario
    """
    if pecas_iguais(obter_peca(tab,pos), cria_peca(' ')):
        return True
    return False

def tabuleiros_iguais(tab1, tab2):
    # tabuleiro x tabuleiro -> booleano
    """
    Recebe dois tabuleiros e devolve
    True caso sejam iguais e False caso contrario
    """
    if eh_tabuleiro(tab1) and eh_tabuleiro(tab1):
        return tab1 == tab2
    return False

def tabuleiro_para_str(tab):
    # tabuleiro -> string
    """
    Recebe um tabuleiro e devolve
    uma cadeia de caracteres que o representa
    """
    res = ''
    for i in ('1', '2', '3'):
        p1 = peca_para_str(obter_peca(tab,cria_posicao('a',i)))
        p2 = peca_para_str(obter_peca(tab,cria_posicao('b',i)))
        p3 = peca_para_str(obter_peca(tab,cria_posicao('c',i)))
        if i == '1':
            linha = '   a   b   c\n1 '+p1+'-'+p2+'-'+p3+'\n'
        elif i == '2':
            linha = '   | \\ | / |\n2 '+p1+'-'+p2+'-'+p3+'\n'
        else:
            linha = '   | / | \\ |\n3 '+p1+'-'+p2+'-'+p3
        res = res + linha
    return res

def tuplo_para_tabuleiro(tuplo):
    # tuplo -> tabuleiro
    """
    Recebe um tuplo que representa um 
    tabuleiro e devolve o tabuleiro correspondente 
    """
    tab = list(tuplo)
    for e in range(len(tuplo)):
        tab[e] = list(tab[e])
    return tab
        
def obter_ganhador(tab):
    # tabuleiro -> peca
    """
    Recebe um tabuleiro e devolve
    a peca que que representa o
    vencedor ou uma peca vazia
    caso nao haja vencedor
    """
    for vetor in ('a', 'b', 'c', '1', '2', '3'):
        if vetor_pecas_iguais(obter_vetor(tab,vetor), cria_peca('X')):
            return cria_peca('X')
        if vetor_pecas_iguais(obter_vetor(tab,vetor), cria_peca('O')):
            return cria_peca('O')
    return cria_peca(' ')

def obter_posicoes(tab, peca):
    # tabuleiro x peca -> tuplo
    """
    Recebe um tabuleiro e uma peca
    e devolve um tuplo com todas
    as posicoes que estejam ocupadas
    por pecas iguais a introduzida
    """
    res = ()
    for i in ('1', '2', '3'):
        for e in ('a', 'b', 'c'):
            if pecas_iguais(obter_peca(tab, cria_posicao(e, i)), peca):
                res += (cria_posicao(e, i),)
    return res    

def obter_posicoes_livres(tab):
    # tabuleiro x peca -> tuplo
    """
    Recebe um tabuleiro e uma peca
    e devolve um tuplo com todas
    as posicoes livres
    """    
    return obter_posicoes(tab, cria_peca(' '))

def obter_posicoes_jogador(tab, peca):
    # tabuleiro x peca -> tuplo
    """
    Recebe um tabuleiro e uma peca
    e devolve um tuplo com todas
    as posicoes que estejam ocupadas
    por pecas iguais a introduzida
    """    
    return obter_posicoes(tab, peca)  

def fase_do_jogo(tab):
    # tabuleiro -> string
    """
    Recebe um tabuleiro e devolve
    atraves do numero de pecas
    uma cadeia de caracteres que representa
    a fase do jogo em que se encontra
    o tabuleiro ( fase de colocacao
    ou fase de movimentacao)
    """
    if len(obter_posicoes_jogador(tab, cria_peca('O'))) == 3:
        return 'mov'
    return 'col'

def impossivel_jogar(tab, peca):
    # tabuleiro x peca -> booleano
    """
    Recebe um tabuleiro na fase de
    movimentacao e uma peca
    representante de um jogador
    e devolve True caso nao seja possivel
    esse jogador mover uma peca
    e False caso contrario
    """
    adj = ()
    for pos1 in obter_posicoes_jogador(tab, peca):     
        adj += obter_posicoes_adjacentes(pos1)
    for pos2 in adj:
        if eh_posicao_livre(tab, pos2):
            return False
    return True
            

def obter_movimento_manual(tab, peca):
    #tabuleiro x peca -> tuplo
    """
    Recebe um tabuleiro e uma peca
    representante de um jogador e devolve
    um tuplo de uma posicao ,caso esteja
    na fase de colocacao, escolhido pelo
    utilizador atraves de um input, ou
    um tuplo de posicoes, caso esteja na fase
    de movimentacao, tambem escolhido pelo utilizador
    atraves de um input.
    Caso o valor atribuido no imput esteja
    errado a funcao gera um ValueError
    """
    if fase_do_jogo(tab) == 'col':
        pos = input('Turno do jogador. Escolha uma posicao: ')
        for i in ('1', '2', '3'):
            for e in ('a', 'b', 'c'):
                if posicao_para_str(cria_posicao(e,i)) == pos and\
                pecas_iguais(obter_peca(tab,cria_posicao(e,i)),cria_peca(' ')):
                    return (cria_posicao(e,i),)
        raise ValueError('obter_movimento_manual: escolha invalida')
    if fase_do_jogo(tab) == 'mov':
        pos = input('Turno do jogador. Escolha um movimento: ')
        if len(pos) == 4 and pos[:2] in\
           ('a1','a2','a3','b1','b2','b3','c1','c2','c3') and pos[2:] in\
           ('a1','a2','a3','b1','b2','b3','c1','c2','c3'):
            inicial =  cria_posicao(pos[0], pos[1])
            final =  cria_posicao(pos[2], pos[3])
            if pecas_iguais(obter_peca(tab,inicial), peca):
            # caso seja impossivel jogar e o jogador tenha escrito
            # duas vezes a mesma posicao e passado o turno
                if impossivel_jogar(tab, peca):
                    if posicoes_iguais(final, inicial):
                        return (inicial, inicial)
                if pecas_iguais(obter_peca(tab,final),cria_peca(' ')):
                    for pos in obter_posicoes_adjacentes(inicial):
                        if posicoes_iguais(pos,final):                   
                            return (inicial,final)    
        raise ValueError('obter_movimento_manual: escolha invalida')
    
def soma_vetor(vetor):
    # vetor -> inteiro
    """
    Recebe um vetor e devolve a soma
    dos valores das pecas
    """
    return peca_para_inteiro(vetor[0]) + peca_para_inteiro(vetor[1])\
        + peca_para_inteiro(vetor[2])
    
def vitoria_bloqueio(tab, peca):
    # tabuleiro x peca -> posicao
    """
    Recebe um tabuleiro e uma peca
    que representa um jogador
    e devolve uma posicao que 
    permite ao jogador ganhar ou
    bloquear o adversario
    """
    for i in ('a', 'b', 'c'):
        if soma_vetor(obter_vetor(tab, i)) == 2 * peca_para_inteiro(peca):
            for e in ('1', '2', '3'):
                if eh_posicao_livre(tab,cria_posicao(i,e)):
                    return cria_posicao(i,e)
    for m in ('1', '2', '3'):
        if soma_vetor(obter_vetor(tab, m)) == 2 * peca_para_inteiro(peca):
            for n in ('a', 'b', 'c'):
                if eh_posicao_livre(tab,cria_posicao(n,m)):
                    return cria_posicao(n,m)   
                
def centro(tab):
    # tabuleiro -> posicao
    """
    Recebe um tabuleiro e devolve
    a posicao central caso esta esteja vazia
    """
    if eh_posicao_livre(tab,cria_posicao('b','2')):
        return cria_posicao('b','2')
    
def canto_vazio(tab):
    # tabuleiro -> posicao
    """
    Recebe um tabuleiro e devolve
    a posicao do primeiro canto que
    se encontra vazio
    """    
    for i in ('a1','c1','a3','c3'):
        if eh_posicao_livre(tab,cria_posicao(i[0],i[1])):
            return cria_posicao(i[0], i[1])
        
def lateral_vazio(tab):
    # tabuleiro -> posicao
    """
    Recebe um tabuleiro e devolve
    a posicao do primeiro lado que
    se encontra vazio
    """    
    for i in ('b1','a2','c2','b3'):
        if eh_posicao_livre(tab,cria_posicao(i[0],i[1])):
            return cria_posicao(i[0], i[1])
        
def posicionar_auto(tab, peca):
    # tabuleiro x peca -> posicao
    """
    Recebe um tabuleiro e uma peca
    e a partir das funcoes anteriores
    devolve uma posicao
    """
    if pecas_iguais(peca, cria_peca('X')):
        adv = cria_peca('O')
    else:
        adv = cria_peca('X')
    if vitoria_bloqueio(tab, peca):
        return vitoria_bloqueio(tab, peca)
    if vitoria_bloqueio(tab, adv):
        return vitoria_bloqueio(tab, adv)  
    if centro(tab):
        return centro(tab)
    if canto_vazio(tab):
        return canto_vazio(tab)
    if lateral_vazio(tab):
        return lateral_vazio(tab)
    
def facil(tab, peca):
    # tabuleiro x peca -> tuplo
    """
    Recebe um tabuleiro e uma peca
    representantede um jogador
    e devolve um tuplo com um
    ou mais elementos dependendo da fase
    do jogo correspondentes a dificuldade
    de jogo, facil
    """
    if fase_do_jogo(tab) == 'col':
        return (posicionar_auto(tab, peca),)
    if fase_do_jogo(tab) == 'mov':
        for i in ('1', '2', '3'):
            for e in ('a', 'b', 'c'):
                if pecas_iguais(obter_peca(tab, cria_posicao(e,i)), peca):
                    for m in obter_posicoes_adjacentes(cria_posicao(e,i)):
                        if eh_posicao_livre(tab, m):
                            return (cria_posicao(e,i), m)
                
              
def minimax(tab, peca, prof, seq):
    # tabuleiro x peca x inteiro x tuplo -> tuplo
    """
    Recebe um tabuleiro, uma peca
    representante do jogador, um inteiro 
    representante da profundidade de recursao
    e um tuplo vazio que representa a 
    sequencia de jogadas e devolve um tuplo 
    com um inteiro representante do vencedor 
    e a melhor sequencia de movimentos 
    de acordo com a profundidade
    """
    if pecas_iguais(peca,cria_peca('X')):
        adv = cria_peca('O')
    else:
        adv = cria_peca('X')
    melhor_seq_movimentos = ()
    if not pecas_iguais(obter_ganhador(tab), cria_peca(' ')) or prof == 0:
        return peca_para_inteiro(obter_ganhador(tab)), seq
    else:
        melhor_resultado = - peca_para_inteiro(peca)
        for e in obter_posicoes_jogador(tab, peca):
            for i in obter_posicoes_adjacentes(e):
                if  eh_posicao_livre(tab,i):
                    newmov = (e,i)
                    novo_tabuleiro = move_peca(cria_copia_tabuleiro(tab),e,i)
                    novo_resultado, nova_seq_movimentos = \
                        minimax(novo_tabuleiro, adv, prof-1,seq + newmov)
                    if not melhor_seq_movimentos or\
                    (pecas_iguais(peca,cria_peca('X')) and\
                     novo_resultado > melhor_resultado) or\
                    (pecas_iguais(peca,cria_peca('O')) and\
                     novo_resultado < melhor_resultado):
                        melhor_resultado, melhor_seq_movimentos =\
                            novo_resultado, nova_seq_movimentos
        return melhor_resultado, melhor_seq_movimentos
    
def normal_dificil(tab,peca,dificuldade):
    # tabuleiro x peca x string -> tuplo
    """
    Recebe um tabuleiro uma peca
    e uma cadeia de caracteres que representa
    uma dificuldade e devolve um tuplo
    com um ou mais argumentos de acordo com
    a dificuldade escolhida
    """
    if dificuldade == 'normal':
        prof = 1
    elif dificuldade == 'dificil':
        prof = 5  
    if fase_do_jogo(tab) == 'col':
        return (posicionar_auto(tab, peca),)
    if fase_do_jogo(tab) == 'mov':
        if impossivel_jogar(tab, peca):
            pos = obter_posicoes_jogador(tab, peca)[0]
            return (pos,pos)
        tuplo_pos = minimax(tab, peca, prof, ())
        return (tuplo_pos[1][0],tuplo_pos[1][1])     
    
def obter_movimento_auto(tab, peca, dificuldade):
    # tabuleiro x peca x string -> tuplo
    """
    Recebe um tuplo e uma peca e
    devolve uma cadeiade caracteres
    que representa a dificuldade e 
    devolve um tuplo com um ou mais
    argumentos de acordo com a dificuldade
    """
    if dificuldade == 'facil':
        return facil(tab, peca)
    if dificuldade == 'normal':
        return normal_dificil(tab, peca, dificuldade)
    if dificuldade == 'dificil':
        return normal_dificil(tab, peca, dificuldade)
    
def joga_jogador(tab, peca):
    # tabuleiro x peca ->  {}
    """
    Recebe um tabuleiro e uma peca
    e altera esse tabuleiro atraves de
    outras funcoes. Apos isso da print
    da representacao agradavel para os olhos
    do tabuleiro alterado
    """
    if fase_do_jogo(tab) == 'col':
        tab = coloca_peca(tab,peca,obter_movimento_manual(tab, peca)[0])
        print(tabuleiro_para_str(tab))
    else:
        tuplo_pos = obter_movimento_manual(tab, peca)
        tab = move_peca(tab,tuplo_pos[0],tuplo_pos[1] )
        print(tabuleiro_para_str(tab))
        
def joga_auto(tab,peca,dif):
    # tabuleiro x peca x string ->  {}
    """
    Recebe um tabuleiro, uma peca
    e uma cadeia de caracteres 
    representanteda dificuldade
    e altera esse tabuleiro atraves de
    outras funcoes. Apos isso da print
    da representacao agradavel para os olhos
    do tabuleiro alterado
    """
    if fase_do_jogo(tab) == 'col':
        tab = coloca_peca(tab,peca,obter_movimento_auto(tab, peca,dif)[0])
        print(tabuleiro_para_str(tab))
    else:
        tuplo_pos = obter_movimento_auto(tab, peca, dif)
        tab = move_peca(tab, tuplo_pos[0], tuplo_pos[1])
        print(tabuleiro_para_str(tab))    
    
def moinho(jog, dif):
    # string x string -> string
    """
    Recebe duas strings que 
    representam o jogador e a
    dificuldade, respetivamente.
    E a funcao principal e permite ao
    utilizador jogar o jogo do moinho.
    Devolve a representacao externa da
    peca do jogador vencedor
    """
    if jog not in ('[X]', '[O]') or dif not in ('facil', 'normal', 'dificil'):
        raise ValueError ('moinho: argumentos invalidos')
    print('Bem-vindo ao JOGO DO MOINHO. Nivel de dificuldade '+dif+'.')
    if jog == '[O]':
        peca = cria_peca('O')
        adv = cria_peca('X')
    else:
        peca = cria_peca('X')
        adv = cria_peca('O')
    tab = cria_tabuleiro()
    print(tabuleiro_para_str(tab))
    while 1 != 0:
        if pecas_iguais(peca, cria_peca('X')):
            joga_jogador(tab, peca)
            if not pecas_iguais(obter_ganhador(tab), cria_peca(' ')):
                return peca_para_str(obter_ganhador(tab))
            print('Turno do computador ('+dif+'):')
            joga_auto(tab,adv,dif)
            if not pecas_iguais(obter_ganhador(tab), cria_peca(' ')):
                return peca_para_str(obter_ganhador(tab))         
        if pecas_iguais(peca, cria_peca('O')):
            print('Turno do computador ('+dif+'):')
            joga_auto(tab,adv,dif)
            if not pecas_iguais(obter_ganhador(tab), cria_peca(' ')):
                return peca_para_str(obter_ganhador(tab))
            joga_jogador(tab, peca)          
            if not pecas_iguais(obter_ganhador(tab), cria_peca(' ')):
                return peca_para_str(obter_ganhador(tab))