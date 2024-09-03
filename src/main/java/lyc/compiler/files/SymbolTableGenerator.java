package lyc.compiler.files;
import java.util.Map;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
//import lyc.compiler.files.Symbol;
  

public class SymbolTableGenerator implements FileGenerator{

    private static Map<String, Symbol> symbolTable = new HashMap<>();
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
    public static int addSymbol(String name, int type, String value, int length){
        if(symbolTable.containsKey(name)){
            return -1;
        }
        symbolTable.put(name, new Symbol(name, type, value, length));
        return 0;
    }
    // public static void addSymbol( Symbol symbol){
    //     symbolTable.put(symbol.getNombre(), symbol);
    // }
    public static Symbol getSymbol(String name){
        return symbolTable.get(name);
    }
}
