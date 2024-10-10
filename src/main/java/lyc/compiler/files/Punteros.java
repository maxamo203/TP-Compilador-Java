package lyc.compiler.files;
import java_cup.runtime.Symbol;
import java.util.Stack;

public class Punteros {
    private static Stack<Nodo> nodoStack = new Stack<>();

    public static Nodo p_root;
    public static Nodo p_programa;
    public static Nodo p_lista_sentencias;
    public static Nodo p_sentencia;
    public static Nodo p_asignacion;
    public static Nodo p_expresion;
    public static Nodo p_term;
    public static Nodo p_factor;
    public static Nodo p_triangulo;
    public static Nodo p_getPenultimatePosition;

    public static void Apilar(Nodo n){
        nodoStack.push(n);
    }
    public static Nodo Desapilar(){
        return nodoStack.pop();
    }
    public static Nodo getTop(){
        return nodoStack.peek();
    }
    public static boolean isEmptyStack(){
        return nodoStack.isEmpty();
    }
}
