package mobile.robot.MobileRobotDispersion.services;

import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Map;

@Service
@SessionScope
public class HelpingSyncDispersionService extends BaseDispersionService {

    public void init(Graph graph) {
        super.init(graph);
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    robot.setRound(4 * graph.getNumOfEdges() - 2 * (graph.getNumOfNodes() - 1));
                    robot.init(this, this);
                });
    }

    public Graph helpingSyncStep() {
        moveRobots();
        System.out.println("--1");
        runHelpingSync();
        System.out.println("--2");
        clearNodes();
        System.out.println("--3");

        round++;
        return graph;
    }

    private void runHelpingSync() {
        this.graph.getRobots()
                .forEach((key, robot) -> {
                    int port = round == 0 ? 0 : findPort(robot);
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

}
