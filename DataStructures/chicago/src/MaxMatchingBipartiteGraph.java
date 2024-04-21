//import java.util.*;
//
//public class MaxMatchingBipartiteGraph {
//    private static int[][] capacity;
//
//    public static int edmondsKarp(List<Integer>[] graph, int source, int sink) {
//        int vertices = graph.length;
//        capacity = new int[vertices][vertices];
//
//        // Initialize the capacity matrix
//        for (int u = 0; u < vertices; u++) {
//            for (int v : graph[u]) {
//                capacity[u][v] = 1; // Each edge has a capacity of 1
//            }
//        }
//
//        int maxMatching = 0;
//
//        while (true) {
//            int[] parent = new int[vertices];
//            Arrays.fill(parent, -1);
//
//            Queue<Integer> queue = new LinkedList<>();
//            queue.offer(source);
//            parent[source] = source;
//
//            // BFS to find augmenting path
//            while (!queue.isEmpty() && parent[sink] == -1) {
//                int u = queue.poll();
//
//                for (int v : graph[u]) {
//                    if (parent[v] == -1 && capacity[u][v] > 0) {
//                        parent[v] = u;
//                        queue.offer(v);
//                    }
//                }
//            }
//
//            if (parent[sink] == -1) {
//                break; // No more augmenting paths
//            }
//
//            // Update capacities along the augmenting path
//            int pathFlow = Integer.MAX_VALUE;
//            for (int v = sink; v != source; v = parent[v]) {
//                int u = parent[v];
//                pathFlow = Math.min(pathFlow, capacity[u][v]);
//            }
//
//            for (int v = sink; v != source; v = parent[v]) {
//                int u = parent[v];
//                capacity[u][v] -= pathFlow;
//                capacity[v][u] += pathFlow;
//            }
//
//            maxMatching += pathFlow;
//        }
//
//        return maxMatching;
//    }
//
//    public static void main(String[] args) {
//        int vertices = 10;
//        List<Integer>[] graph = new ArrayList[vertices];
//        for (int i = 0; i < vertices; i++) {
//            graph[i] = new ArrayList<>();
//        }
//
//        // Add edges to the graph
//        int[][] edges = {
//                {0, 2}, {0, 4}, {0, 8}, {0, 9},
//                {1, 2}, {1, 4}, {1, 8}, {1, 9},
//                {2, 3}, {2, 5}, {2, 6}, {2, 7},
//                {3, 4}, {3, 9},
//                {4, 5}, {4, 6}, {4, 7},
//                {5, 8}, {5, 9},
//                {6, 8}, {6, 9},
//                {7, 8}, {7, 9}
//        };
//
//        for (int[] edge : edges) {
//            graph[edge[0]].add(edge[1]);
//            graph[edge[1]].add(edge[0]); // Undirected graph, so add reverse edges
//        }
//
//        int source = 0; // Add a source
//        int sink = 9; // Add a sink
//
//        // Connect source to the first set of vertices
//        for (int i = 0; i < vertices / 2; i++) {
//            graph[source].add(i);
//        }
//
//        // Connect sink to the second set of vertices
//        for (int i = vertices / 2; i < vertices; i++) {
//            graph[i].add(sink);
//        }
//
//        int maxMatching = edmondsKarp(graph, source, sink);
//        System.out.println("Maximum Matching: " + maxMatching);
//    }
//}
//
import java.util.*;

public class MaxMatchingBipartiteGraph {
    private static int[][] capacity;

    public static int edmondsKarp(List<Integer>[] graph, int source, int sink) {
        int vertices = graph.length;
        capacity = new int[vertices][vertices];

        // Initialize the capacity matrix
        for (int u = 0; u < vertices; u++) {
            for (int v : graph[u]) {
                capacity[u][v] = 1; // Each edge has a capacity of 1
            }
        }

        int maxMatching = 0;

        while (true) {
            int[] parent = new int[vertices];
            Arrays.fill(parent, -1);

            Queue<Integer> queue = new LinkedList<>();
            queue.offer(source);
            parent[source] = source;

            // BFS to find augmenting path
            while (!queue.isEmpty() && parent[sink] == -1) {
                int u = queue.poll();

                for (int v : graph[u]) {
                    if (parent[v] == -1 && capacity[u][v] > 0) {
                        parent[v] = u;
                        queue.offer(v);
                    }
                }
            }

            if (parent[sink] == -1) {
                break; // No more augmenting paths
            }

            // Update capacities along the augmenting path
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v]);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                capacity[u][v] -= pathFlow;
                capacity[v][u] += pathFlow;
            }

            maxMatching += pathFlow;
        }

        return maxMatching;
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

        int maxMatching = edmondsKarp(graph, source, sink);
        System.out.println("Maximum Matching: " + maxMatching);
    }
}



