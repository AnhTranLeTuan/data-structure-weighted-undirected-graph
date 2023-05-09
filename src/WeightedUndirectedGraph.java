import java.util.*;

public class WeightedUndirectedGraph {
    private class Node{
        private String item;

        public Node(String item) {
            this.item = item;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(item, node.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item);
        }

        @Override
        public String toString(){
            return item;
        }
    }

    private class Edge{
        private Node fromNode;
        private Node toNode;
        private int weight;

        public Edge(Node fromNode, Node toNode, int weight) {
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.weight = weight;
        }

        public Edge(Node fromNode, Node toNode) {
            this.fromNode = fromNode;
            this.toNode = toNode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return fromNode.equals(edge.fromNode) && toNode.equals(edge.toNode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fromNode, toNode);
        }

        @Override
        public String toString(){
            return fromNode + "->" + toNode;
        }
    }

    private Map<String, Node> nodeMap = new HashMap<>();
    private  Map<Node, List<Edge>> adjacencyMap = new HashMap<>();

    public void addNode(String item){
        if (item == null)
            return;

        Node node = new Node(item);

        nodeMap.putIfAbsent(item, node);

        adjacencyMap.putIfAbsent(node, new LinkedList<>());
    }

    public void addEdge(String fromItem, String toItem, int weight){
        if (fromItem == null || toItem == null || fromItem.equals(toItem))
            return;

        Node fromNode = nodeMap.get(fromItem);
        Node toNode = nodeMap.get(toItem);

        if (fromNode == null || toNode == null)
            throw new IllegalArgumentException();

        List<Edge> fromBucket = adjacencyMap.get(fromNode);
        List<Edge> toBucket = adjacencyMap.get(toNode);
        Edge fromToEdge = new Edge(fromNode, toNode, weight);
        Edge toFromEdge = new Edge(toNode, fromNode, weight);

        if (!fromBucket.contains(fromToEdge)) {
            fromBucket.add(fromToEdge);
            toBucket.add(toFromEdge);
        }
    }

    public void removeNode(String item){
        if (item == null)
            return;

        if (!nodeMap.containsKey(item))
            return;

        Node node = new Node(item);

        nodeMap.remove(item);

        adjacencyMap.remove(node);

        for (var entry : adjacencyMap.entrySet())
            entry.getValue().remove(new Edge(entry.getKey(), node));
    }

    public void removeEdge(String fromItem, String toItem){
        if (fromItem == null || toItem == null || fromItem.equals(toItem))
            return;

        Node fromNode = nodeMap.get(fromItem);
        Node toNode = nodeMap.get(toItem);

        if (fromNode == null || toNode == null)
            throw new IllegalArgumentException();

        adjacencyMap.get(fromNode).remove(new Edge(fromNode, toNode));
        adjacencyMap.get(toNode).remove(new Edge(toNode, fromNode));
    }

    public boolean cycleDetection(){
        Set<Node> remainingSet = new HashSet<>();
        Set<Node> visitingSet = new HashSet<>();
        Set<Node> visitedSet = new HashSet<>();

        remainingSet.addAll(nodeMap.values());

        while (!remainingSet.isEmpty()){
            if (cycleDetection(null, remainingSet.iterator().next(), remainingSet, visitingSet, visitedSet))
                return true;
        }

        return false;
    }

    private class NodeWithWeight{
        Node node;
        int weight;

        public NodeWithWeight(Node node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    public static class Path{
        List<String> path = new ArrayList<>();
        int totalWeight;

        public Path(){}

        public Path(List<String> path, int totalWeight) {
            this.path = path;
            this.totalWeight = totalWeight;
        }

        @Override
        public String toString() {
            return "Path{" +
                    "path=" + path +
                    ", totalWeight=" + totalWeight +
                    '}';
        }
    }

    // Dijkstra's shortest path algorithm
    public Path getThePathWithSmallestWeightBetweenTwoItems(String fromItem, String toItem){
        if (fromItem == null || toItem == null)
            return null;

        if (fromItem.equals(toItem)){
            List<String> path = new ArrayList<>();
            path.add(fromItem);
            return new Path(path, 0);
        }

        Node fromNode = nodeMap.get(fromItem);
        Node toNode = nodeMap.get(toItem);

        if (fromNode == null || toNode == null)
            throw new IllegalArgumentException();

        Map<Node, Integer> weights = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<NodeWithWeight> nodeWithWeightQueue = new PriorityQueue<>(
                Comparator.comparingInt(n -> n.weight)
        );
        Set<Node> nodeSet = new HashSet<>();
        NodeWithWeight currentNodeWithWeight;
        Node currentNode;

        for(var node : adjacencyMap.keySet())
            weights.put(node, Integer.MAX_VALUE);

        weights.put(fromNode, 0);
        nodeWithWeightQueue.add(new NodeWithWeight(fromNode, 0));

        while (!nodeWithWeightQueue.isEmpty()){
            currentNodeWithWeight = nodeWithWeightQueue.remove();
            currentNode = currentNodeWithWeight.node;

            if (!nodeSet.add(currentNode))
                 continue;

            for (var edge : adjacencyMap.get(currentNode)){
                if (nodeSet.contains(edge.toNode))
                    continue;

                int currentSmallestWeight = weights.get(edge.toNode);
                int totalWeight = currentNodeWithWeight.weight + edge.weight;

                 if (totalWeight < currentSmallestWeight){
                     weights.put(edge.toNode, totalWeight);
                     previousNodes.put(edge.toNode, edge.fromNode);

                     nodeWithWeightQueue.add(new NodeWithWeight(edge.toNode, totalWeight));
                 }
            }
        }

        return createPathObject(fromItem, toItem, weights, previousNodes);
    }

    // Prim's algorithm for obtaining minimum spanning tree
    public WeightedUndirectedGraph getMinimumSpanningTree(){
        WeightedUndirectedGraph minimumSpanningTree = new WeightedUndirectedGraph();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(
                Comparator.comparingInt(e -> e.weight)
        );

        Node firstNode = nodeMap.values().iterator().next();
        minimumSpanningTree.addNode(firstNode.item);
        Edge minimumEdge;

        for (var edge : adjacencyMap.get(firstNode))
            edgeQueue.add(edge);

        while (minimumSpanningTree.nodeMap.size() < nodeMap.size()){
            minimumEdge = edgeQueue.remove();
            Node nextNode = minimumEdge.toNode;

            if (minimumSpanningTree.nodeMap.containsKey(nextNode.item))
                continue;

            minimumSpanningTree.addNode(nextNode.item);
            minimumSpanningTree.addEdge(minimumEdge.fromNode.item, nextNode.item, minimumEdge.weight);

            for (var edge : adjacencyMap.get(nextNode)){
                if (minimumSpanningTree.nodeMap.containsKey(edge.toNode))
                    continue;

                edgeQueue.add(edge);
            }
        }

        return minimumSpanningTree;
    }

    private boolean cycleDetection(Edge edgeForTwoWayDirection, Node currentNode, Set<Node> remainingSet,
                                   Set<Node> visitingSet, Set<Node> visitedSet){
        remainingSet.remove(currentNode);
        visitingSet.add(currentNode);

        for (var edge : adjacencyMap.get(currentNode)) {
            if (!(edgeForTwoWayDirection == null)){
                if (edgeForTwoWayDirection.fromNode.equals(edge.toNode))
                    continue;
            }

            if (visitedSet.contains(edge.toNode))
                continue;

            if (visitingSet.contains(edge.toNode))
                return true;

            if (cycleDetection(edge, edge.toNode, remainingSet, visitingSet, visitedSet))
                return true;
        }

        visitingSet.remove(currentNode);
        visitedSet.add(currentNode);

        return false;
    }

    private Path createPathObject(String fromItem, String toItem, Map<Node, Integer> weights, Map<Node, Node> previousNodes){
        Node toNode = nodeMap.get(toItem);

        ArrayDeque<String> stack = new ArrayDeque<>();
        Node currentNode = toNode;

        while (previousNodes.containsKey(currentNode)){
            stack.addLast(currentNode.item);
            currentNode = previousNodes.get(currentNode);
        }

        Path path = new Path();
        path.path.add(fromItem);

        while (!stack.isEmpty())
            path.path.add(stack.removeLast());

        path.totalWeight = weights.get(toNode);

        return path;
    }

    @Override
    public String toString() {
        return "WeightedUndirectedGraph{" +
                "nodeMap=" + nodeMap +
                ", adjacencyMap=" + adjacencyMap +
                '}';
    }
}
