package lyc.compiler.model;

public class FloatOutOfRangeException extends CompilerException{

    public FloatOutOfRangeException(String message) {
        super("Valor fuera del rango de float de 32 bits: " + message);
    }
}