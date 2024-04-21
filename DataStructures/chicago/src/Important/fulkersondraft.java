package Important;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class fulkersondraft {

     static ArrayList<Integer> setA;

     static ArrayList<Integer> setB;


    private static int verticesCount; // Number of vertices
    private static ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();

    // Color the vertices using DFS and return the sets
    static boolean colorGraph(int node, int[] color, int c) {
        color[node] = c;

        if (c == 0) {
            setA.add(node);
        } else {
            setB.add(node);
        }

        for (int neighbor : adjList.get(node)) {
            if (color[neighbor] == -1) {
                if (!colorGraph(neighbor, color, 1 - c)) {
                    return false;
                }
            } else if (color[neighbor] == c) {
                return false;
            }
        }

        return true;
    }

    // Divide the graph into two unequal sets, color them, and return the sets
    public static boolean divideAndColor() {
        int[] color = new int[verticesCount];
        Arrays.fill(color, -1);



        for (int i = 0; i < verticesCount; ++i) {

            if (color[i] == -1 && ! colorGraph(i, color, 0)) {
                return false; // Graph is not bipartite
            }

        }

        return true;
    }

    private static boolean[][] createAdjacencyMatrix() {
        int row = setA.size();
        int column = setB.size();
        boolean[][] bpGraph = new boolean[row][column];

        // Fill the matrix based on edges between vertices in setA and setB

        // Fill the boolean matrix based on the adjacency list
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                int vertexA = setA.get(i);
                int vertexB = setB.get(j);

                // Check if there is an edge between vertexA and vertexB
                if (hasEdge(adjList, vertexA, vertexB) || hasEdge(adjList, vertexB, vertexA)) {
                    bpGraph[i][j] = true;
                }
            }
        }




        return bpGraph;
    }
    private static boolean hasEdge(ArrayList<ArrayList<Integer>> adjList, int u, int v) {
        return adjList.get(u).contains(v);
    }

    static final int N = 5;

    // A DFS based recursive function that
    // returns true if a matching for
    // vertex u is possible
    static boolean bpm(boolean bpGraph[][], int u,
                       boolean seen[], int matchR[])
    {
        // Try every job one by one
        for (int v = 0; v < setB.size(); v++)
        {
            // If applicant u is interested
            // in job v and v is not visited
            if (bpGraph[u][v] && !seen[v])
            {

                // Mark v as visited
                seen[v] = true;

                // If job 'v' is not assigned to
                // an applicant OR previously
                // assigned applicant for job v (which
                // is matchR[v]) has an alternate job available.
                // Since v is marked as visited in the
                // above line, matchR[v] in the following
                // recursive call will not get job 'v' again
                if (matchR[v] < 0 || bpm(bpGraph, matchR[v],
                        seen, matchR))
                {
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    // Returns maximum number
    // of matching from M to N
    static int maxBPM(boolean bpGraph[][])
    {
        // An array to keep track of the
        // applicants assigned to jobs.
        // The value of matchR[i] is the
        // applicant number assigned to job i,
        // the value -1 indicates nobody is assigned.
        int matchR[] = new int[setB.size()];

        // Initially all jobs are available
        for(int i = 0; i < setB.size(); ++i)
            matchR[i] = -1;

        // Count of jobs assigned to applicants
        int result = 0;
        for (int u = 0; u < setA.size(); u++)
        {
            // Mark all jobs as not seen
            // for next applicant.
            boolean seen[] =new boolean[setB.size()] ;
            for(int i = 0; i < setB.size(); ++i)
                seen[i] = false;

            // Find if the applicant 'u' can get a job
            if (bpm(bpGraph, u, seen, matchR))
                result++;
        }

        // Print the assignment of jobs to applicants
        printAssignment(matchR);

        return result;
    }

    // Print the assignment of jobs to applicants
    static void printAssignment(int matchR[]) {

        for (int j = 0; j < setB.size(); j++) {
            if (matchR[j] != -1) {
                System.out.println("\t(" + setB.get(j) + "," + setA.get(matchR[j])+")");
            }
        }
    }




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

            int graphNumber = 1; // To keep track of graph numbers

            System.out.println("Maximum number of matches in bipartite graphs in "+fileName);

            int totalGraphs=0;
            // Create a Matcher object
            Matcher matcher = Pattern.compile("\\b(\\d+)\\b").matcher(reader.readLine());
            if (matcher.find()) {
                 totalGraphs = Integer.parseInt(matcher.group(1));
            }

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
                            int v = Integer.parseInt(edgeMatcher.group(2));


                            adjList.get(u).add(v);
                            adjList.get(v).add(u);
                        }


                    }
                }
                long startTime = System.currentTimeMillis();
                System.out.println("** G"+graphNumber+": |V|="+verticesCount);

                if (!divideAndColor()) {
                    System.out.print("\tNot a bipartite graph");
                    long endTime = System.currentTimeMillis();

                    long duration = endTime - startTime;
                    System.out.print(" ("+ duration+" ms)\n");


                }
                else{
                    // Create the adjacency matrix
                    boolean[][] bpGraph = createAdjacencyMatrix();

//                // Print the adjacency matrix
//                System.out.println("Adjacency Matrix:");
//                for (boolean[] row : bpGraph) {
//                    System.out.println(Arrays.toString(row));
//                }



                    System.out.print("\tMatches: "+maxBPM(bpGraph)+" Pairs ");
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
        System.out.println("Asg 7 by Shree Bhavya Kandula");
    }




}
