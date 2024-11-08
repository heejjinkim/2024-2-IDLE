package com.example.__2_IDLE.robot;

import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.robot.model.Robot;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotService {

    private final RobotMapRepository robotMapRepository;

    public void initRobotMap(Ros ros) {
        robotMapRepository.init(ros);
    }

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
        robotMapRepository.addRobot(robot);
    }

    public void removeRobot(String namespace) {
        robotMapRepository.removeRobot(namespace);
    }

    public Map<String, Robot> getAllRobots() {
        return robotMapRepository.getRobotMap();
    }

    public Optional<Robot> getRobot(String namespace) {
        return robotMapRepository.getRobot(namespace);
    }
}
