package io;

import com.google.gson.*;
import model.Edge;
import model.Graph;
import java.io.*;
import java.util.*;

public class JsonReader {
    public static List<Graph> readGraphs(String filename) throws IOException {
        JsonObject json = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();
        List<Graph> graphs = new ArrayList<>();
        JsonArray arr = json.getAsJsonArray("graphs");

        for (JsonElement e : arr) {
            JsonObject obj = e.getAsJsonObject();
            List<String> nodes = new ArrayList<>();
            obj.getAsJsonArray("nodes").forEach(n -> nodes.add(n.getAsString()));

            List<Edge> edges = new ArrayList<>();
            obj.getAsJsonArray("edges").forEach(ed -> {
                JsonObject eo = ed.getAsJsonObject();
                edges.add(new Edge(eo.get("from").getAsString(),
                        eo.get("to").getAsString(),
                        eo.get("weight").getAsDouble()));
            });
            graphs.add(new Graph(nodes, edges));
        }
        return graphs;
    }
}