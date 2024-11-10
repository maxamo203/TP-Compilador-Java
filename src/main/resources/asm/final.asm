include macros2.asm
include number.asm
.model LARGE
.386
.stack 200h
MAXTEXTSIZE equ 40
.DATA
a DD  ?
b DD  ?
c DD  ?
d DD  ?
e DD  ?
_1 DD 1
_muejaja DB "muejaja", '$'
_2 DD 2
_3 DD 3
_222 DD 222
id DB MAXTEXTSIZE dup (?)
_666 DD 666
_555 DD 555
_444 DD 444
_333 DD 333
.CODE
START:
MOV AX, @DATA
MOV DS, AX
FLD _3
FSTP c
FLD _1
FSTP a
GetString id
FLD c
FLD _2
fxch 
FCOMP 
FSTSW AX 
SAHF 
JAE label0end 
FLD c
FLD _3
fxch 
FCOMP 
FSTSW AX 
SAHF 
JAE label0end 
FLD _3
FSTP b
FLD b
FLD _3
fxch 
FCOMP 
FSTSW AX 
SAHF 
JB label1end 
FLD _3
FSTP a
label1end:
label0end:
FLD c
FLD _222
FCOMP 
FSTSW AX 
SAHF 
JE label2start 
FLD c
FLD _444
fxch 
FCOMP 
FSTSW AX 
SAHF 
JNA label2end 
label2start: 
FLD _3
FSTP b
FLD c
FLD _333
FCOMP 
FSTSW AX 
SAHF 
JNE label3end 
FLD c
FLD _555
fxch 
FCOMP 
FSTSW AX 
SAHF 
JNA label3end 
FLD _3
FSTP b
label3end:
label2end:
label4start: 
FLD a
FLD _444
fxch 
FCOMP 
FSTSW AX 
SAHF 
JB label5start 
FLD b
FLD _666
FCOMP 
FSTSW AX 
SAHF 
JNE label5end 
label5start: 
label6start: 
FLD _2
FLD _3
FCOMP 
FSTSW AX 
SAHF 
JE label7end 
DisplayString _muejaja
;JMP label6start: 

label7end:
;JMP label4start: 

label5end:
DisplayString id
MOV AH, 4CH
INT 21H
END START
