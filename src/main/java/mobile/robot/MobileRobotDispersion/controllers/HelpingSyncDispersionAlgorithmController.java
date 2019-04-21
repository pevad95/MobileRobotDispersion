package mobile.robot.MobileRobotDispersion.controllers;

import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import mobile.robot.MobileRobotDispersion.services.HelpingSyncDispersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;

@RestController
@RequestMapping("/api/helpingSync")
@CrossOrigin
public class HelpingSyncDispersionAlgorithmController extends BaseController {

    private HelpingSyncDispersionService helpingSyncDispersionService;

    @Autowired
    public HelpingSyncDispersionAlgorithmController(HelpingSyncDispersionService helpingSyncDispersionService) {
        this.helpingSyncDispersionService = helpingSyncDispersionService;
    }

    @PostMapping("/init")
    public ResponseEntity<Boolean> init(@RequestBody Graph graph) {
        helpingSyncDispersionService.init(graph);

        return ResponseEntity.ok(OK);
    }

    @PostMapping("/step")
    public ResponseEntity<TreeMap<Integer, Robot>> helpingSyncStep() {
        try {
            return ResponseEntity.ok(helpingSyncDispersionService.helpingSyncStep());
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @PostMapping("/run")
    public ResponseEntity<TreeMap<Integer, Robot>> helpingSync() {
        try {
            return ResponseEntity.ok(helpingSyncDispersionService.helpingSync());
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @PostMapping("/log")
    public ResponseEntity<Boolean> saveLog() {
        helpingSyncDispersionService.saveLog();
        return ResponseEntity.ok(OK);
    }

    @GetMapping("/graph")
    public ResponseEntity<Graph> getGraph() {
        return ResponseEntity.ok(helpingSyncDispersionService.getGraph());
    }

    @GetMapping("/terminated")
    public ResponseEntity<Boolean> isTerminated() { return ResponseEntity.ok(helpingSyncDispersionService.isTerminated()); }

}
