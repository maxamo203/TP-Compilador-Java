package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import static guru.nidi.graphviz.model.Factory.*;
import guru.nidi.graphviz.engine.Format;

public class IntermediateCodeGenerator implements FileGenerator {
    private static int tempCount = 0;
    public static MutableGraph graficarNodo(Nodo nodo) {
        if (nodo == null) {
            return mutGraph();
        }

        // Creamos un nuevo nodo cada vez, sin importar si su contenido es igual
        MutableNode nodoActual = mutNode(tempCount++ +"_"+ nodo.getPayload());

        // Graficamos recursivamente los nodos hijos
        if (nodo.getLeft() != null) {
            nodoActual.addLink(graficarNodo(nodo.getLeft()).rootNodes().iterator().next());
        }
        if (nodo.getRight() != null) {
            nodoActual.addLink(graficarNodo(nodo.getRight()).rootNodes().iterator().next());
        }

        return mutGraph().add(nodoActual);
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("TODO");
        Graphviz.fromGraph(graficarNodo(Punteros.p_root)).width(10000).render(Format.PNG).toFile(new java.io.File("target/output/intermediate-code.png"));
    }
}
