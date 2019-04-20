package mobile.robot.MobileRobotDispersion.services;

import mobile.robot.MobileRobotDispersion.logger.FileLogger;
import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.TreeMap;

@Service
@ApplicationScope
public class HelpingAsyncDispersionService extends BaseDispersionService {

    public HelpingAsyncDispersionService() {
        this.logger = new FileLogger();
    }

    public void init(Graph graph) {
        round = START_ROUND;
        int maxRounds = 4 * graph.getNumOfEdges() - 2 * (graph.getNumOfNodes() - 1);
        super.init(graph);
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    robot.setRound(maxRounds);
                    robot.setSpeed(getRandomSpeed());
                    robot.setCurrentPosition(Robot.MAX_SPEED);
                    robot.init(this.computer, this.computer);
                });

        logger.log("Helping-async init, nodes: " + graph.getNumOfNodes() + ", edges: " + graph.getNumOfEdges() + ", delta: " + graph.getDelta() +
                ", robots: " + graph.getRobots().size() + ", max rounds: " + maxRounds);
    }

    public TreeMap<Integer, Robot> helpingAsyncStep() {
        if(!graph.isTerminated()) {
            moveRobots();
            runHelpingAsync();
            clearNodes();
            logStep();
            checkRobots();
        }

        round++;
        return graph.getRobots();
    }

    public TreeMap<Integer, Robot> helpingAsync() {
        TreeMap<Integer, Robot> robots = null;

        while (!graph.isTerminated()) {
            robots = helpingAsyncStep();
        }

        return robots;
    }

    private void runHelpingAsync() {
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    if (robot.arrived()) {
                        int port = round == START_ROUND ? 0 : computer.findPort(robot);
                        robot.helpingAsync(port);
                    }
                });
    }

}
