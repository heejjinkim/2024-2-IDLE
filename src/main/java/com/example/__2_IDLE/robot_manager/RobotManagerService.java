package com.example.__2_IDLE.robot_manager;

import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.Robot;
import com.example.__2_IDLE.robot_manager.robot.RobotContainer;
import com.example.__2_IDLE.robot_manager.state.RobotState;
import com.example.__2_IDLE.robot_manager.state.RobotStateContainer;
import com.example.__2_IDLE.task.model.RobotTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RobotManagerService {
    private RobotContainer robotContainer;

    public void updateShelf(String namespace, Shelf shelf) {
        Optional<Robot> optionalRobot = getRobot(namespace);
        if (optionalRobot.isPresent()) { // 선반 업데이트
            Robot targetRobot = optionalRobot.get();
            targetRobot.setShelf(shelf);
        } else {
            log.info("로봇 '{}'가 존재하지 않습니다.", namespace);
        }
    }

    public void updatePos(String namespace, Pose pose) {
        Optional<Robot> optionalRobot = getRobot(namespace);
        if (optionalRobot.isPresent()) {
            Robot robot = optionalRobot.get();
            robot.setPose(pose);
            log.info("로봇 {}의 pos를 {}로 변경하였습니다.", namespace, robot.getPose().toString());
        } else {
            log.info("로봇 '{}'가 존재하지 않습니다.", namespace);
        }
    }

    public void updateState(String namespace, String stateName) {
        Optional<Robot> optionalRobot = getRobot(namespace);
        if (optionalRobot.isPresent()) { // 상태 업데이트
            Robot targetRobot = optionalRobot.get();
            RobotState currentState = targetRobot.getState();
            RobotState nextState = RobotStateContainer.getState(stateName);
            targetRobot.setState(nextState);
            log.info("로봇 '{}' 상태 {} -> {} 업데이트 완료", namespace, currentState.stateName(), nextState.stateName());
        } else {
            log.info("로봇 '{}'가 존재하지 않습니다.", namespace);
        }
    }

    public void updateRobotTask (String namespace, RobotTask robotTask) {
        Optional<Robot> optionalRobot = getRobot(namespace);
        if (optionalRobot.isPresent()) {
            Robot targetRobot = optionalRobot.get();
            targetRobot.setRobotTask(robotTask);
        }
    }

    public void addRobot(Robot robot) {
        if (getRobot(robot.getNamespace()).isPresent()) {
            log.info("이미 로봇 {}가 존재합니다.", robot.getNamespace());
        }
        robotContainer.addRobot(robot);
    }
    public void removeRobot(String namespace) { robotContainer.removeRobot(namespace); }
    public Map<String, Robot> getRobotByState(RobotState state) { return robotContainer.getRobotsByState(state); }
    public Optional<Robot> getRobot(String namespace) { return robotContainer.getRobot(namespace); }
}
