package lyc.compiler.files;

public class Symbol {
    // Definimos constantes estáticas para representar los tipos de datos
    public static final String UNKNOWN = "UNKNOWN";
    public static final String INTEGER = "INTEGER";
    public static final String FLOAT = "FLOAT";
    public static final String STRING = "STRING";
    public static final String CTE_INTEGER = "CTE_INTEGER";
    public static final String CTE_FLOAT = "CTE_FLOAT";
    public static final String CTE_STRING = "CTE_STRING";

    private String nombre;
    private String tipoDato;
    private String valor;
    private int longitud;

    

    public Symbol(String nombre, String tipoDato, String valor, int longitud) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.valor = valor;
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return nombre + "||" + tipoDato + "||" + valor + "||" + longitud;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

}
