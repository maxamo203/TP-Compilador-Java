package lyc.compiler.files;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.constants.Constants;
public class AsmCodeGenerator implements FileGenerator {
    private static int labelNum = 0;
    private static Stack<String> labelStack = new Stack<String>();

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
        String name = nodo.getPayload();
        String asm = "";
        String out = "";

        if(name == "WHILE"){
            out = "label" + labelNum + " \n";
            labelNum++;
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
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FADD " + nodo.getRight().getPayload() + "\n";
                }else{
                    asm += "FADD \n";
                    asm += "FFREE 0\n";
                }
            case "*":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FMUL " + nodo.getRight().getPayload() + "\n";
                }else{
                    asm += "FMUL \n";
                    asm += "FFREE 0\n";
                }
            case "-":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FSUB " + nodo.getRight().getPayload() + "\n";
                }else{
                    asm += "FSUB \n";
                    asm += "FFREE 0\n";
                }
            case "/":                
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FDIV " + nodo.getRight().getPayload() + "\n";
                }else{
                    asm += "FDIV \n";
                    asm += "FFREE 0\n";
                }
            case "IF":
                String label = labelStack.pop();
                asm += label+"\n";
                break;
            case "ELSE":
                asm += "label" + labelNum + "end\n";
                break;
            case "WHILE":
                asm += "label" + labelNum + "end\n";
                asm += "JA " + labelNum + "\n";
                break;
            case "dummy":
                break;
            case "WRITE":
                break;
            case "READ":
                break;
            case "==":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FCOMP LD \n";
                asm += "FSTSW dest \n";
                asm += "SAHF \n";
                asm += "JNE label" + labelNum + "end \n";
                labelStack.push("label"+labelNum+"end");
                labelNum++;
                break;
            case "!=":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FCOMP LD \n";
                asm += "FSTSW dest \n";
                asm += "SAHF \n";
                asm += "JE label" + labelNum + "end \n";
                labelStack.push("label"+labelNum+"end");
                labelNum++;
                break;
            case "<":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FCOMP LD \n";
                asm += "FSTSW dest \n";
                asm += "SAHF \n";
                asm += "fxch \n";
                asm += "JAE label" + labelNum + "end \n";
                labelStack.push("label"+labelNum+"end");
                labelNum++;
                break;
            case ">":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FCOMP LD \n";
                asm += "FSTSW dest \n";
                asm += "SAHF \n";
                asm += "fxch \n";
                asm += "JNA label" + labelNum + "end \n";
                labelStack.push("label"+labelNum+"end");
                labelNum++;
                break;
            case ">=":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FCOMP LD \n";
                asm += "FSTSW dest \n";
                asm += "SAHF \n";
                asm += "fxch \n";
                asm += "JB label" + labelNum + "end \n";
                labelStack.push("label"+labelNum+"end");
                labelNum++;
                break;
            case "<=":
                if(nodo.getLeft().isLeaf()){
                    asm += "FLD " + nodo.getLeft().getPayload() + "\n";
                }
                if(nodo.getRight().isLeaf()){
                    asm += "FLD " + nodo.getRight().getPayload() + "\n";
                }
                asm += "FCOMP LD \n";
                asm += "FSTSW dest \n";
                asm += "SAHF \n";
                asm += "fxch \n";
                asm += "JA label" + labelNum + "end \n";
                labelStack.push("label"+labelNum+"end");
                labelNum++;
                break;
            case "AND":
                break;
            case "OR":
                break;
            case "NOT":
                break;
            default:
                /*if(SymbolTableGenerator.containsSymbol(name)){
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
                }*/
                break;
        }
        return out + asm;
    }
}
