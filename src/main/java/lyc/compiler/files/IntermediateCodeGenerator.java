package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;

import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import static guru.nidi.graphviz.model.Factory.*;
import guru.nidi.graphviz.engine.Format;

public class IntermediateCodeGenerator implements FileGenerator {

    public static MutableGraph graficarNodo(Nodo nodo) {
        if (nodo == null) {
            return mutGraph();
        }

        MutableNode nodoActual = mutNode(nodo.getPayload());
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
        Nodo raiz = Nodo.crearNodo("=", Nodo.crearHijo("a"), Nodo.crearHijo("b"));
        Graphviz.fromGraph(graficarNodo(raiz)).width(700).render(Format.PNG).toFile(new java.io.File("target/output/intermediate-code.png"));
    }
}
