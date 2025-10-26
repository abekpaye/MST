package prim;

import model.Edge;
import model.Graph;
import model.OperationCounter;

import java.util.*;

public class PrimAlgorithm {
    public static List<Edge> findMST(Graph g, OperationCounter counter) {
        Set<String> visited = new HashSet<>();
        List<Edge> mst = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        String start = g.nodes.get(0);
        visited.add(start);
        pq.addAll(g.getAdj(start));

        while (!pq.isEmpty() && mst.size() < g.getVertexCount() - 1) {
            Edge e = pq.poll();
            counter.add(1);
            String next = visited.contains(e.from) ? e.to : e.from;
            if (visited.contains(next)) continue;
            visited.add(next);
            mst.add(e);
            for (Edge ne : g.getAdj(next)) {
                if (!visited.contains(ne.from) || !visited.contains(ne.to)) pq.add(ne);
            }
        }
        return mst;
    }
}
