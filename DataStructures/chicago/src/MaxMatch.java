import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
In this class, we will determine whether a given undirected graph is bipartite, and if it is, we will
find the maximum number of matches between the two disjoint and independent sets of vertices.
 */
public class MaxMatch {

    private static int verticesCount; // Number of vertices
    static ArrayList<Integer> setA; //  set of vertices
    static ArrayList<Integer> setB; // set of vertices
    private static ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();

    /**
     * Colors the vertices of a bipartite graph using Depth-First Search (DFS) and returns the sets.
     * @param currentVertex  The current vertex being processed.
     * @param vertexColors   Array representing the color assigned to each vertex.
     * @param currentColor   The current color (0 or 1) being assigned to the current vertex.
     * @return True if the graph is not bipartite, false if the graph is bipartite.
     */
    static boolean colorGraph(int currentVertex, int[] vertexColors, int currentColor) {
        vertexColors[currentVertex] = currentColor;
        if (currentColor == 0) {
            setA.add(currentVertex);
        } else {
            setB.add(currentVertex);
        }
        return checkColor(vertexColors, currentColor, currentVertex);
    }

    /**
     * Initializes the match array with default values.
     *
     * @return The initialized match array.
     */
    static int[] initializeMatchArray() {
        int[] verticesTrack = new int[setB.size()];
        Arrays.fill(verticesTrack, -1);
        return verticesTrack;
    }

    /**
     * This method creates a boolean adjacency matrix for the independent set of vertices
     * @return a 2-d boolean array
     */
    private static boolean[][] createAdjacencyMatrix() {
        int row = setA.size();
        int column = setB.size();
        boolean[][] booleanMatrix = new boolean[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int vertexA = setA.get(i);
                int vertexB = setB.get(j);
                // Checks if there is an edge between vertexA and vertexB
                if (adjList.get(vertexA).contains(vertexB) || adjList.get(vertexB).contains(vertexA)) {
                    booleanMatrix[i][j] = true;
                }
            }
        }

        return booleanMatrix;
    }

    /**
     * Finds the maximum number of matchings from set A to set B using the
     * Bipartite Matching algorithm.
     * @param booleanMatrix The bipartite graph represented as an adjacency matrix.
     * @return The maximum number of matchings from set A to set B.
     */
    static int maxMatches(boolean[][] booleanMatrix) {
        int[] verticesTrack = initializeMatchArray();
        int matchValue = 0;
        for (int vertexA = 0; vertexA < setA.size(); vertexA++) {
            boolean[] visited = initializeSeenArray();
            if (matchingPairs( vertexA, visited, verticesTrack,booleanMatrix)) {
                matchValue++;
            }
        }
        printAssignment(verticesTrack);
        return matchValue;
    }


    /**
     * Prints the matches between the two disjoint and independent sets of vertices.
     *
     * @param verticesTrack An array representing the indices pf matched vertices.
     */
    public static void printAssignment(int[] verticesTrack) {
        for (int j = 0; j < setB.size(); j++) {
            if (verticesTrack[j] != -1) {
                System.out.println("\t(" + setB.get(j) + "," + setA.get(verticesTrack[j]) + ")");
            }
        }
    }

    /**
     * This method gets total graphs in the file
     * @param line - line in the file
     * @return a int value
     */
    public static int getTotalGraphs(String line){
        int totalGraphs=0;
        Matcher matcher = Pattern.compile("\\b(\\d+)\\b").matcher(line);
        if (matcher.find()) {
            totalGraphs = Integer.parseInt(matcher.group(1));
        }
        return totalGraphs;
    }
    /**
     * Checks the color of the current vertex and its neighbors in a bipartite graph.
     *
     * @param vertexColors   Array representing the color assigned to each vertex.
     * @param currentColor     The current set (0 or 1) to which the current vertex belongs.
     * @param currentVertex  The current vertex being processed.
     * @return True if the graph is not bipartite, false if the graph is bipartite.
     */

    public static boolean checkColor(int[] vertexColors, int currentColor,int currentVertex){
        for (int neighbor : adjList.get(currentVertex)) {
            if (vertexColors[neighbor] == -1) {
                if (colorGraph(neighbor, vertexColors, 1 - currentColor)) {
                    return true;
                }
            } else if (vertexColors[neighbor] == currentColor) {
                return true;
            }
        }
        return false;
    }
    /**
     * Initializes the 'visited' array with default values.
     *
     * @return The initialized 'visited' array.
     */
    static boolean[] initializeSeenArray() {
        return new boolean[setB.size()];
    }

    /**
     * The Main Method
     * @param args - args
     */

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: java FordFulkerson <file-name>");
            return;
        }

        String fileName = args[0];


        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            setA = new ArrayList<>();
            setB = new ArrayList<>();

            boolean readingGraph = false;
            int graphNumber = 1;

            System.out.println("Maximum number of matches in bipartite graphs in "+fileName);

            int totalGraphs= getTotalGraphs( reader.readLine());


            while(graphNumber<=totalGraphs){
                setA = new ArrayList<>();
                setB = new ArrayList<>();

                String line;
                while ((line = reader.readLine()) != null && !line.equals("-------------------------")) {

                    line = line.trim(); // Trim leading and trailing spaces from the line
                    if (line.startsWith("** G")) {

                        Matcher m = Pattern.compile("\\|V\\|=([0-9]+)").matcher(line);
                        if (m.find()) {
                            verticesCount = Integer.parseInt(m.group(1)); // Extract the number of vertices
                            readingGraph = true; // Start reading graph lines
                            adjList = new ArrayList<>(verticesCount);
                            for (int i = 0; i < verticesCount; ++i)
                                adjList.add(new ArrayList<>());
                        }
                    } else if (readingGraph) {
                        line = line.trim();
                        Matcher edgeMatcher = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)").matcher(line);
                        if (edgeMatcher.find()) {
                            int u = Integer.parseInt(edgeMatcher.group(1));
                            int vertexB = Integer.parseInt(edgeMatcher.group(2));
                            adjList.get(u).add(vertexB);
                            adjList.get(vertexB).add(u);
                        }


                    }
                }
                long startTime = System.currentTimeMillis();
                System.out.println("** G"+graphNumber+": |V|="+verticesCount);

                if (!checkIfBipartite()) {
                    System.out.print("\tNot a bipartite graph");
                    long endTime = System.currentTimeMillis();

                    long duration = endTime - startTime;
                    System.out.print(" ("+ duration+" ms)\n");


                }
                else{
                    // Create the boolean adjacency matrix
                    boolean[][] booleanMatrix = createAdjacencyMatrix();

                    System.out.print("\tMatches: "+maxMatches(booleanMatrix)+" Pairs ");
                    long endTime = System.currentTimeMillis();

                    long duration = endTime - startTime;
                    System.out.println("("+ duration+" ms)");

                }
                graphNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading the file.");
        }
        System.out.println("Asg 8 by Shree Bhavya Kandula");
    }
    /**
     * Checks if the graph is bipartite by dividing it into two unequal sets and coloring the vertices.
     *
     * @return True if the graph is bipartite, false otherwise.
     */
    public static boolean checkIfBipartite() {
        int[] vertexColors = new int[verticesCount];
        Arrays.fill(vertexColors, -1);

        for (int i = 0; i < verticesCount; ++i) {
            if (vertexColors[i] == -1 && colorGraph(i, vertexColors, 0)) {
                return false; // Graph is not bipartite
            }
        }
        return true;
    }

    /**
     * A recursive function DFS that returns true when matching for vertex u is possible
     * @param booleanMatrix The bipartite graph represented as a boolean adjacency matrix.
     * @param vertexA       The current vertex
     * @param visited    An array representing the visited vertices.
     * @param verticesTrack  An array tracking the vertices matched.
     * @return True if a matching for vertex u is possible and false otherwise.
     */
    static boolean matchingPairs( int vertexA, boolean[] visited, int[] verticesTrack, boolean[][] booleanMatrix) {
        for (int vertexB = 0; vertexB < setB.size(); vertexB++) {
            if (!visited[vertexB] && booleanMatrix[vertexA][vertexB] ) {
                visited[vertexB] = true;                 // Mark vertexB as visited
                if (verticesTrack[vertexB] < 0 || matchingPairs( verticesTrack[vertexB], visited, verticesTrack,booleanMatrix)) {
                    verticesTrack[vertexB] = vertexA;
                    return true;
                }
            }
        }
        return false;
    }

}

