package com.example.__2_IDLE.robot;

import com.example.__2_IDLE.global.model.Robot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RobotService {
    private RobotRepository robotRepository;

    public void addRobot(Robot robot) {
        if (getRobot(robot.getNamespace()).isPresent()) {
            log.info("이미 로봇 {}가 존재합니다.", robot.getNamespace());
        }
        robotRepository.addRobot(robot);
    }



    public void removeRobot(String namespace) {
        robotRepository.removeRobot(namespace);
    }

    public Optional<Robot> getRobot(String namespace) {
        return robotRepository.getRobot(namespace);
    }
}
