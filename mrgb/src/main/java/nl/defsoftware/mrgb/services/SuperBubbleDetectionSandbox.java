package nl.defsoftware.mrgb.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class SuperBubbleDetectionSandbox {

 // Driver method
    public static void main(String args[])
    {
        // Create a graph given in the above diagram
        SuperBubbleDetectionSandbox g = new SuperBubbleDetectionSandbox(16);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(3, 5);
        g.addEdge(3, 11);
        g.addEdge(4, 8);
        g.addEdge(5, 6);
        g.addEdge(5, 9);
        g.addEdge(6, 7);
        g.addEdge(6, 10);
        g.addEdge(7, 8);
        g.addEdge(8, 13);
        g.addEdge(8, 14);
        g.addEdge(9, 10);
        g.addEdge(10, 7);
        g.addEdge(11, 12);
        g.addEdge(12, 8);
        g.addEdge(13, 14);
        g.addEdge(13, 15);
        g.addEdge(15, 14);
 
        System.out.println("SuperBubbleDetectionHelper");
        g.topologicalSort();
    }
    
    private int V; // No. of vertices
    private LinkedList<Integer> adj[]; // Adjacency List

    public SuperBubbleDetectionSandbox(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i)
            adj[i] = new LinkedList();
    }

    // A recursive function used by topologicalSort
    void topologicalSortUtil(int v, boolean visited[], Stack stack) {
        // Mark the current node as visited.
        visited[v] = true;
        Integer i;

        // Recur for all the vertices adjacent to this
        // vertex
        Iterator<Integer> it = adj[v].iterator();
        while (it.hasNext()) {
            i = it.next();
            if (!visited[i])
                topologicalSortUtil(i, visited, stack);
        }

        // Push current vertex to stack which stores result
        stack.push(new Integer(v));
    }

    // The function to do Topological Sort. It uses
    // recursive topologicalSortUtil()
    void topologicalSort() {
        Stack stack = new Stack();

        // Mark all the vertices as not visited
        boolean visited[] = new boolean[V];
        for (int i = 0; i < V; i++)
            visited[i] = false;

        // Call the recursive helper function to store
        // Topological Sort starting from all vertices
        // one by one
        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                topologicalSortUtil(i, visited, stack);

        // Print contents of stack
        int i = 1;
        while (stack.empty() == false) {
            System.out.println(i + ": " + stack.pop() + " ");
            i++;
        }
    }
    
 // Function to add an edge into the graph
    void addEdge(int v, int w) { 
        adj[v].add(w); 
    }
}
