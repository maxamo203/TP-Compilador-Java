package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;

// Import necesario para Listas
import java.util.ArrayList;
import java.util.List;      

public class SymbolTableGenerator implements FileGenerator {

    private final List<Symbol> symbolTable;

    public SymbolTableGenerator() {
        this.symbolTable = new ArrayList<>();
    }

    public void addSymbol(String name, String dataType, String value, String length) {
        symbolTable.add(new Symbol(name, dataType, value, length));
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("NOMBRE|TIPODATO|VALOR|LONGITUD\n");

        // for (Symbol symbol : symbolTable) {
        //     fileWriter.write(symbol.toString() + "\n");
        // }
    }

     public List<Symbol> getTable() {
        return symbolTable;
    }
}
