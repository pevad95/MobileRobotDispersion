package mobile.robot.MobileRobotDispersion.controllers;

import mobile.robot.MobileRobotDispersion.model.robot.Graph;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;
import mobile.robot.MobileRobotDispersion.services.HelpingAsyncDispersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;

@RestController
@RequestMapping("/api/helpingAsync")
@CrossOrigin
public class HelpingAsyncDispersionAlgorithmController extends BaseController {

    private HelpingAsyncDispersionService helpingAsyncDispersionService;

    @Autowired
    public HelpingAsyncDispersionAlgorithmController(HelpingAsyncDispersionService helpingAsyncDispersionService) {
        this.helpingAsyncDispersionService = helpingAsyncDispersionService;
    }

    @PostMapping("/init")
    public ResponseEntity<Boolean> init(@RequestBody Graph graph) {
        helpingAsyncDispersionService.init(graph);

        return ResponseEntity.ok(OK);
    }

    @PostMapping("/step")
    public ResponseEntity<TreeMap<Integer, Robot>> helpingAsyncStep() {
        try {
            return ResponseEntity.ok(helpingAsyncDispersionService.helpingAsyncStep());
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @PostMapping("/run")
    public ResponseEntity<TreeMap<Integer, Robot>> helpingAsync() {
        try {
            return ResponseEntity.ok(helpingAsyncDispersionService.helpingAsync());
        } catch (Exception e) {
            e.printStackTrace();
            return createBadRequest(e);
        }
    }

    @PostMapping("/log")
    public ResponseEntity<Boolean> saveLog() {
        helpingAsyncDispersionService.saveLog();
        return ResponseEntity.ok(OK);
    }

    @GetMapping("/graph")
    public ResponseEntity<Graph> getGraph() {
        return ResponseEntity.ok(helpingAsyncDispersionService.getGraph());
    }
}
