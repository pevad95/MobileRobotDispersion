package mobile.robot.MobileRobotDispersion.model.robot;

import mobile.robot.MobileRobotDispersion.model.graph.Node;

import java.util.Map;
import java.util.TreeMap;

public class Computer implements Robot.Sensor, Robot.Engine {

    private Graph graph;

    public Computer(Graph graph) {
        this.graph = graph;
    }

    public int findPort(Robot robot) {
        Map<Integer, String> ports = graph.getNodes().get(robot.getMovingTo())
                .getPorts();

        for (int port : ports.keySet()) {
            if (ports.get(port).equals(robot.getParentNode())) {
                return port;
            }
        }

        return Graph.NO_PORT;
    }

    /// Sensor interface

    @Override
    public Robot settledRobotAtCurrentNode(int robotId) {
        Robot actualRobot = this.graph.getRobots().get(robotId);

        try {
            return graph.getRobots().get(graph.getNodes().get(actualRobot.getMovingTo()).getSettledRobotId());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int numOfRobots() {
        return graph.getNodes().size();
    }

    @Override
    public Robot mutexWinner(int robotId) {
        TreeMap<Integer, Robot> robots = graph.getRobots();

        return robots.get(graph.getNodes().get(robots.get(robotId).getMovingTo()).mutex());
    }

    @Override
    public int degreeOfCurrentNode(int robotId) {
        return graph.getNodes().get(graph.getRobots().get(robotId).getMovingTo()).degree();
    }

    @Override
    public void dock(Robot robot) {
        this.graph.getNodes().get(robot.getMovingTo()).setSettledRobotId(robot.getId());
    }

    /// Engine interface

    @Override
    public void move(int robotId, int port) {
        Robot robot = graph.getRobots().get(robotId);
        robot.resetPosition();
        Node currentNode = graph.getNodes().get(robot.getMovingTo());
        robot.setParentNode(robot.getMovingTo());
        robot.setMovingTo(currentNode.getPorts().get(port));
    }
}
