package lyc.compiler.files;
import java.util.Stack;

public class Punteros {
    private static Stack<Nodo> nodoStack = new Stack<>();

    public static Nodo p_root;
    public static Nodo p_programa;
    public static Nodo p_lista_sentencias;
    public static Nodo p_sentencia;
    public static Nodo p_seleccion;
    public static Nodo p_while;
    public static Nodo p_condicion;
    public static Nodo p_cuerpoIf;
    public static Nodo p_cuerpoElse;
    public static Nodo p_asignacion;
    public static Nodo p_expresion;
    public static Nodo p_expresion_texto;
    public static Nodo p_term;
    public static Nodo p_factor;
    public static Nodo p_triangulo;
    public static Nodo p_getPenultimatePosition;
    public static Nodo p_read;
    public static Nodo p_write;

    public static void Apilar(Nodo n){
        nodoStack.push(n);
    }
    public static Nodo Desapilar(){
        return nodoStack.pop();
    }
    public static Nodo getTop(){
        try{
            return nodoStack.peek();
        }catch(Exception e){
            return null;
        }
    }
    public static boolean isEmptyStack(){
        return nodoStack.isEmpty();
    }
}
