import kruskal.KruskalAlgorithm;
import model.Edge;
import model.Graph;
import model.OperationCounter;
import org.junit.jupiter.api.Test;
import prim.PrimAlgorithm;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestMST {

    private Graph createTestGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 4),
                new Edge("A", "C", 3),
                new Edge("B", "C", 2),
                new Edge("B", "D", 5),
                new Edge("C", "D", 7),
                new Edge("C", "E", 8),
                new Edge("D", "E", 6)
        );
        return new Graph(nodes, edges);
    }

    private boolean isConnected(List<Edge> mst, List<String> nodes) {
        Map<String, List<String>> adj = new HashMap<>();
        for (String node : nodes) adj.put(node, new ArrayList<>());
        for (Edge e : mst) {
            adj.get(e.from).add(e.to);
            adj.get(e.to).add(e.from);
        }

        Set<String> visited = new HashSet<>();
        Queue<String> q = new LinkedList<>();
        q.add(nodes.get(0));

        while (!q.isEmpty()) {
            String current = q.poll();
            visited.add(current);
            for (String neighbor : adj.get(current)) {
                if (!visited.contains(neighbor)) q.add(neighbor);
            }
        }
        return visited.size() == nodes.size();
    }

    private boolean hasCycle(List<Edge> mst, List<String> nodes) {
        Map<String, String> parent = new HashMap<>();
        for (String node : nodes) parent.put(node, node);

        for (Edge e : mst) {
            String u = find(parent, e.from);
            String v = find(parent, e.to);
            if (u.equals(v)) return true;
            parent.put(u, v);
        }
        return false;
    }

    private String find(Map<String, String> parent, String node) {
        if (!parent.get(node).equals(node)) parent.put(node, find(parent, parent.get(node)));
        return parent.get(node);
    }

    @Test
    void testMSTCorrectnessAndPerformance() {
        Graph graph = createTestGraph();

        OperationCounter primOps = new OperationCounter();
        long primStart = System.nanoTime();
        List<Edge> primMST = PrimAlgorithm.findMST(graph, primOps);
        double primTimeMs = (System.nanoTime() - primStart) / 1_000_000.0;
        double primCost = primMST.stream().mapToDouble(e -> e.weight).sum();

        OperationCounter kruskalOps = new OperationCounter();
        long kruskalStart = System.nanoTime();
        List<Edge> kruskalMST = KruskalAlgorithm.findMST(graph, kruskalOps);
        double kruskalTimeMs = (System.nanoTime() - kruskalStart) / 1_000_000.0;
        double kruskalCost = kruskalMST.stream().mapToDouble(e -> e.weight).sum();

        assertTrue(isConnected(primMST, graph.nodes), "Prim MST is not connected");
        assertTrue(isConnected(kruskalMST, graph.nodes), "Kruskal MST is not connected");

        assertFalse(hasCycle(primMST, graph.nodes), "Prim MST has a cycle");
        assertFalse(hasCycle(kruskalMST, graph.nodes), "Kruskal MST has a cycle");

        assertEquals(graph.getVertexCount() - 1, primMST.size(), "Prim MST should have V-1 edges");
        assertEquals(graph.getVertexCount() - 1, kruskalMST.size(), "Kruskal MST should have V-1 edges");

        assertEquals(primCost, kruskalCost, 0.0001, "Prim and Kruskal MST costs must match");

        assertTrue(primTimeMs >= 0, "Prim execution time must be non-negative");
        assertTrue(kruskalTimeMs >= 0, "Kruskal execution time must be non-negative");

        assertTrue(primOps.get() >= 0, "Prim operations count must be non-negative");
        assertTrue(kruskalOps.get() >= 0, "Kruskal operations count must be non-negative");

        List<Edge> primMST2 = PrimAlgorithm.findMST(graph, new OperationCounter());
        List<Edge> kruskalMST2 = KruskalAlgorithm.findMST(graph, new OperationCounter());
        double primCost2 = primMST2.stream().mapToDouble(e -> e.weight).sum();
        double kruskalCost2 = kruskalMST2.stream().mapToDouble(e -> e.weight).sum();
        assertEquals(primCost, primCost2, 0.0001, "Prim MST cost should be reproducible");
        assertEquals(kruskalCost, kruskalCost2, 0.0001, "Kruskal MST cost should be reproducible");
    }

    @Test
    void testDisconnectedGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C");
        List<Edge> edges = Collections.singletonList(new Edge("A", "B", 1));
        Graph graph = new Graph(nodes, edges);

        OperationCounter primOps = new OperationCounter();
        long primStart = System.nanoTime();
        List<Edge> primMST = PrimAlgorithm.findMST(graph, primOps);
        double primTimeMs = (System.nanoTime() - primStart) / 1_000_000.0;
        assertTrue(primMST.size() < graph.getVertexCount() - 1);
        assertTrue(primTimeMs >= 0);

        OperationCounter kruskalOps = new OperationCounter();
        long kruskalStart = System.nanoTime();
        List<Edge> kruskalMST = KruskalAlgorithm.findMST(graph, kruskalOps);
        double kruskalTimeMs = (System.nanoTime() - kruskalStart) / 1_000_000.0;
        assertTrue(kruskalMST.size() < graph.getVertexCount() - 1);
        assertTrue(kruskalTimeMs >= 0);
    }
}
