package mobile.robot.MobileRobotDispersion.services.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GraphException extends Exception {

    public GraphException(String message) {
        super(message);
    }
}
