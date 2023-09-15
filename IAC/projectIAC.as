orig            0000h
                MVI     R6, 8000h

terreno         EQU     4000h

seed            WORD    155
pos_dino        WORD    altmin_dino
estado_dino     WORD    0

n               EQU     80
altmax          EQU     8
altmax_dino     EQU     0000101100000101b ; 21 - 10(altura maxima) = 11
altmin_dino     EQU     0001010100000101b

dinossauro      EQU     'R'
cato            EQU     'H'
chao            EQU     ' '

disp7_d0        EQU     FFF0h
disp7_d1        EQU     FFF1h
disp7_d2        EQU     FFF2h
disp7_d3        EQU     FFF3h
                
term_color      EQU     FFFBh
term_cursor     EQU     FFFCh
term_write      EQU     FFFEh
term_cls        EQU     FFFFh

color_cato      EQU     0000000000011100b
color_dino      EQU     0000000011100000b
color_chao      EQU     1111100100000000b
color_gameover  EQU     0000000011000010b

timer_state     EQU     FFF7h
timer_count     EQU     FFF6h
timer_count_val EQU     1
timer_ticks     WORD    0

int_mask        EQU     FFFAh
int_mask_val    EQU     1000000000001001b

score           WORD    0
started         WORD    0
game_over       STR     'GAME  OVER'

                MVI     R1, int_mask           ; Inicializar mascara de interrupcoes
                MVI     R2, int_mask_val
                STOR    M[R1], R2
                ENI
wait_for_start: MVI     R1, started            ; Espera que o jogador carregue
                LOAD    R1, M[R1]              ; no botao 0
                CMP     R1, R0 
                BR.Z    wait_for_start

; Funcao principal do programa, inicia o temporizador
main:           MVI     R2, timer_count_val
                MVI     R1, timer_count
                STOR    M[R1], R2
                MVI     R1, timer_ticks
                STOR    M[R1], R0
                MVI     R2, 1
                MVI     R1, timer_state
                STOR    M[R1], R2
                
; Espera por interrupcao do timer
timer_listener: MVI     R1, timer_ticks
                LOAD    R2, M[R1]
                CMP     R2, R0
                JAL.NZ  process_timer_event
                
                BR      timer_listener

; Funcao que processa eventos de temporizador:
;  atualiza o terreno
;  limpa o ecra
;  processa o salto do dinossauro
;  escreve para o ecra
;  desenha o score no mostrador de sete digitos
process_timer_event:
                DEC     R6                ; Guardar contexto
                STOR    M[R6], R7
                
                DSI
                MVI     R1, timer_ticks        ; Decrementa aos timer_ticks
                LOAD    R2, M[R1]
                DEC     R2
                STOR    M[R1], R2
                ENI
                
                MVI     R1, terreno      ; R1 = Posicao no terreno
                MVI     R2, n            ; R2 = No. colunas
                JAL     atualizajogo
                JAL     limparecra
                JAL     salto
                JAL     output
                JAL     draw_score
                
                LOAD    R7, M[R6]        ; Restaurar contexto
                INC     R6
                JMP     R7
                
salto:          MVI     R1, estado_dino
                LOAD    R1, M[R1]
                MVI     R2, -1           ; Se o estado for -1, 
                CMP     R1, R2           ; salta para .descida
                BR.Z    .descida
                MVI     R2, 1            ; Se o estado for 1,
                CMP     R1, R2           ; salta para .subida
                BR.Z    .subida
.return:        JMP     R7
.descida:       MVI     R1, pos_dino           ; Decresce 1 a altura do dinossauro
                LOAD    R2, M[R1]
                MVI     R3, 0000000100000000b
                ADD     R2, R2, R3
                STOR    M[R1], R2
                MVI     R3, altmin_dino
                CMP     R2, R3
                BR.NZ   .return
                MVI     R1, estado_dino
                STOR    M[R1], R0
                BR      .return
.subida:        MVI     R1, pos_dino           ; Acresce 1 a altura do dinsosauro
                LOAD    R2, M[R1]
                MVI     R3, 0000000100000000b
                SUB     R2, R2, R3
                STOR    M[R1], R2
                MVI     R1, altmax_dino
                CMP     R2, R1
                BR.NZ   .return
                MVI     R1, estado_dino
                MVI     R2, -1
                STOR    M[R1], R2
                BR      .return         

; Funcao auxiliar que desenha no terminal o estado do jogo
output:         ; Guardar contexto
                DEC     R6
                STOR    M[R6], R4
                DEC     R6
                STOR    M[R6], R7
                
                ; Desenhar dinossauro
                MVI     R1, pos_dino
                LOAD    R1, M[R1]
                
                MVI     R2, term_cursor
                STOR    M[R2], R1
                MVI     R2, term_color
                MVI     R1, color_dino
                STOR    M[R2], R1        ; Cor do dinossauro
                MVI     R1, term_write
                MVI     R2, dinossauro
                STOR    M[R1], R2        ; Desenhar dinossauro
                
                ; Desenhar chao
                MVI     R2, term_color
                MVI     R1, color_chao
                STOR    M[R2], R1        ; Cor do chao
                MVI     R3, 80
                MVI     R1, 0
.loop1:         MVIH    R1, 22
                MVI     R2, term_cursor
                STOR    M[R2], R1
                MVI     R4, term_write
                MVI     R2, chao
                STOR    M[R4], R2        ; Desenhar chao
                INC     R1
                DEC     R3
                CMP     R3, R0
                BR.P    .loop1           ; Iterar sobre todas as colunas
                
                ; Desenhar catos
                MVI     R2, terreno
                MVI     R1, 0
                MVI     R4, 80
.loop2:         MVIH    R1, 22
                LOAD    R3, M[R2]
                
                DEC     R6                ; Guardar contexto
                STOR    M[R6], R2
                DEC     R6
                STOR    M[R6], R4
                
                CMP     R3, R0
                JAL.NZ  desenharcato
                
                LOAD    R4, M[R6]         ; Restaurar contexto
                INC     R6
                LOAD    R2, M[R6]
                INC     R6
                
                INC     R1                ; Proxima coluna
                INC     R2
                DEC     R4
                CMP     R4, R0
                BR.NZ   .loop2            ; Iterar ate a ultima coluna
                
                LOAD    R7, M[R6]         ; Restaurar contexto
                INC     R6
                LOAD    R4, M[R6]
                INC     R6
                JMP     R7

; Funcao que desenha um cato na posicao R1
desenharcato:   MVI     R2, 0000000100000000b
                SUB     R1, R1, R2
                
                DEC     R6                ; Guardar contexto
                STOR    M[R6], R1
                DEC     R6
                STOR    M[R6], R3
                DEC     R6
                STOR    M[R6], R7
                
                ; Verificar se esta seccao do cato esta a ser desenhada
                ; na mesma posicao que o dinossauro
                MVI     R2, pos_dino
                LOAD    R2, M[R2]
                CMP     R1, R2
                JAL.Z   gameover
                
                LOAD    R7, M[R6]         ; Restaurar contexto
                INC     R6
                LOAD    R3, M[R6]
                INC     R6
                LOAD    R1, M[R6]
                INC     R6
                
                MVI     R2, term_cursor
                STOR    M[R2], R1         ; Posicionar cursor na posicao do cato
                MVI     R2, term_color    
                MVI     R4, color_cato
                STOR    M[R2], R4         ; Escolher cor do cato
                MVI     R4, term_write
                MVI     R2, cato
                STOR    M[R4], R2         ; Desenhar seccao do cato
                DEC     R3
                CMP     R3, R0
                BR.NZ   desenharcato      ; Iterar ate desenhar todo o cato
                JMP     R7

; Funcao auxiliar que apenas limpa todo o terminal
limparecra:     DSI
                MVI     R1, term_cursor
                MVI     R2, term_cls
                STOR    M[R1], R2
                ENI
                JMP     R7

; Funcao final do programa, desenha 'game  over' no terminal 
gameover:       MVI     R2, term_color
                MVI     R1, color_gameover
                STOR    M[R2], R1        ; Escolher cor do texto
                MVI     R1, started
                STOR    M[R1], R0        ; Jogo terminou
                MVIL    R1, 35
                MVIH    R1, 10
                MVI     R3, game_over
.loop:          MVI     R2, term_cursor  ; Posicionar curosr
                STOR    M[R2], R1
                LOAD    R4, M[R3]
                MVI     R2, term_write
                STOR    M[R2], R4        ; Escrever letra
                INC     R1
                INC     R3
                CMP     R4, R0
                BR.NZ   .loop            ; Iterar ate acabar de escrever 'game  over'
                JAL     limpar_terreno   ; Limpa o terreno visivel no terminal
                MVI     R2, 0
                MVI     R1, timer_state
                STOR    M[R1], R2        ; Parar temporizador
                MVI     R1, score
                STOR    M[R1], R0        ; Reset do score
                JMP     wait_for_start

; Funcao auxiliar que escreve o score em decimal no mostrador de 7 segmentos
draw_score:     DEC     R6                ; Guardar contexto
                STOR    M[R6], R7
                
                MVI     R1, score
                LOAD    R4, M[R1]
                INC     R4
                STOR    M[R1], R4         ; Adicionar 1 ao score
                
                MOV     R1, R4
                MVI     R2, 1000
                JAL     divide
                LOAD    R4, M[R6]
                INC     R6
                MVI     R1, disp7_d3
                STOR    M[R1], R3
                
                MOV     R1, R4
                MVI     R2, 100
                JAL     divide
                LOAD    R4, M[R6]
                INC     R6
                MVI     R1, disp7_d2
                STOR    M[R1], R3
                
                MOV     R1, R4
                MVI     R2, 10
                JAL     divide
                LOAD    R4, M[R6]
                INC     R6
                MVI     R1, disp7_d1
                STOR    M[R1], R3
                
                MOV     R1, R4
                MVI     R2, 10
                JAL     divide
                LOAD    R4, M[R6]
                INC     R6
                MVI     R1, disp7_d0
                STOR    M[R1], R4
                        
                LOAD    R7, M[R6]        ; Restaurar contexto
                INC     R6
                JMP     R7
                
; Funcao auxiliar que realiza a divisao inteira do primeiro parametro pelo segundo                
; R1 = dividendo, R2 = divisor
divide:         MVI     R3, 0
                CMP     R1, R2
                BR.N    .break
.loop:          INC     R3
                SUB     R1, R1, R2
                CMP     R1, R2
                BR.NN   .loop
.break:         DEC     R6
                STOR    M[R6], R1 ; R1 corresponde ao resto, passado pela pilha
                JMP     R7        ; R3 corresponde ao resultado da div inteira

; Funcao auxiliar que limpa o terreno visivel no ecra
limpar_terreno: MVI     R1, terreno
                MVI     R2, 80
.loop:          STOR    M[R1], R0
                INC     R1
                DEC     R2
                CMP     R2, R0
                BR.NZ   .loop        ; Iterar sobre todas as colunas do terreno
                JMP     R7

atualizajogo:   INC     R1               ; Desloca coluna
                LOAD    R4, M[R1]        ; em R1 + 1 para
                DEC     R1               ; a coluna na
                STOR    M[R1], R4        ; posicao R1
                
                INC     R1               ; Proxima coluna
                DEC     R2               ; Falta menos 1 coluna para terminar
                BR.P    atualizajogo     ; Iterar ate deslocarmos N colunas
                DEC     R1
                
                DEC     R6               ; Guardar contexto
                STOR    M[R6], R7
                DEC     R6
                STOR    M[R6], R1
                
                MVI     R1, altmax       ; Parametro de geracato(R1 = altura)
                JAL     geracato
                
                LOAD    R1, M[R6]        ; Restaurar contexto
                INC     R6
                LOAD    R7, M[R6]
                INC     R6
                
                STOR    M[R1], R3        ; Colocar altura do cacto no fim do vetor
                JMP     R7
                        
geracato:       MVI     R3, 1
                MVI     R4, seed
                LOAD    R4, M[R4]        ; R4 = x
                AND     R5, R4, R3       ; R5 = bit = x & 1
                SHR     R4               ; R4 = x >> 1
                MVI     R3, seed
                
                DEC     R6
                STOR    M[R6], R7
                DEC     R5               ; if bit:
                JAL.Z   ifbit
                MVI     R3, seed
                STOR    M[R3], R4
                LOAD    R7, M[R6]
                INC     R6
                
                MVI     R3, 29491
                CMP     R4, R3           ; x < 29491
                BR.N    ifx
                BR.O    ifx
                
                DEC     R1               ; R1 = altura - 1
                AND     R3, R4, R1       ; R3 = x & (altura - 1)
                INC     R3               ; R3 = (x & (altura - 1)) + 1
                JMP     R7               ; return R3
                
ifbit:          MVI     R5, B400h
                XOR     R4, R4, R5       ; R4 = XOR(x, 0xB400)
                JMP     R7
                
ifx:            MVI     R3, 0            ; return 0
                JMP     R7

; Funcao auxiliar a rotina de interrupcoes do temporizador
aux_clk:        DEC     R6                ; Guardar contexto
                STOR    M[R6], R1
                DEC     R6
                STOR    M[R6], R2

                DSI
                MVI     R2, timer_count
                MVI     R1, timer_count_val
                STOR    M[R2], R1
                MVI     R2, 1
                MVI     R1, timer_state   ; Reiniciar temporizador
                STOR    M[R1], R2
                MVI     R2, timer_ticks
                LOAD    R1, M[R2]
                INC     R1                ; Incrementar aos ticks
                STOR    M[R2], R1
                ENI
                
                LOAD    R2, M[R6]         ; Restaurar contexto
                INC     R6
                LOAD    R1, M[R6]
                INC     R6
                JMP     R7

; Funcao auxiliar a rotina de interrupcoes do botao up
up_aux:         DEC     R6                ; Guardar contexto
                STOR    M[R6], R1
                DEC     R6
                STOR    M[R6], R2
                
                MVI     R1, estado_dino
                LOAD    R2, M[R1]
                CMP     R2, R0
                BR.NZ   .aux        
                MVI     R2, 1
                STOR    M[R1], R2         ; Mudar estado do dinossauro para 1,
                                          ; se for incialmente 0
                
.aux:           LOAD    R2, M[R6]         ; Restaurar contexto
                INC     R6
                LOAD    R1, M[R6]
                INC     R6
                JMP     R7
                
                ORIG    7FF0h
int_timer:      DEC     R6                ; Rotina de interrupcoes do temporizador
                STOR    M[R6], R7
                JAL     aux_clk
                LOAD    R7, M[R6]
                INC     R6
                RTI

                ORIG    7F30h             ; Rotina de interrupcoes do botao up
int_up:         DEC     R6
                STOR    M[R6], R7
                JAL     up_aux
                LOAD    R7, M[R6]
                INC     R6
                RTI
                
                ORIG    7F00h             ; Rotina de interrupcoes do botao 0
int_0:          DEC     R6                ; Guardar contexto         
                STOR    M[R6], R1
                DEC     R6
                STOR    M[R6], R2
                MVI     R1, started
                MVI     R2, 1
                STOR    M[R1], R2
                LOAD    R2, M[R6]         ; Restaurar contexto
                INC     R6
                LOAD    R1, M[R6]
                INC     R6
                RTI

