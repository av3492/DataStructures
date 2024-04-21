import java.util.*;

public class FordFulkersonMaxMatching {
    private static int fordFulkerson(List<Integer>[] graph, int source, int sink) {
        int vertices = graph.length;
        int[][] residualGraph = new int[vertices][vertices];
        for (int u = 0; u < vertices; u++) {
            for (int v : graph[u]) {
                residualGraph[u][v] = 1;
            }
        }

        int[] parent = new int[vertices];
        int maxMatching = 0;

        while (bfs(residualGraph, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }

            maxMatching += pathFlow;
        }

        return maxMatching;
    }

    private static boolean bfs(int[][] graph, int source, int sink, int[] parent) {
        int vertices = graph.length;
        boolean[] visited = new boolean[vertices];
        Arrays.fill(visited, false);

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < vertices; v++) {
                if (!visited[v] && graph[u][v] > 0) {
                    queue.offer(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return visited[sink];
    }

    public static void main(String[] args) {
        int vertices = 10;
        List<Integer>[] graph = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }

        // Add edges to the graph
        int[][] edges = {
                {0, 2}, {0, 4}, {0, 8}, {0, 9},
                {1, 2}, {1, 4}, {1, 8}, {1, 9},
                {2, 3}, {2, 5}, {2, 6}, {2, 7},
                {3, 4}, {3, 9},
                {4, 5}, {4, 6}, {4, 7},
                {5, 8}, {5, 9},
                {6, 8}, {6, 9},
                {7, 8}, {7, 9}
        };


        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]); // Undirected graph, so add reverse edges
        }

        int source = 0; // Add a source
        int sink = 9; // Add a sink

        // Connect source to the first set of vertices
        for (int i = 0; i < vertices / 2; i++) {
            graph[source].add(i);
        }

        // Connect sink to the second set of vertices
        for (int i = vertices / 2; i < vertices; i++) {
            graph[i].add(sink);
        }

        int maxMatching = fordFulkerson(graph, source, sink);
        System.out.println("Maximum Matching: " + maxMatching);
    }
}
