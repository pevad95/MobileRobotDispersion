package mobile.robot.MobileRobotDispersion.controllers;

import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import mobile.robot.MobileRobotDispersion.services.IndependentAsyncDispersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;

@RestController
@RequestMapping("/api/independentAsync")
public class IndependentAsyncDispersionAlgorithmController extends BaseController {

    private IndependentAsyncDispersionService independentAsyncDispersionService;

    @Autowired
    public IndependentAsyncDispersionAlgorithmController(IndependentAsyncDispersionService independentAsyncDispersionService) {
        this.independentAsyncDispersionService = independentAsyncDispersionService;
    }

    @PostMapping("/init")
    public ResponseEntity<Boolean> init(@RequestBody Graph graph) {
        independentAsyncDispersionService.init(graph);

        return ResponseEntity.ok(OK);
    }

    @PostMapping("/step")
    public ResponseEntity<TreeMap<Integer, Robot>> independentAsyncStep() {
        try {
            return ResponseEntity.ok(independentAsyncDispersionService.independentAsyncStep());
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @PostMapping("/run")
    public ResponseEntity<TreeMap<Integer, Robot>> independentAsync() {
        try {
            return ResponseEntity.ok(independentAsyncDispersionService.independentAsnyc());
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @PostMapping("/log")
    public ResponseEntity<Boolean> saveLog() {
        independentAsyncDispersionService.saveLog();
        return ResponseEntity.ok(OK);
    }

    @GetMapping("/graph")
    public ResponseEntity<Graph> getGraph() {
        return ResponseEntity.ok(independentAsyncDispersionService.getGraph());
    }

    @GetMapping("/terminated")
    public ResponseEntity<Boolean> isTerminated() { return ResponseEntity.ok(independentAsyncDispersionService.isTerminated()); }
}
