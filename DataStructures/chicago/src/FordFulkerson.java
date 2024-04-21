/**Name : Shree Bhavya Kandula 
*Student ID : 807204958
*I pledge that I did not got involved in any activity(copy/paste,resubmit,retype code etc) *that might be framed as plagiarism or cheating
*Copyright(c) [Shree Bhavya Kandula]
*All rights reserved to me.   
**/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class FordFulkerson {

    private static double[][] residualGraph;
    private static int numVertices;
    private static final int source = 0;
    private static int sink;
/**
     * Executes the Ford-Fulkerson algorithm on the given graph and returns the maximum flow.
     *
     * @param graph The input graph represented as a 2D array.
     * @return The maximum flow in the graph.
     * Shree bhavya kandula 11-13-23 6:30 am
     */
    public static double fordFulkerson(double[][] graph) {
        numVertices = graph.length;
        residualGraph = new double[numVertices][numVertices];
        IntStream.range(0, numVertices).forEach(i ->
                System.arraycopy(graph[i], 0, residualGraph[i], 0, numVertices));

        double maxFlow = 0;
        int[] parent = new int[numVertices];

        while (bfs(residualGraph, parent)) {
            double pathFlow = findMinPathFlow(residualGraph, parent);
            maxFlow += pathFlow;
            updateResidualGraph(residualGraph, parent, pathFlow);
        }

        return maxFlow;
    }
 /**
     * Performs a Breadth-First Search (BFS) on the residual graph to find augmenting paths.
     *
     * @param residualGraph The residual graph to search.
     * @param parent        An array that stores the parent of each vertex in the augmenting path.
     * @return True if an augmenting path is found; otherwise, false.
     * Shree bhavya kandula 11-13-23 11:30 pm 
     */
    private static boolean bfs(double[][] residualGraph, int[] parent) {
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && residualGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        return visited[sink];
    }

     /**
     * Finds the minimum flow along the augmenting path in the residual graph.
     *
     * @param residualGraph The residual graph to find the minimum path flow.
     * @param parent        An array that stores the parent of each vertex in the augmenting path.
     * @return The minimum flow along the augmenting path.
     * shree bhavya kandula 11-14-23 2:10 am
     */
    private static double findMinPathFlow(double[][] residualGraph, int[] parent) {
        return IntStream.iterate(sink, v -> v != source, v -> parent[v])
                        .mapToDouble(v -> residualGraph[parent[v]][v])
                        .min().orElse(Double.MAX_VALUE);
    }


    /**
     * Updates the residual graph by subtracting the path flow from forward edges
     * and adding it to backward edges along the augmenting path.
     *
     * @param residualGraph The residual graph to update.
     * @param parent        An array that stores the parent of each vertex in the augmenting path.
     * @param pathFlow      The flow along the augmenting path.
     */
    private static void updateResidualGraph(double[][] residualGraph, int[] parent, double pathFlow) {
        IntStream.iterate(sink, v -> v != source, v -> parent[v])
                 .forEach(v -> {
                     residualGraph[parent[v]][v] -= pathFlow;
                     residualGraph[v][parent[v]] += pathFlow;
                 });
    }
    /**
     * Main method to read the input file, process multiple graphs, and apply the Ford-Fulkerson algorithm.
     * @param args Command-line arguments. Expects a single argument: the input file name.
     * shree bhavya kandula 11-15-23 5:30 am 
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java FordFulkerson <file-name>");
            return;
        }

        String fileName = args[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            List<double[]> graphLines = new ArrayList<>();
            boolean readingGraph = false;
            int verticesCount = 0;
            int graphNumber = 1; // To keep track of graph numbers

            System.out.println("Ford-Fulkerson algorithm");
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Trim leading and trailing spaces from the line
                if (line.startsWith("** G")) {
                    if (!graphLines.isEmpty()) {
                        // System.out.println("** G" + graphNumber + ":\t|V|=" + verticesCount);
                        processGraph(graphLines, verticesCount,graphNumber);
                        graphLines.clear();
                        graphNumber++; // Increment graph number for the next graph
                    }
                    Matcher m = Pattern.compile("\\|V\\|=([0-9]+)").matcher(line);
                    if (m.find()) {
                        verticesCount = Integer.parseInt(m.group(1)); // Extract the number of vertices
                        readingGraph = true; // Start reading graph lines
                        graphLines = new ArrayList<>(verticesCount); 
                    }
                } else if (readingGraph) {
                    line = line.trim(); 
                    Matcher edgeMatcher = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)").matcher(line);
                    if (edgeMatcher.find()) {
                        int u = Integer.parseInt(edgeMatcher.group(1));
                        int v = Integer.parseInt(edgeMatcher.group(2));
                        double weight = Double.parseDouble(edgeMatcher.group(3));

                        
                        while (graphLines.size() <= u) {
                            graphLines.add(new double[verticesCount]);
                        }
                        
                        if (graphLines.get(u).length < verticesCount) {
                            graphLines.set(u, new double[verticesCount]);
                        }
                        graphLines.get(u)[v] = weight; 
                    }
            
                }
            }
            if (!graphLines.isEmpty()) {
                processGraph(graphLines, verticesCount,graphNumber); 
            }
            

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading the file.");
        }
        System.out.println("Asg 7 by Shree Bhavya Kandula");
    }
    /**
 * Processes a graph and calculates the maximum flow using the Ford-Fulkerson algorithm.
 * @param graphLines A list of double arrays representing the graph's edges and capacities.
 * @param verticesCount The number of vertices in the graph.
 * @param graphNumber The number of the graph being processed.
 * shree bhavya kandla 11-18-23 8:50 am
 */
    private static void processGraph(List<double[]> graphLines, int verticesCount, int graphNumber) {
        double[][] graph = new double[verticesCount][verticesCount];
        for (int i = 0; i < graphLines.size(); i++) {
            graph[i] = graphLines.get(i);
        }
        sink = verticesCount - 1; 
        
        long startTime = System.currentTimeMillis(); 
        double maxFlow = fordFulkerson(graph);
        long endTime = System.currentTimeMillis(); 
        
        long duration = endTime - startTime; 
        
        printGraph(graph, maxFlow, verticesCount, duration, graphNumber);
    }
    
    /**
 * Prints the graph, its maximum flow, and other information.
 * @param graph The graph represented as a 2D array.
 * @param maxFlow The maximum flow calculated for the graph.
 * @param verticesCount The number of vertices in the graph.
 * @param duration The time taken to compute the maximum flow.
 * @param graphNumber The number of the graph being processed.
 */
    private static void printGraph(double[][] graph, double maxFlow, int verticesCount, long duration, int graphNumber) {
        if (verticesCount <= 10) {
            System.out.println("** G" + graphNumber + ":\t|V|=" + verticesCount);
            System.out.println("Flow network:");
            printMatrix(graph, verticesCount); 
            System.out.println("------------------------------");
            System.out.println("Maximum flow:");
            double[][] flowNetwork = calculateFlowNetwork(graph, residualGraph, verticesCount);
            printMatrix(flowNetwork, verticesCount); 
            System.out.println("Max flow ==> " + maxFlow + " (" + duration + " ms) \n"); 
        } else {
            System.out.println("** G" + graphNumber + ":\t|V|=" + verticesCount);
        System.out.println("Max flow ==> " + maxFlow + " (" + duration + " ms) \n"); 
        }
    }
    
    
/**
 * Prints a 2D matrix.
 * @param matrix The matrix to be printed.
 * @param verticesCount The number of vertices in the graph (for formatting).
 * shree bhavya kandula 11-18-23 7:05 pm 
 */
    
    private static void printMatrix(double[][] matrix, int verticesCount) {
        // Print column headers
        System.out.print("      ");
        for (int i = 0; i < verticesCount; i++) {
            System.out.printf("%6s", i + ":");
        }
        System.out.println();
    
        // Print rows
        for (int i = 0; i < verticesCount; i++) {
            System.out.printf("%4s: ", i);
            for (int j = 0; j < verticesCount; j++) {
                if (matrix[i][j] <= 0) { // Check for non-positive values
                    System.out.print("     -");
                } else {
                    System.out.printf("%6.0f", matrix[i][j]);
                }
            }
            System.out.println();
        }
    }
    
    
/**
 * Calculates the flow network from the original graph and its residual graph.
 * @param graph The original graph represented as a 2D array.
 * @param residualGraph The residual graph represented as a 2D array.
 * @param verticesCount The number of vertices in the graph.
 * @return A 2D array representing the flow network.
 * shree bhavya kandula 11-18-23 11:00 pm
 */
    
    private static double[][] calculateFlowNetwork(double[][] graph, double[][] residualGraph, int verticesCount) {
        double[][] flowNetwork = new double[verticesCount][verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            for (int j = 0; j < verticesCount; j++) {
                flowNetwork[i][j] = graph[i][j] - residualGraph[i][j];
            }
        }
        return flowNetwork;
    }
}
