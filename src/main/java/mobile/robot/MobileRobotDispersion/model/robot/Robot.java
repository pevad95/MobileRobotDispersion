package mobile.robot.MobileRobotDispersion.model.robot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Robot {

    public interface Sensor {
        Robot settledRobotAtCurrentNode(int robotId);
        int numOfRobots();
        Robot mutexWinner(int robotId);
        int degreeOfCurrentNode(int robotId);
    }

    public interface Engine {
        void move(int robotId, int port);
    }

    public static final int NO_ROBOT = -1;

    public static final byte EXPLORE = 0;
    public static final byte BACKTRACK = 1;
    public static final byte SETTLED = 2;

    public static final boolean VISITED = true;
    public static final double MAX_SPEED = 1.0;

    private String parentNode;
    private String movingTo;
    private double speed;
    private double currentPosition;
    private int id;

    @JsonIgnore
    private Engine engine;

    @JsonIgnore
    private Sensor sensor;

    private byte state;
    private int port_entered;
    private int parent_ptr;
    private boolean seen;
    private int round;
    private boolean[] visited;
    private int[] entry_port;

    public Robot() {
        parentNode = "";
        movingTo = "";
    }

    public Robot(int id, String parentNode) {
        this.id = id;
        this.parentNode = parentNode;
        this.movingTo = parentNode;
        this.speed = MAX_SPEED;
    }

    /// ALGORITHMS

    public void init(Sensor sensor, Engine engine) {
        port_entered = Graph.NO_PORT;
        state = EXPLORE;
        parent_ptr = Graph.NO_PORT;
        seen = !VISITED;
        this.sensor = sensor;
        this.engine = engine;
    }

    public void helpingSync(int port) {
        if (round >= 0) {
            System.out.println(round + "> " + state);
            System.out.println(round + "> " + movingTo);
            if (state != SETTLED) {
                port_entered = port;
            }

            if (state == EXPLORE) {
                Robot settledRobot = sensor.settledRobotAtCurrentNode(id);
                if (settledRobot != null) {
                    seen = settledRobot.receiveVisited(id);
                    parent_ptr = settledRobot.receiveEntryPort(id);

                    if (!seen) {
                        parent_ptr = port_entered;
                        settledRobot.sendEntryPort(id, port_entered);
                        settledRobot.sendVisited(id);
                    } else {
                        state = BACKTRACK;

                        //TODO move through port_entered
                        move(port_entered);
                    }
                } else {
                    Robot mutexWinner = sensor.mutexWinner(id);
                    if (mutexWinner != null && id == mutexWinner.getId()) {
                        dock();
                    } else {
                        mutexWinner.sendEntryPort(id, port_entered);
                        mutexWinner.sendVisited(id);
                    }
                }

                if (state == EXPLORE) {
                    port_entered = (port_entered + 1) % sensor.degreeOfCurrentNode(id);

                    if (port_entered == parent_ptr) {
                        state = BACKTRACK;
                    }

                    //TODO move through port_entered
                    move(port_entered);
                }
            } else if (state == BACKTRACK) {
                Robot settledRobot = sensor.settledRobotAtCurrentNode(id);
                seen = settledRobot.receiveVisited(id);
                parent_ptr = settledRobot.receiveEntryPort(id);
                port_entered = (port_entered + 1) % sensor.degreeOfCurrentNode(id);

                if (port_entered != parent_ptr) {
                    state = EXPLORE;
                }

                move(port_entered);
            }
        }
    }

    private void dock() {
        state = SETTLED;
        this.visited = new boolean[sensor.numOfRobots()];
        this.entry_port = new int[sensor.numOfRobots()];
    }

    private void move(int port) {
        engine.move(id, port);
    }

    public boolean receiveVisited(int id) {
        return visited[id];
    }

    public int receiveEntryPort(int id) {
        return entry_port[id];
    }

    public void sendVisited(int id) {
        this.visited[id] = VISITED;
    }

    public void sendEntryPort(int id, int port) {
        this.entry_port[id] = port;
    }

    /// MOVING

    public boolean arrived() {
        return currentPosition >= 1;
    }

    public void move() {
        currentPosition += speed;
    }

    public void resetPosition() {
        currentPosition = 0;
    }
}
