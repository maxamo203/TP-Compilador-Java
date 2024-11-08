package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.constants.Constants;
public class AsmCodeGenerator implements FileGenerator {

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write(generarEncabezado());
        fileWriter.write(grabarTablaSimbolos());
        fileWriter.write(iniciarSegmentoDatos());
        try{

            fileWriter.write(generateAsm(Punteros.p_root));
        }catch(Exception e){
            System.out.println("Error al generar el codigo ASM");
        }
        fileWriter.write(generarFooter());
    }
    private static String generarEncabezado() {
        String out = "";
        out += "include macros2.asm\n";
        out += "include number.asm\n";
        out += ".model LARGE\n";
        out += ".386\n";
        out += ".stack 200h\n";
        out += "MAXTEXTSIZE equ " + Constants.MAX_STRING +"\n";
        
        return out;
    }
    private static String generarFooter(){
        String out = "";
        out += "MOV AH, 4C00H\n";
        out += "INT 21H\n";
        out += "END START\n";
        return out;
    }
    private static String iniciarSegmentoDatos(){
        String out = "";
        out += ".CODE\n";
        out += "START:\n";
        out += "MOV AX, @DATA\n";
        out += "MOV DS, AX\n";
        return out;
    }
    private static String grabarTablaSimbolos(){
        String out = "";
        out += ".DATA\n";
        try{
            for(String key : SymbolTableGenerator.getSymbolTable().keySet()) {
                Symbol symbol = SymbolTableGenerator.getSymbol(key);
                if(symbol.getTipoDato().equals(Symbol.INTEGER) || symbol.getTipoDato().equals(Symbol.FLOAT)){
                    out += key + " DD ";
                    if(symbol.getValor().equals("-"))
                        out += " ?\n";
                    else
                        out += symbol.getValor() + "\n";
                }
                else if(symbol.getTipoDato().equals(Symbol.STRING)){
                    out += key + " DB ";
                    if(symbol.getValor().equals("-"))
                        out += "MAXTEXTSIZE" +" dup (?)\n";
                    else
                        out += symbol.getValor() + ", '$'\n";
                }
            }

        }catch(Exception e){
            System.out.println("Error al leer la tabla de simbolos");
        }
        return out;
    }
    private static String generateAsm(Nodo nodo) throws Exception {
        if (nodo == null) {
            return "";
        }
        String out = generateAsm(nodo.getLeft()) + generateAsm(nodo.getRight());
        String name = nodo.getPayload();
        String asm = "";
        switch (name) {
            case "=":
                asm = "FSTP " + nodo.getLeft().getPayload() + "\n";
                break;
            case "+":
                asm = "FADD\n";
            case "*":
                asm =  "FMUL\n";
            case "-":
                asm =  "FSUB\n";
            case "/":
                asm =  "FDIV\n";
            case "IF":
                break;
            case "ELSE":
                break;
            case "WHILE":
                break;
            case "dummy":
                break;
            case "WRITE":
                break;
            case "READ":
                break;
            case "==":
                break;
            case "<":
                break;
            case ">":
                break;
            case ">=":
                break;
            case "<=":
                break;
            case "!=":
                break;
            case "AND":
                break;
            case "OR":
                break;
            case "NOT":
                break;
            default:
                if(SymbolTableGenerator.containsSymbol(name)){
                    Symbol symbol = SymbolTableGenerator.getSymbol(name);
                    if(name.startsWith("_") && !symbol.getTipoDato().equals(Symbol.STRING)){
                        asm = "FLD " + name + "\n";
                    }
                    else{
                        asm = "FLD " + name + "\n";
                    }
                }
                else{
                    System.out.println("Error: Variable no declarada " + name);
                    asm = "Error: Variable no declarada " + name + "\n";
                }
                break;
        }
        return out + asm;
    }
}
