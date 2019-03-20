package mobile.robot.MobileRobotDispersion.model.robot;

import lombok.Data;
import mobile.robot.MobileRobotDispersion.model.graph.Node;

import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

@Data
public class Graph {

    public static final String ENCODED_GRAPH_FORMAT = "<NodeId (string)>:<NeighbourNodeId [,...]>[:<RoborId (int) [,...]>]\nExample\n" +
                                                        "A:B,C,D:0\n" +
                                                        "B:D,A:1,2\n" +
                                                        "C:A,D\n" +
                                                        "D:B,A,C";

    public static final int NO_PORT = -1;


    private int delta;
    private int numOfEdges;
    private TreeMap<String, Node> nodes;
    private TreeMap<Integer, Robot> robots;

    public static Graph build(String encodedGraph) {
        Scanner sc = new Scanner(encodedGraph);
        Graph graph = new Graph();
        int delta = 0;

        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(":");

            Node currentNode = new Node(line[0], line[1].split(","));
            graph.addNode(currentNode);

            int actualNumOfEdges = currentNode.getPorts().size();

            graph.numOfEdges += actualNumOfEdges;

            if (actualNumOfEdges > delta) {
                delta = actualNumOfEdges;
            }

            if (line.length == 3) {
                Arrays.asList(line[2].split(","))
                        .forEach(id -> {
                            int currentId = Integer.parseInt(id);
                            currentNode.getArrivingRobots().add(currentId);
                            graph.robots.put(currentId, new Robot(currentId, currentNode.getId()));
                        });
            }
        }

        graph.delta = delta;
        graph.numOfEdges /= 2;
        return graph;
    }

    public Graph() {
        this.nodes = new TreeMap<>();
        this.robots = new TreeMap<>();
    }

    public void addNode(Node node) {
        if (node == null) {
            return;
        }

        nodes.put(node.getId(), node);
    }

    public int getNumOfNodes() {
        return nodes.size();
    }
}
