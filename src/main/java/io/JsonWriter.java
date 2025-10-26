package io;

import com.google.gson.*;
import model.Edge;
import java.io.*;
import java.util.*;

public class JsonWriter {
    public static void writeResults(String filename, List<JsonObject> results) throws IOException {
        JsonObject root = new JsonObject();
        JsonArray arr = new JsonArray();
        results.forEach(arr::add);
        root.add("results", arr);
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
        }
    }

    public static JsonObject createResult(int id, int v, int e,
                                          List<Edge> primMST, double primCost, long primOps, double primTime,
                                          List<Edge> kruskalMST, double kruskalCost, long kruskalOps, double kruskalTime) {
        JsonObject res = new JsonObject();
        res.addProperty("graph_id", id);

        JsonObject inputStats = new JsonObject();
        inputStats.addProperty("vertices", v);
        inputStats.addProperty("edges", e);
        res.add("input_stats", inputStats);

        JsonObject prim = algoBlock(primMST, primCost, primOps, primTime);
        JsonObject kruskal = algoBlock(kruskalMST, kruskalCost, kruskalOps, kruskalTime);

        res.add("prim", prim);
        res.add("kruskal", kruskal);

        return res;
    }

    private static JsonObject algoBlock(List<Edge> mst, double cost, long ops, double time) {
        JsonObject algo = new JsonObject();
        JsonArray mstArr = new JsonArray();
        for (Edge e : mst) {
            JsonObject o = new JsonObject();
            o.addProperty("from", e.from);
            o.addProperty("to", e.to);
            o.addProperty("weight", e.weight);
            mstArr.add(o);
        }
        algo.add("mst_edges", mstArr);
        algo.addProperty("total_cost", cost);
        algo.addProperty("operations_count", ops);
        algo.addProperty("execution_time_ms", time);
        return algo;
    }
}
