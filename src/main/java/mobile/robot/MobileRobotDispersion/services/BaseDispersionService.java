package mobile.robot.MobileRobotDispersion.services;

import lombok.Getter;
import mobile.robot.MobileRobotDispersion.logger.FileLogger;
import mobile.robot.MobileRobotDispersion.model.graph.Node;
import mobile.robot.MobileRobotDispersion.model.robot.Computer;
import mobile.robot.MobileRobotDispersion.model.robot.Graph;

public abstract class BaseDispersionService {

    public static final boolean ALGORITHM_TERMINATED = true;
    public static final int START_ROUND = 1;

    @Getter
    protected Graph graph;

    @Getter
    protected int round;

    @Getter
    protected Computer computer;

    protected FileLogger logger;

    public void init(Graph graph) {
        this.graph = graph;
        this.computer = new Computer(graph);
    }

    protected void moveRobots() {
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    if (robot.isSettled()) {
                        return;
                    }

                    robot.move();
                    if (robot.arrived()) {
                        Node node = graph.getNodes().get(robot.getMovingTo());
                        if (!node.getArrivingRobots().contains(robot.getId())) {
                            node.getArrivingRobots().add(robot.getId());
                        }
                        //robot.resetPosition();
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

    protected void checkRobots() {
        boolean allRobotsSettled = true;
        for (int id : graph.getRobots().keySet()) {
            allRobotsSettled = allRobotsSettled && graph.getRobots().get(id).isSettled();

            if (!allRobotsSettled) {
                return;
            }
        }

        graph.setTerminated(ALGORITHM_TERMINATED);
        logger.log("TERMINATED, rounds: " + round);
        logger.saveAndClear();
    }

    protected void logStep() {
        StringBuilder message = new StringBuilder();
        message.append("Round: ");
        message.append(round);
        message.append('\n');

        this.graph.getRobots()
                .forEach((key, robot) -> message.append("\t Robot " + robot.getId() + ", state: " +
                        robot.convertStateToString() + ", parent node: " + robot.getParentNode() +
                        ", moving to: " + robot.getMovingTo() + ", distance: " + robot.getDistance() + ", speed: " + robot.getSpeed() +  "\n"));
        logger.log(message.toString());
    }

    protected double getRandomSpeed() {
        return Math.random() * 0.6 + 0.4;
    }

    public void saveLog() {
        logger.saveAndClear();
    }

    public boolean isTerminated() {
        return graph.isTerminated();
    }
}
