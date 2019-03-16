package mobile.robot.MobileRobotDispersion.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispersion")
public class DispersionAlgorithmsController {

    @GetMapping("/supported")
    public ResponseEntity<String> supportedAlgorithms() {
        return ResponseEntity.ok("HELPING-SYNC, HELPING_ASYNC, INDEPENDENT-ASYNC");
    }

}
