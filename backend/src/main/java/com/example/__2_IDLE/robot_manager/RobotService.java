package com.example.__2_IDLE.robot_manager;

import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.Robot;
import com.example.__2_IDLE.task.model.RobotTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RobotService {
    private RobotRepository robotRepository;

    public void updateShelf(String namespace, Shelf shelf) {
        Optional<Robot> optionalRobot = getRobot(namespace);
        if (optionalRobot.isPresent()) { // 선반 업데이트
            Robot targetRobot = optionalRobot.get();
        } else {
            log.info("로봇 '{}'가 존재하지 않습니다.", namespace);
        }
    }

    public void updatePos(String namespace, Pose pose) {
        Optional<Robot> optionalRobot = getRobot(namespace);
        if (optionalRobot.isPresent()) {
            Robot robot = optionalRobot.get();
            robot.setCurrentPose(pose);
            log.info("로봇 {}의 pos를 {}로 변경하였습니다.", namespace, robot.getCurrentPose().toString());
        } else {
            log.info("로봇 '{}'가 존재하지 않습니다.", namespace);
        }
    }

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
