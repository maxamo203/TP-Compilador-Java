package lyc.compiler.model;

import java.io.Serial;

public class InvalidLengthIdException extends CompilerException {

    @Serial
    private static final long serialVersionUID = -6649278000190971816L;

    public InvalidLengthIdException(String message) {
        super("Longitud Invalida de Identificador: " + message);
    }

}
