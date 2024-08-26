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
Assig = "="
OpenBracket = "("
CloseBracket = ")"
Letter = [a-zA-Z]
Digit = [0-9]
StartComment = "*-"
EndComment = "-*"
Quote = "\""
StartIf = "¿"
EndIf = "?"
Else = "sino"
StartWhile = "¿¿"
EndWhile = "??"
TypeFloat = "decimal"
TypeInt = "entero"
TypeString = "texto"
Init = "init"
And = "y"
Or = "o"
Not = "no es"

WhiteSpace = {LineTerminator} | {Identation}
Identifier = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Digit}+
StringConstant = {Quote}({Letter}|{Digit})*{Quote}
Comment = {StartComment}(.)*{EndComment}
%%


/* keywords */

<YYINITIAL> {
  {And}                                   {return symbol(ParserSym.AND);}
  {Or}                                    {return symbol(ParserSym.OR);}
  {Not}                                   {return symbol(ParserSym.NOT);}

  {Init}                                  {return symbol(ParserSym.INIT);}

  {TypeFloat}                             {return symbol(ParserSym.TYPE_FLOAT);}
  {TypeInt}                               {return symbol(ParserSym.TYPE_INTEGER);}
  {TypeString}                            {return symbol(ParserSym.TYPE_STRING);}

  {StartIf}                               {return symbol(ParserSym.START_IF);}
  {EndIf}                                 {return symbol(ParserSym.END_IF);}
  {Else}                                  {return symbol(ParserSym.ELSE);}

  {StartWhile}                            {return symbol(ParserSym.START_WHILE);}
  {EndWhile}                              {return symbol(ParserSym.END_WHILE);}
  /* identifiers */
  {Identifier}                              { if(yylength() > MAX_LENGTH){ throw new InvalidLengthException(yytext()); }
                                            else{return symbol(ParserSym.IDENTIFIER, yytext());}}
  /* Constants */
  {IntegerConstant}                        { return symbol(ParserSym.INTEGER_CONSTANT, yytext()); }
  {StringConstant}                         {if(yylength() > MAX_STRING+2){throw new InvalidLengthException(yytext());}
                                            else{return symbol(ParserSym.STRING_CONSTANT, yytext());}}
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


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
