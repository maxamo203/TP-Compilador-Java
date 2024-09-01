package lyc.compiler.files;

public class Symbol {
    // Definimos constantes estáticas para representar los tipos de datos
    public static final int UNKNOWN = 0;
    public static final int INTEGER = 1;
    public static final int FLOAT = 2;
    public static final int STRING = 3; 

    private String nombre;
    private int tipoDato;
    private String valor;
    private int longitud;

    

    public Symbol(String nombre, int tipoDato, String valor, int longitud) {
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

    public int getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(int tipoDato) {
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
