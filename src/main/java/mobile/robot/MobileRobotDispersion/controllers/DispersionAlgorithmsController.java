package mobile.robot.MobileRobotDispersion.controllers;

import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import mobile.robot.MobileRobotDispersion.services.BaseDispersionService;
import mobile.robot.MobileRobotDispersion.services.HelpingSyncDispersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;

@RestController
@RequestMapping("/api/dispersion")
public class DispersionAlgorithmsController extends BaseController {

    public static final boolean OK = true;
    private HelpingSyncDispersionService helpingSyncDispersionService;

    @Autowired
    public DispersionAlgorithmsController(HelpingSyncDispersionService helpingSyncDispersionService) {
        this.helpingSyncDispersionService = helpingSyncDispersionService;
    }

    @GetMapping("/supported")
    public ResponseEntity<String> supportedAlgorithms() {
        return ResponseEntity.ok("HELPING-SYNC, HELPING_ASYNC, INDEPENDENT-ASYNC");
    }

    @PostMapping("/init")
    public ResponseEntity<Boolean> init(@RequestBody Graph graph) {
        helpingSyncDispersionService.init(graph);

        return ResponseEntity.ok(OK);
    }

    @PostMapping("/helpingSyncStep")
    public ResponseEntity<TreeMap<Integer, Robot>> helpingSyncStep() {
        try {
            return ResponseEntity.ok(helpingSyncDispersionService.helpingSyncStep());
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @PostMapping("/helpingSync")
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

}
