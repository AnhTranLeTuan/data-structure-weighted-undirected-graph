# Data Structure: Weighted Undirected Graph

## Description
Experiment with concepts of weighted undirected graph, or the weighted two-way connection graph in specific. In this project, we can see how:
* Create the private Node class and override its equals() and hashcode() to be able to compare two nodes in the future
* Create the Edge class that contains two end nodes, and the weight of the connection. This class also needs to have equals() and hashcode() override
* Create attributes of the weighted undirected graph
* Design this graph based on the adjacency list instead of the adjacency array
* Add a node into the graph
* Add an edge or the connection between two nodes to the graph (two-way connection which mean when we add an edge from node A tp node B, we also need the connection from node B to node A)
* Remove a node from the graph
* Remove an edge from the graph (remove both A -> B and B -> A) 
* Detect the cycle in the undirected graph
* Get the path (a Path class will need to be created) with smallest weight between two nodes (Dijkstra's shortest path algorithm)
* Get the minimum spanning tree (obtain the tree from the graph, or we can see it as retrieving the graph with the number of edge equals to number of node minus one) (Prim's algorithm)
