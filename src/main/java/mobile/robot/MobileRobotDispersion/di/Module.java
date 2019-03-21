package mobile.robot.MobileRobotDispersion.di;

import mobile.robot.MobileRobotDispersion.logger.FileLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Module {

    @Bean
    public FileLogger fileLogger() {
        return new FileLogger();
    }
}
