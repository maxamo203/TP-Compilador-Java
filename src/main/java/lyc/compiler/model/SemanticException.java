package lyc.compiler.model;

import java.io.Serial;

public class SemanticException extends CompilerException {

    @Serial
    private static final long serialVersionUID = -6649278000190971816L;

    public SemanticException(String message) {
        super(message);
    }

}