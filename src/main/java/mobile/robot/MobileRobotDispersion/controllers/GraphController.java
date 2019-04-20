package mobile.robot.MobileRobotDispersion.controllers;

import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin
public class GraphController extends BaseController {

    private GraphService graphService;

    @Autowired
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PostMapping("/build")
    public ResponseEntity<Graph> build(@RequestBody String encodedGraph) {
        try {
            return ResponseEntity.ok(graphService.buildGraph(encodedGraph));
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @GetMapping("/format")
    public ResponseEntity<String> format() {
        return ResponseEntity.ok(Graph.ENCODED_GRAPH_FORMAT);
    }
}
