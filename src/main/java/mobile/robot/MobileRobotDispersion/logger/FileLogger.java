package mobile.robot.MobileRobotDispersion.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

public class FileLogger {

    private StringBuilder sb;

    public FileLogger() {
        sb = new StringBuilder();
    }

    private String generateFileName() {
        return "log/disp/dispersion-" + Calendar.getInstance().getTime().getTime() + ".txt";
    }

    public void log(String message) {
        sb.append(Calendar.getInstance().getTime());
        sb.append("--> ");
        sb.append(message);
        sb.append("\n-------------\n");
    }


    public void saveAndClear() {
        try (PrintWriter pw = new PrintWriter(new File(generateFileName()))) {
            pw.println(sb.toString());
            clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        sb = new StringBuilder();
    }

}
