import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        WeightedUndirectedGraph weightedUndirectedGraph = new WeightedUndirectedGraph();

        weightedUndirectedGraph.addNode("C");
        weightedUndirectedGraph.addNode("A");
        weightedUndirectedGraph.addNode("B");
        weightedUndirectedGraph.addNode("D");
        weightedUndirectedGraph.addNode("E");

        weightedUndirectedGraph.addEdge("A", "B", 3);
        weightedUndirectedGraph.addEdge("A", "C", 4);
        weightedUndirectedGraph.addEdge("A", "D", 2);

        weightedUndirectedGraph.addEdge("B", "A", 3);
        weightedUndirectedGraph.addEdge("B", "D", 6);
        weightedUndirectedGraph.addEdge("B", "E", 1);

        weightedUndirectedGraph.addEdge("C", "A", 4);
        weightedUndirectedGraph.addEdge("C", "D", 1);

        weightedUndirectedGraph.addEdge("D", "A", 2);
        weightedUndirectedGraph.addEdge("D", "B", 6);
        weightedUndirectedGraph.addEdge("D", "C", 1);
        weightedUndirectedGraph.addEdge("D", "E", 5);

        weightedUndirectedGraph.addEdge("E", "B", 1);
        weightedUndirectedGraph.addEdge("E", "D", 5);







        //System.out.println(weightedUndirectedGraph.cycleDetection());

        WeightedUndirectedGraph.Path path;

        path = weightedUndirectedGraph.getThePathWithSmallestWeightBetweenTwoItems("A", "C");

        System.out.println(path);

        WeightedUndirectedGraph graph = weightedUndirectedGraph.getMinimumSpanningTree();

        System.out.println(weightedUndirectedGraph);

        System.out.println(graph);




    }
}