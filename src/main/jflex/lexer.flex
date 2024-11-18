package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import static lyc.compiler.constants.Constants.*;

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}


%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
  
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]

Plus = "+"
Mult = "*"
Sub = "-"
Div = "/"
Assig = ":="
Colon = ":"
OpenBracket = "("
CloseBracket = ")"
Coma = ","
OpenCorchete = "["
CloseCorchete = "]"
OpenLlave = "{"
CloseLlave = "}"
Letter = [a-zA-Z]
Digit = [0-9]
StartComment = "*-"
EndComment = "-*"
Quote = "\""
StartIf = "¿"
EndIf = "?"
If = "si"
Else = "sino"
While = "mientras"
StartWhile = "¿¿"
EndWhile = "??"
TypeFloat = "Float"
TypeInt = "Int"
TypeString = "String"
Init = "init"
And = "AND"
Or = "OR"
Not = "NOT"
Mayor = ">"
Menor = "<"
MenorOigual = "<="
MayorOigual = ">="
Igual = "=="

Trinagulo = "triangulo"
GetPenultimatePosition = "getPenultimatePosition"
Leer = "leer"
Escribir = "escribir"

WhiteSpace = {LineTerminator} | {Identation}
Identifier = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Digit}+
StringConstant = {Quote}({Letter}|{Digit}|[ ])*{Quote}
FloatConstant = ({Digit}+\.{Digit}+)|({Digit}+?\.{Digit}+)|({Digit}+\.{Digit}+?)
Comment = {StartComment}(.)*?{EndComment}
%%

/* keywords */

<YYINITIAL> {
  {And}                                   {return symbol(ParserSym.AND);}
  {Or}                                    {return symbol(ParserSym.OR);}
  {Not}                                   {return symbol(ParserSym.NOT);}
  {Colon}                                   { return symbol(ParserSym.COLON); }

  {Init}                                  {return symbol(ParserSym.INIT);}
  {Coma}                                    { return symbol(ParserSym.COMMA); }
  {OpenCorchete}                            { return symbol(ParserSym.OPEN_CORCHETE); }
  {CloseCorchete}                           { return symbol(ParserSym.CLOSE_CORCHETE); }
  {OpenLlave}                               { return symbol(ParserSym.OPEN_LLAVE); }
  {CloseLlave}                              { return symbol(ParserSym.CLOSE_LLAVE);}
  {TypeFloat}                             {return symbol(ParserSym.TYPE_FLOAT);}
  {TypeInt}                               {return symbol(ParserSym.TYPE_INTEGER);}
  {TypeString}                            {return symbol(ParserSym.TYPE_STRING);}

  {If}                                    {return symbol(ParserSym.IF);}
  {StartIf}                               {return symbol(ParserSym.START_IF);}
  {EndIf}                                 {return symbol(ParserSym.END_IF);}
  {Else}                                  {return symbol(ParserSym.ELSE);}

  {While}                                   {return symbol(ParserSym.WHILE);}
  {StartWhile}                            {return symbol(ParserSym.START_WHILE);}
  {EndWhile}                              {return symbol(ParserSym.END_WHILE);}

  {GetPenultimatePosition}                  {return symbol(ParserSym.GETPENULTIMATEPOSITION);}
  {Trinagulo}                               {return symbol(ParserSym.TRIANGULO);}  
  {Leer}                                  {return symbol(ParserSym.LEER);}  
  {Escribir}                                  {return symbol(ParserSym.ESCRIBIR);}  
  /* identifiers */
  {Identifier}                              { if(yylength() > MAX_LENGTH){ throw new InvalidLengthIdException(yytext()); }
                                            else{return symbol(ParserSym.IDENTIFIER, yytext());}}
  /* Constants */
  {IntegerConstant}                        { { if(Integer.parseInt(yytext()) >= MAX_INT){ throw new InvalidIntegerException(yytext()); }
                                            else{return symbol(ParserSym.INTEGER_CONSTANT, yytext());}} }
  {StringConstant}                         {if(yylength() > MAX_STRING+2){throw new InvalidLengthException(yytext());}
                                            else{return symbol(ParserSym.STRING_CONSTANT, yytext());}}
  {FloatConstant}                           { 
    float value = Float.parseFloat(yytext());
        // Verificar si el valor está dentro del rango permitido para float de 32 bits
        if (value == Float.POSITIVE_INFINITY || value == Float.NEGATIVE_INFINITY) {
            throw new FloatOutOfRangeException(yytext());
        }
    return symbol(ParserSym.FLOAT_CONSTANT, yytext()); 
    }

  /* operators */
  {Plus}                                    { return symbol(ParserSym.PLUS); }
  {Sub}                                     { return symbol(ParserSym.SUB); }
  {Mult}                                    { return symbol(ParserSym.MULT); }
  {Div}                                     { return symbol(ParserSym.DIV); }
  {Assig}                                   { return symbol(ParserSym.ASSIG); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET); }
  /* whitespace */
  {WhiteSpace}                    {/* ignore */}
  {Comment}                       {/* ignore */}
}

/* comparers */
{Mayor}                                     { return symbol(ParserSym.GREATER); }
{Menor}                                     { return symbol(ParserSym.LESS); }
{MenorOigual}                               { return symbol(ParserSym.LESSOREQUAL); }
{MayorOigual}                               { return symbol(ParserSym.GREATEROREQUAL); }
{Igual}                                     { return symbol(ParserSym.EQUAL); }

/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
