import com.google.gson.JsonObject;
import io.JsonReader;
import io.JsonWriter;
import kruskal.KruskalAlgorithm;
import model.Edge;
import model.Graph;
import model.OperationCounter;
import prim.PrimAlgorithm;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String input = "src/main/resources/assign_3_input.json";
        String output = "src/main/resources/assign_3_output.json";

        List<Graph> graphs = JsonReader.readGraphs(input);
        List<JsonObject> results = new ArrayList<>();

        int id = 1;
        for (Graph g : graphs) {
            OperationCounter primOps = new OperationCounter();
            long start = System.nanoTime();
            List<Edge> primMST = PrimAlgorithm.findMST(g, primOps);
            double primTime = (System.nanoTime() - start) / 1_000_000.0;
            double primCost = primMST.stream().mapToDouble(e -> e.weight).sum();

            OperationCounter kruskalOps = new OperationCounter();
            start = System.nanoTime();
            List<Edge> kruskalMST = KruskalAlgorithm.findMST(g, kruskalOps);
            double kruskalTime = (System.nanoTime() - start) / 1_000_000.0;
            double kruskalCost = kruskalMST.stream().mapToDouble(e -> e.weight).sum();

            JsonObject res = JsonWriter.createResult(id++, g.getVertexCount(), g.getEdgeCount(),
                    primMST, primCost, primOps.get(), primTime,
                    kruskalMST, kruskalCost, kruskalOps.get(), kruskalTime);
            results.add(res);
        }

        JsonWriter.writeResults(output, results);
        System.out.println("Results written to " + output);
    }
}