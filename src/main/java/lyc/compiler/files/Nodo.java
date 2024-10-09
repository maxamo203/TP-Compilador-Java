package lyc.compiler.files;
import java_cup.runtime.Symbol;

public class Nodo {
    private String payload;
    private Nodo left;
    private Nodo right;

    private Nodo(String payload){
        this.payload = payload;
        this.left = null;
        this.right = null;
    }
    private Nodo(String payload, Nodo left, Nodo right){
        this.payload = payload;
        this.left = left;
        this.right = right;
    }
    public static Nodo crearHijo(String payload){
        return new Nodo(payload);
    }
    public static Nodo crearNodo(String payload, Nodo left, Nodo right){
        return new Nodo(payload, left, right);
    }
    public String getPayload(){
        return this.payload;
    }
    public Nodo getLeft(){
        return this.left;
    }
    public Nodo getRight(){
        return this.right;
    }
}
