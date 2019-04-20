package mobile.robot.MobileRobotDispersion.services;

import mobile.robot.MobileRobotDispersion.logger.FileLogger;
import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.TreeMap;

@Service
@ApplicationScope
public class IndependentAsyncDispersionService extends BaseDispersionService{

    public IndependentAsyncDispersionService() {
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
                    robot.initIndependentAsync(this.computer, this.computer);
                });

        logger.log("Independent-async init, nodes: " + graph.getNumOfNodes() + ", edges: " + graph.getNumOfEdges() + ", delta: " + graph.getDelta() +
                ", robots: " + graph.getRobots().size() + ", max rounds: " + maxRounds);
    }

    public TreeMap<Integer, Robot> independentAsyncStep() {
        if(!graph.isTerminated()) {
            moveRobots();
            runIndependentAsync();
            clearNodes();
            logStep();
            checkRobots();
        }

        round++;
        return graph.getRobots();
    }

    public TreeMap<Integer, Robot> independentAsnyc() {
        TreeMap<Integer, Robot> robots = null;

        while (!graph.isTerminated()) {
            robots = independentAsyncStep();
        }

        return robots;
    }

    private void runIndependentAsync() {
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    if (robot.arrived()) {
                        int port = round == START_ROUND ? 0 : computer.findPort(robot);
                        robot.independentAsync(port);
                    }
                });
    }

}
