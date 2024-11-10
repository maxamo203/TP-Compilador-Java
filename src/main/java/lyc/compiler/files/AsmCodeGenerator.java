package lyc.compiler.files;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.constants.Constants;
import java.util.Map;
import java.util.HashMap;
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
            System.out.println("Error al generar el codigo ASM" + e);
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
        /*
         * a <= b or c==d => entra
         * a == b and c<= d => salir
         */
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
        String name = nodo.getPayload();
        String asm = "";
        String out = "";

        if(name.equals("WHILE")){
            out = "label" + labelNum + ": \n";
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
        if(name == "AND"){
            labelNum--;
            labelStack.pop();
        }
        
        out += generateAsm(nodo.getRight());
        System.out.println(name);
        switch (name) {
            case "=":
                System.out.println(nodo.getRight().isLeaf());
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FSTP " + nodo.getLeft().getPayload() + "\n";
                break;
            case "+":
                asm += operacion(nodo,"FADD");
            case "*":
                asm += operacion(nodo,"FMUL");
            case "-":
                asm += operacion(nodo,"FSUB");
            case "/":                
                asm += operacion(nodo,"FDIV");
            case "IF":
                String label = labelStack.pop();
                asm += label+":\n";
                break;
            case "ELSE":
                asm += "label" + labelNum + "end:\n";
                break;
            case "WHILE":
                asm += "label" + labelNum + "end:\n";
                asm += "JA " + labelNum + "\n";
                break;
            case "dummy":
                break;
            case "WRITE":
                asm += "displayString " + nodo.getLeft().getPayload() + "\n";
                break;
            case "READ":
                asm += "getString " + nodo.getLeft().getPayload() + "\n";
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
        out += "FCOMP \n";
        out += "FSTSW AX \n";
        out += "SAHF \n";
        out += "fxch \n";
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
                out += "label" + labelNum + "start: \n";
                labelStack.push("label"+labelNum+"end");
                labelNum++;
            }
            else{
                commandAsm = comparatorMap.get(commandAsm);
                out += commandAsm + " label" + labelNum + "start \n";
            }
        }else{ //AND
            out += commandAsm + " label" + labelNum + "end \n";
            labelStack.push("label"+labelNum+"end");
            labelNum++;
        }

        
        return out;
    }
    private static String operacion(Nodo nodo, String operacion){
        String out = "";
        if(nodo.getLeft().isLeaf()){
            out += "FLD " + nodo.getLeft().getPayload() + "\n";
        }
        if(nodo.getRight().isLeaf()){
            out += operacion +  " " + nodo.getRight().getPayload() + "\n";
        }
        out += operacion + "\n";
        out += "FFREE 0\n";
        return out;
    }
}
