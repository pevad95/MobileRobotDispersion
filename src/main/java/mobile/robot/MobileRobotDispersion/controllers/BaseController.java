package mobile.robot.MobileRobotDispersion.controllers;

import org.springframework.http.ResponseEntity;

public class BaseController {

    public static final String ERROR_RESPONSE_HEADER_NAME = "error";
    public static final boolean OK = true;

    public ResponseEntity createBadRequest(Exception e) {
        return ResponseEntity.badRequest().header(ERROR_RESPONSE_HEADER_NAME, e.getMessage()).build();
    }

    public ResponseEntity createBadRequest() {
        return ResponseEntity.badRequest().header(ERROR_RESPONSE_HEADER_NAME, "Unexpected error!").build();
    }
}
