package mobile.robot.MobileRobotDispersion.services;

import lombok.Getter;
import mobile.robot.MobileRobotDispersion.model.graph.Node;
import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;

import java.util.TreeMap;

public abstract class BaseDispersionService implements Robot.Sensor, Robot.Engine {

    @Getter
    protected Graph graph;

    @Getter
    protected int round;

    public void init(Graph graph) {
        this.graph = graph;
    }

    protected void moveRobots() {
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    robot.move();
                    if (robot.arrived()) {
                        Node node = graph.getNodes().get(robot.getMovingTo());
                        if (!node.getArrivingRobots().contains(robot.getId())) {
                            node.getArrivingRobots().add(robot.getId());
                        }
                        robot.resetPosition();
                    }
                });

    }

    protected void clearNodes() {
        this.graph.getNodes()
                .forEach((key, node) -> {
                    node.resetMutex();
                    node.getArrivingRobots().clear();
                });
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

    /// Engine interface

    @Override
    public void move(int robotId, int port) {
        Robot robot = graph.getRobots().get(robotId);
        Node currentNode = graph.getNodes().get(robot.getMovingTo());
        robot.setParentNode(robot.getMovingTo());
        robot.setMovingTo(currentNode.getPorts().get(port));
    }
}