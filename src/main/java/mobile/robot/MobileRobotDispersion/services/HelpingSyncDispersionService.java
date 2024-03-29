package mobile.robot.MobileRobotDispersion.services;

import mobile.robot.MobileRobotDispersion.logger.FileLogger;
import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

import java.util.TreeMap;

@Service
@ApplicationScope
public class HelpingSyncDispersionService extends BaseDispersionService {

    public HelpingSyncDispersionService() {
        this.logger = new FileLogger();
    }

    public void init(Graph graph) {
        round = START_ROUND;
        int maxRounds = 4 * graph.getNumOfEdges() - 2 * (graph.getNumOfNodes() - 1);
        super.init(graph);

        this.graph.getRobots()
                .forEach((key, robot) -> {
                    robot.setRound(maxRounds);
                    robot.init(this.computer, this.computer);
                });

        logger.log("Helping-sync init, nodes: " + graph.getNumOfNodes() + ", edges: " + graph.getNumOfEdges() + ", delta: " + graph.getDelta() +
                ", robots: " + graph.getRobots().size() + ", max rounds: " + maxRounds);
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
                    int port = round == START_ROUND ? 0 : computer.findPort(robot);
                    robot.helpingSync(port);
                });
    }

}
