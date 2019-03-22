package mobile.robot.MobileRobotDispersion.services;

import mobile.robot.MobileRobotDispersion.logger.FileLogger;
import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Map;
import java.util.TreeMap;

@Service
@SessionScope
public class HelpingSyncDispersionService extends BaseDispersionService {

    public HelpingSyncDispersionService() {
        this.logger = new FileLogger();
    }

    public void init(Graph graph) {
        round = START_ROUND;
        super.init(graph);
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    robot.setRound(4 * graph.getNumOfEdges() - 2 * (graph.getNumOfNodes() - 1));
                    robot.init(this, this);
                });

        logger.log("Init, nodes: " + graph.getNumOfNodes() + ", edges: " + graph.getNumOfEdges() + ", delta: " + graph.getDelta() + ", robots: " + graph.getRobots().size());
    }

    public TreeMap<Integer, Robot> helpingSyncStep() {
        if(!graph.isTerminated()) {
            moveRobots();
            runHelpingSync();
            clearNodes();
            logStep();
            checkRobots();
        }

        round++;
        return graph.getRobots();
    }

    public TreeMap<Integer, Robot> helpingSync() {
        TreeMap<Integer, Robot> robots = null;

        while (!graph.isTerminated()) {
            robots = helpingSyncStep();
        }

        return robots;
    }

    private void runHelpingSync() {
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    int port = round == START_ROUND ? 0 : findPort(robot);
                    //robot.setParentNode(robot.getMovingTo());
                    robot.helpingSync(port);
                });
    }

    private int findPort(Robot robot) {
        Map<Integer, String> ports = graph.getNodes().get(robot.getMovingTo())
                .getPorts();

        for (int port : ports.keySet()) {
            if (ports.get(port).equals(robot.getParentNode())) {
                return port;
            }
        }

        return Graph.NO_PORT;
    }

    private void checkRobots() {
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

    private void logStep() {
        StringBuilder message = new StringBuilder();
        message.append("Round: ");
        message.append(round);
        message.append('\n');

        this.graph.getRobots()
                .forEach((key, robot) -> message.append("\t Robot " + robot.getId() + ", state: " +
                        robot.convertStateToString() + ", parent node: " + robot.getParentNode() +
                        ", moving to: " + robot.getMovingTo() + ", distance: " + robot.getDistance() + "\n"));
        logger.log(message.toString());
    }

    public void saveLog() {
        logger.saveAndClear();
    }

}
