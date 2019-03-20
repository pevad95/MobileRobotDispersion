package mobile.robot.MobileRobotDispersion.model.graph;

import lombok.Data;
import mobile.robot.MobileRobotDispersion.model.robot.Robot;

import java.util.*;
import java.util.stream.IntStream;

@Data
public class Node {

    public static final int NO_WINNER = -1;

    private String id;
    private int settledRobotId;
    private Map<Integer, String> ports;
    private List<Integer> arrivingRobots;
    private int mutexWinnerId;

    public Node(String id, String[] neighbours) {
        this.id = id;
        this.ports = new TreeMap<>();
        this.arrivingRobots = new ArrayList<>();
        this.settledRobotId = Robot.NO_ROBOT;
        this.mutexWinnerId = NO_WINNER;

        IntStream.range(0, neighbours.length)
                .forEach(i -> ports.put(i, neighbours[i]));
    }

    public Node() {
        this.arrivingRobots = new ArrayList<>();
        this.ports = new TreeMap<>();
        this.settledRobotId = Robot.NO_ROBOT;
    }

    public int degree() {
        return ports.size();
    }

    /// MUTEX

    public int mutex() {
        if (mutexWinnerId == NO_WINNER) {
            mutexWinnerId = Collections.min(arrivingRobots);
        }

        return mutexWinnerId;
    }

    public void resetMutex() {
        mutexWinnerId = NO_WINNER;
    }

}
