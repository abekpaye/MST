package kruskal;

import model.Edge;
import model.Graph;
import model.OperationCounter;

import java.util.*;

public class KruskalAlgorithm {
    public static List<Edge> findMST(Graph g, OperationCounter counter) {
        List<Edge> mst = new ArrayList<>();
        Collections.sort(g.edges); // сортировка по весу
        UnionFind uf = new UnionFind();
        for (String node : g.nodes) uf.makeSet(node);

        for (Edge e : g.edges) {
            counter.add(1);
            String uRoot = uf.find(e.from);
            String vRoot = uf.find(e.to);
            if (!uRoot.equals(vRoot)) {
                mst.add(e);
                uf.union(uRoot, vRoot);
            }
        }
        return mst;
    }
}