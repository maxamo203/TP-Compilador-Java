package lyc.compiler.model;

public class InvalidIntegerException extends CompilerException{

    public InvalidIntegerException(String message) {
        super("Entero fuera del rango de entero de 16 bits"+message);
    }
}
