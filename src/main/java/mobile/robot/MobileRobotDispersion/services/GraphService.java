package mobile.robot.MobileRobotDispersion.services;

import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class GraphService {

    public Graph buildGraph(String encodedGraph) {
        return Graph.build(encodedGraph);
    }
}
