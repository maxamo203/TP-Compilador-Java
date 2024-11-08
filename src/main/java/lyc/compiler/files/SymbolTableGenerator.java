package lyc.compiler.files;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.model.*;
//import lyc.compiler.files.Symbol;
  

public class SymbolTableGenerator implements FileGenerator{

    private static Map<String, Symbol> symbolTable = new HashMap<>();
    private static Stack<String> idStack = new Stack<>();
    public static int pruba = 4;
    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        // addSymbol("a", Symbol.INTEGER, "5", 4);
        // addSymbol("b", Symbol.FLOAT, "5.5", 4);
        // addSymbol("c", Symbol.STRING, "hola", 4);
        // addSymbol("d", Symbol.INTEGER, "5", 4); //pruebas para ver como queda el archivo
        fileWriter.write("Nombre||Tipo de dato||Valor||Longitud\n");
        for (Map.Entry<String, Symbol> entry : symbolTable.entrySet()) {
            fileWriter.write(entry.getValue().toString() + "\n");
        }
    }
    public static int addSymbol(String name, String type, String value, int length){
        if(symbolTable.containsKey(name)){
            return -1;
        }
        symbolTable.put(name, new Symbol(name, type, value, length));
        return 0;
    }
    // public static void addSymbol( Symbol symbol){
    //     symbolTable.put(symbol.getNombre(), symbol);
    // }
    public static Symbol getSymbol(String name) throws SemanticException {
        if(!symbolTable.containsKey(name)){
            throw new SemanticException("No existe una variable definida como: <<" + name + ">>");
        }
        return symbolTable.get(name);
    }
    public static boolean containsSymbol(String name){
        return symbolTable.containsKey(name);
    }
    public static void pushId(String id){
        idStack.push(id);
    }
    public static String popId(){
        return idStack.pop();
    }
    public static boolean idStackIsEmpty(){
        return idStack.isEmpty();
    }
    public static HashMap<String, Symbol> getSymbolTable(){
        return (HashMap<String, Symbol>) symbolTable;
    }
}
