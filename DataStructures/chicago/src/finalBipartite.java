//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.Scanner;
//
//class MaxMatch {
//    private int V; // Number of vertices
//    private ArrayList<ArrayList<Integer>> adj; // Adjacency list representation of the graph
//    private int[] parent;
//
//
//    public MaxMatch(int v) {
//        V = v;
//        adj = new ArrayList<>(V);
//        for (int i = 0; i < V; ++i)
//            adj.add(new ArrayList<>());
//        parent = new int[V];
//
//    }
//
//    // Function to add an edge to the graph
//    public void addEdge(int u, int v) {
//        adj.get(u).add(v);
//        adj.get(v).add(u);
//    }
//
//    // Function to check if the graph is bipartite using BFS
//    private boolean isBipartite() {
//        int[] color = new int[V];
//        for (int i = 0; i < V; ++i)
//            color[i] = -1;
//
//        for (int i = 0; i < V; ++i) {
//            if (color[i] == -1) {
//                if (!bipartiteBFS(i, color))
//                    return false;
//            }
//        }
//        return true;
//    }
//
//    // Helper function for BFS traversal to check bipartiteness
//    private boolean bipartiteBFS(int src, int[] color) {
//        color[src] = 1;
//        parent[src] = -1;
//
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(src);
//
//        while (!queue.isEmpty()) {
//            int u = queue.poll();
//
//            for (int v : adj.get(u)) {
//                if (color[v] == -1) {
//                    color[v] = 1 - color[u];
//                    parent[v] = u;
//                    queue.add(v);
//                } else if (color[v] == color[u]) {
//                    return false; // Graph is not bipartite
//                }
//            }
//        }
//
//        return true; // Graph is bipartite
//    }
//    // Function to find the maximum number of matches using Ford-Fulkerson algorithm
//    private int maxMatches() {
//        int[][] graph = new int[V][V];
//        for (int i = 0; i < V; ++i) {
//            for (int j : adj.get(i)) {
//                graph[i][j] = 1;
//            }
//        }
//        int maxMatches =0;
//
//        int[][] residualGraph = new int[V][V];
//        int[] color = new int[V];  // Initialize the color array outside the loop
//
//        // Perform BFS only once and store the result in the parent array
//        bipartiteBFS(0, color);
//
//        while (parent[V - 1] != -1) {
//            System.arraycopy(graph, 0, residualGraph, 0, V);
//
//            int pathFlow = Integer.MAX_VALUE;
//            for (int v = V - 1; v != 0; v = parent[v]) {
//                int u = parent[v];
//                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
//            }
//
//            for (int v = V - 1; v != 0; v = parent[v]) {
//                int u = parent[v];
//                residualGraph[u][v] -= pathFlow;
//                residualGraph[v][u] += pathFlow;
//            }
//            if (pathFlow == Integer.MAX_VALUE) {
//                break;  // No positive path found, exit the loop
//            }
//
//            maxMatches += pathFlow;
//
//            // Perform BFS again to update the parent array for the next iteration
//            bipartiteBFS(0, color);
//        }
//
//        return maxMatches;
//    }
//
//
//
////    // Function to find the maximum number of matches using Ford-Fulkerson algorithm
////    private int maxMatches() {
////
////
////
////        int[][] graph = new int[V][V];
////        for (int i = 0; i < V; ++i) {
////            for (int j : adj.get(i)) {
////                graph[i][j] = 1;
////            }
////        }
////
////        // Find the maximum number of matches using Ford-Fulkerson
////        int[][] residualGraph = new int[V][V];
////        for (int i = 0; i < V; ++i)
////            System.arraycopy(graph[i], 0, residualGraph[i], 0, V);
////
////        int maxMatches = 0;
////        while (bipartiteBFS(0, new int[V])) {
////            int pathFlow = Integer.MAX_VALUE;
////            for (int v = V - 1; v != 0; v = parent[v]) {
////                int u = parent[v];
////                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
////            }
////
////            for (int v = V - 1; v != 0; v = parent[v]) {
////                int u = parent[v];
////                residualGraph[u][v] -= pathFlow;
////                residualGraph[v][u] += pathFlow;
////            }
////
////            maxMatches += pathFlow;
////        }
////
////        return maxMatches;
////    }
//
//    // Function to print the maximum number of matches
//    public void printMaxMatches() {
//        if (!isBipartite()) {
//            System.out.println("The graph is not bipartite.");
//        } else {
//            System.out.println("The graph is  bipartite.");
//            int maxMatches = maxMatches();
//            System.out.println("The maximum number of matches is: " + maxMatches);
//        }
//    }
//
//    public static void main(String[] args) {
//
//
//            int V = 10;
//        MaxMatch graph = new MaxMatch(V);
//
//
//        graph.addEdge(0, 2);
//        graph.addEdge(0, 4);
//        graph.addEdge(0, 5);
//        graph.addEdge(0, 9);
//        graph.addEdge(1, 2);
//        graph.addEdge(1, 3);
//        graph.addEdge(1, 4);
//        graph.addEdge(1, 5);
//        graph.addEdge(2, 6);
//        graph.addEdge(2, 7);
//        graph.addEdge(2, 8);
//        graph.addEdge(3, 6);
//        graph.addEdge(3, 7);
//        graph.addEdge(3, 8);
//        graph.addEdge(4, 6);
//        graph.addEdge(4, 7);
//        graph.addEdge(4, 8);
//        graph.addEdge(5, 6);
//        graph.addEdge(5, 7);
//        graph.addEdge(5, 8);
//        graph.addEdge(7, 9);
//        graph.addEdge(8, 9);
//
//
//        graph.printMaxMatches();
//
//    }
//}
