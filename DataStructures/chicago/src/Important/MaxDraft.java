
package Important;

        import java.util.ArrayList;
        import java.util.Arrays;

class MaxDraft {
    private int V; // Number of vertices
    private ArrayList<ArrayList<Integer>> adjList;

    MaxDraft(int v) {
        V = v;
        adjList = new ArrayList<>(V);
        for (int i = 0; i < V; ++i)
            adjList.add(new ArrayList<>());
    }

    // Add an edge to the graph
    void addEdge(int u, int v) {
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }

    // Color the vertices using DFS and return the sets
    boolean colorGraph(int node, int[] color, int c, ArrayList<Integer> setA, ArrayList<Integer> setB) {
        color[node] = c;

        if (c == 0) {
            setA.add(node);
        } else {
            setB.add(node);
        }

        for (int neighbor : adjList.get(node)) {
            if (color[neighbor] == -1) {
                if (!colorGraph(neighbor, color, 1 - c, setA, setB)) {
                    return false;
                }
            } else if (color[neighbor] == c) {
                return false;
            }
        }

        return true;
    }

    // Divide the graph into two unequal sets, color them, and return the sets
    boolean divideAndColor(ArrayList<Integer> setA, ArrayList<Integer> setB) {
        int[] color = new int[V];
        Arrays.fill(color, -1);

        for (int i = 0; i < V; ++i) {
            if (color[i] == -1 && !colorGraph(i, color, 0, setA, setB)) {
                return false; // Graph is not bipartite
            }
        }

        return true;
    }

    public static void main(String[] args) {
        MaxDraft graph = new MaxDraft(10);

//        g.addEdge(0, 1);
//        g.addEdge(1, 2);
//        g.addEdge(2, 3);
//        g.addEdge(3, 4);
//        g.addEdge(4, 5);
//        g.addEdge(5, 0);
//
//        graph.addEdge(0, 2);
//        graph.addEdge(0, 4);
//        graph.addEdge(0, 8);
//        graph.addEdge(0, 9);
//        graph.addEdge(1, 2);
//        graph.addEdge(1, 4);
//        graph.addEdge(1, 8);
//        graph.addEdge(1, 9);
//        graph.addEdge(2, 3);
//        graph.addEdge(2, 5);
//        graph.addEdge(2, 6);
//        graph.addEdge(2, 7);
//        graph.addEdge(3, 4);
//        graph.addEdge(3, 9);
//        graph.addEdge(4, 5);
//        graph.addEdge(4, 6);
//        graph.addEdge(4, 7);
//        graph.addEdge(5, 8);
//        graph.addEdge(5, 9);
//        graph.addEdge(6, 8);
//        graph.addEdge(6, 9);
//        graph.addEdge(7, 8);
//        graph.addEdge(7, 9);
        graph.addEdge(0, 2);
        graph.addEdge(0, 4);
        graph.addEdge(0, 5);
        graph.addEdge(0, 9);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(1, 5);
        graph.addEdge(2, 6);
        graph.addEdge(2, 7);
        graph.addEdge(2, 8);
        graph.addEdge(3, 6);
        graph.addEdge(3, 7);
        graph.addEdge(3, 8);
        graph.addEdge(4, 6);
        graph.addEdge(4, 7);
        graph.addEdge(4, 8);
        graph.addEdge(5, 6);
        graph.addEdge(5, 7);
        graph.addEdge(5, 8);
        graph.addEdge(7, 9);
        graph.addEdge(8, 9);

        ArrayList<Integer> setA = new ArrayList<>();
        ArrayList<Integer> setB = new ArrayList<>();

        if (!graph.divideAndColor(setA, setB)) {
            System.out.println("Not a bipartite graph");
        } else {
            System.out.println("Set A: " + setA);
            System.out.println("Set B: " + setB);
        }

    }
}
