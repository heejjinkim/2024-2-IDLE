package com.example.__2_IDLE.robot_manager.robot;

import com.example.__2_IDLE.robot_manager.state.RobotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class RobotContainer {
    private Map<String, Robot> robotMap = new HashMap<>();

    // 로봇을 리턴하는 함수
    public Optional<Robot> getRobot (String namespace) {
        Robot robot = robotMap.get(namespace);
        return Optional.ofNullable(robot);
    }

    // 로봇을 추가하는 함수
    public void addRobot (Robot robot) {
        String namespace = robot.getNamespace();
        robotMap.put(namespace, robot);
        log.info("[RobotContainer-addRobot] : 로봇 '{}'을 추가하였습니다.", namespace);
    }

    // 로봇을 제거하는 함수
    public void removeRobot (String namespace) {
        Robot robot = robotMap.remove(namespace);
        if (robot != null) {
            log.info("[RobotContainer-removeRobot] : 로봇 '{}'를 제거하였습니다.", namespace);
        } else {
            log.info("[RobotContainer-removeRobot] : 제거할 로봇 '{}'이 존재하지 않습니다.", namespace);
        }
    }

    // 상태에 해당하는 로봇 map 리턴
    public Map<String, Robot> getRobotsByState (RobotState state) {
        Map<String, Robot> returnMap = new HashMap<>();
        for (Robot robot : robotMap.values()) {
            RobotState robotState = robot.getState();
            if (robotState.getClass() == state.getClass()) {
                returnMap.put(robot.getNamespace(), robot);
            }
        }
        return returnMap;
    }
}
