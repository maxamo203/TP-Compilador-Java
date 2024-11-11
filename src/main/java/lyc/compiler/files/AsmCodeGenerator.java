package lyc.compiler.files;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.constants.Constants;
import java.util.Map;
import java.util.HashMap;
import lyc.compiler.files.SymbolTableGenerator;
import lyc.compiler.files.Symbol;

public class AsmCodeGenerator implements FileGenerator {
    private static int labelNum = 0;
    private static Stack<String> labelStack = new Stack<String>();
    private static boolean isOr = false;
    private static boolean isNot = false;
    private static final Map<String, String> comparatorMap = new HashMap<String, String>() {{
        put("JE", "JNE");
        put("JNE", "JE");
        put("JNA", "JA");
        put("JAE", "JB");
        put("JA", "JNA");
        put("JB", "JAE");
    }};
    private static int contCond = 2;
    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write(generarEncabezado());
        fileWriter.write(grabarTablaSimbolos());
        fileWriter.write(iniciarSegmentoDatos());
        try{

            fileWriter.write(generateAsm(Punteros.p_root));
        }catch(Exception e){
            System.out.println("Error al generar el codigo ASM: " + e);
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
        out += "MOV AH, 4CH\n";
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
        String name = nodo.getPayload();
        String asm = "";
        String out = "";

        if(name.equals("WHILE")){
            out = "label" + labelNum + "start: \n";
            labelStack.push("label" + labelNum + "start: \n");
            labelNum++;
        }
        if(name == "NOT"){
            isNot = true;
        }
        if(name == "OR"){
            isOr = true;
            contCond = 2;
        }
        out += generateAsm(nodo.getLeft());
        if(name == "ELSE"){
            String label = labelStack.pop();
            out += "JMP " + "label" +labelNum + "finif\n";
            out += label + ": \n";
            labelStack.push("label" +labelNum + "finif");
        }
        if(name == "AND"){
            labelNum--;
            labelStack.pop();
        }
        
        out += generateAsm(nodo.getRight());
        switch (name) {
            case "=":
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FSTP " + nodo.getLeft().getPayload() + "\n";
                break;
            case "+":
                asm += operacion(nodo,"FADD");
                break;
            case "*":
                asm += operacion(nodo,"FMUL");
                break;
            case "-":
                asm += operacion(nodo,"FSUB");
                break;
            case "/":                
                asm += operacion(nodo,"FDIV");
                break;
            case "IF":
                String label = labelStack.pop();
                asm += label+":\n";
                break;
            case "ELSE":
                break;
            case "WHILE":
                String label3 = labelStack.pop();
                String label2 = labelStack.pop();
                asm += "JMP " + label2 + "\n";
                asm += label3 + ":\n";
                break;
            case "dummy":
                break;
            case "WRITE":
                String tipo = new SymbolTableGenerator().getSymbol(nodo.getLeft().getPayload()).getTipoDato();
                if(tipo.equals(Symbol.INTEGER) || tipo.equals(Symbol.FLOAT)){
                    asm += "DisplayFloat " + nodo.getLeft().getPayload() + "\n";
                }
                else if(tipo.equals(Symbol.STRING)){
                    asm += "DisplayString " + nodo.getLeft().getPayload() + "\n";
                }
                break;
            case "READ":
                String tipo2 = new SymbolTableGenerator().getSymbol(nodo.getLeft().getPayload()).getTipoDato();
                if(tipo2.equals(Symbol.INTEGER) || tipo2.equals(Symbol.FLOAT)){
                    asm += "GetFloat " + nodo.getLeft().getPayload() + "\n";
                }
                else if(tipo2.equals(Symbol.STRING)){
                    asm += "GetString " + nodo.getLeft().getPayload() + "\n";
                }
                break;
            case "==":
                asm += comparator(nodo,"JNE");
                break;
            case "!=":
                asm += comparator(nodo,"JE");
                break;
            case "<":
                asm += comparator(nodo,"JAE");
                break;
            case ">":
                asm += comparator(nodo,"JNA");
                break;
            case ">=":
                asm += comparator(nodo,"JB");
                break;
            case "<=":
                asm += comparator(nodo,"JA");
                break;
            case "AND":
                break;
            case "OR":
                isOr = false;
                break;
            case "NOT":
                isNot = false;
                break;
            default:
                break;
        }
        return out + asm;
    }

    private static String comparator(Nodo nodo,String commandAsm){
        String out = "";
        if(nodo.getLeft().isLeaf()){
            out += "FLD " + nodo.getLeft().getPayload() + "\n";
        }
        if(nodo.getRight().isLeaf()){
            out += "FLD " + nodo.getRight().getPayload() + "\n";
        }
        if(!(commandAsm == "JNE" || commandAsm == "JE")){
            out += "fxch \n";
        }
        out += "FCOMP \n";
        out += "FSTSW AX \n";
        out += "SAHF \n";
        if(isNot){
            commandAsm = comparatorMap.get(commandAsm);
            out += commandAsm + " label" + labelNum + "end \n";
            labelStack.push("label"+labelNum+"end");
            labelNum++;
        }
        else if(isOr){
            contCond--;
            if(contCond == 0){
                out += commandAsm + " label" + labelNum + "end \n";
                out += "label" + labelNum + "start: \n"; //agrega la etiqueta de salto para la primer condicion del or
                labelStack.push("label"+labelNum+"end");
                labelNum++;
            }
            else{
                commandAsm = comparatorMap.get(commandAsm);
                out += commandAsm + " label" + labelNum + "start \n";
            }
        }else{ //AND o comparacion normal
            out += commandAsm + " label" + labelNum + "end \n";
            labelStack.push("label"+labelNum+"end");
            labelNum++;
        }

        
        return out;
    }
    private static String operacion(Nodo nodo, String operacionParam){
        String out = "";
        boolean isLeftLeaf = nodo.getLeft().isLeaf();
        if(nodo.getLeft().isLeaf()){
            out += "FLD " + nodo.getLeft().getPayload() + "\n";
        }
        if(nodo.getRight().isLeaf()){
            out += operacionParam +  " " + nodo.getRight().getPayload() + "\n";
        }
        else{
            out += isLeftLeaf ? "fxch \n": ""; //para dar vuelta en caso de resta o division (lo hace innesesariamente en suma y multiplicacion)
            out += operacionParam + "\n";
            out += "FFREE 0\n";
        }
        return out;
    }
}
