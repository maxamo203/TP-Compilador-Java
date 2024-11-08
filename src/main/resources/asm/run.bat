PATH=C:\Program Files (x86)\GUI Turbo Assembler\BIN;

tasm numbers.asm
tasm final.asm
tlink final.obj numbers.obj
final.exe
del final.obj
del numbers.obj 
del final.exe
del final.map