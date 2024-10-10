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
    public static Nodo crearNodoDummy(Nodo left, Nodo right){
        return new Nodo("dummy", left, right);
    }
    public static Nodo agregarHijos(Nodo padre, Nodo hijoI, Nodo hijoD){
        if(padre.left != null || padre.right != null){
            throw new RuntimeException("Error: Nodo ya tiene hijos");
        }
        padre.left = hijoI;
        padre.right = hijoD;
        return padre;
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
    @Override
    public String toString(){
        return this.payload;
    }
}
