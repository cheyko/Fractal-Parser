/* Specification for Fractal tokens */

// user customisations

package fractal.syntax;
import java_cup.runtime.*;
import fractal.sys.FractalException;
import fractal.sys.FractalLexerException;

// JFlex directives
    
%%

%cup
%public

%class FractalLexer
%throws FractalException

%type java_cup.runtime.Symbol

%eofval{
	return new Symbol(sym.EOF);
%eofval}

%eofclose false

%char
%column
%line

%{
    private Symbol mkSymbol(int id) {
        return new Symbol(id, yyline, yycolumn);
    }

    private Symbol mkSymbol(int id, Object val) {
        return new Symbol(id, yyline, yycolumn, val);
    }

    public int getChar() {
	return yychar + 1;
    }

    public int getColumn() {
    	return yycolumn + 1;
    }

    public int getLine() {
	return yyline + 1;
    }

    public String getText() {
	return yytext();
    }
%}

nl = [\n\r]

cc = ([\b\f]|{nl})

ws = {cc}|[\t ]

digit = [0-9]

realDigit = ([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+)|([0-9]+)

alpha = [_a-zA-Z?]

alphnum = {digit}|{alpha}

%%

<YYINITIAL>	{nl}	{
			 //skip newline
			}
<YYINITIAL>	{ws}	{
			 // skip whitespace
			}

<YYINITIAL>	"//".*	{
			 // skip line comments
			}

<YYINITIAL> {
    "+"			{return mkSymbol(sym.PLUS);}
    "-"			{return mkSymbol(sym.MINUS);}
    "*"			{return mkSymbol(sym.MUL);}
    "/"			{return mkSymbol(sym.DIV);}
    "%"			{return mkSymbol(sym.MOD);}

    ">"			{return mkSymbol(sym.ISGREATER);}
    "<"			{return mkSymbol(sym.ISLESS);}
    "="			{return mkSymbol(sym.ASSIGN);}
    ":"			{return mkSymbol(sym.COLON);}
    
    "or"		{return mkSymbol(sym.OR);}
    "and"		{return mkSymbol(sym.AND);}
    "not"		{return mkSymbol(sym.NOT);}
    ","			{return mkSymbol(sym.COMMA);}
        
    "fd"		{return mkSymbol(sym.FORWARD);}
    "bk"		{return mkSymbol(sym.BACK);}
    "left"		{return mkSymbol(sym.LEFT);}
    "right"		{return mkSymbol(sym.RIGHT);}
    
    "pu"		{return mkSymbol(sym.PENUP);}
    "pd"		{return mkSymbol(sym.PENDOWN);}
    "home"		{return mkSymbol(sym.HOME);}
    "clear"		{return mkSymbol(sym.CLEAR);}
        
    "("			{return mkSymbol(sym.LPAREN);}
    ")"			{return mkSymbol(sym.RPAREN);}
    "["			{return mkSymbol(sym.LBRACE);}
    "]"			{return mkSymbol(sym.RBRACE);}
    
    "self"			{return mkSymbol(sym.SELF);}
    "repeat"		{return mkSymbol(sym.REPEAT);}
    "render"		{return mkSymbol(sym.RENDER);}
    "save"			{return mkSymbol(sym.SAVE);}
    "restore"		{return mkSymbol(sym.RESTORE);}
    "end"			{return mkSymbol(sym.END);}
    "@"				{return mkSymbol(sym.COMPOSE);}
    "!"				{return mkSymbol(sym.SEQUENCE);}
    "def"			{return mkSymbol(sym.DEFINE);}
    "fractal"		{return mkSymbol(sym.FRACTAL);}

    {digit}+ 		{
			 // INTEGER
	       		 return mkSymbol(sym.INT, 
			 	         new Integer(yytext()));
	       		}
	{realDigit}+ 		{
			 // INTEGER
	       		 return mkSymbol(sym.REAL, 
			 	         new Double(yytext()));
	       		}

    {alpha}{alphnum}*   {
    		      	 // IDENTIFIERS
	       		 return mkSymbol(sym.ID, yytext());
	       		}

    .			{ // Unknown token (leave this in the last position)
    			  throw new FractalLexerException(yytext(), getLine(),
							  getColumn());
    			}
}
