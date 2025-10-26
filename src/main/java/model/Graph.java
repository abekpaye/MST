package model;

import java.util.*;

public class Graph {
    public List<String> nodes;
    public List<Edge> edges;

    public Graph(List<String> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public int getVertexCount() { return nodes.size(); }
    public int getEdgeCount() { return edges.size(); }

    public List<Edge> getAdj(String node) {
        List<Edge> res = new ArrayList<>();
        for (Edge e : edges) {
            if (e.from.equals(node) || e.to.equals(node)) res.add(e);
        }
        return res;
    }
}
