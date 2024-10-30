package com.example.__2_IDLE.global.model.robot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
@Component
public class RobotRepository {
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
        log.info("[RobotRepository-addRobot] : 로봇 '{}'을 추가하였습니다.", namespace);
    }

    // 로봇을 제거하는 함수
    public void removeRobot (String namespace) {
        Robot robot = robotMap.remove(namespace);
        if (robot != null) {
            log.info("[RobotRepository-removeRobot] : 로봇 '{}'를 제거하였습니다.", namespace);
        } else {
            log.info("[RobotRepository-removeRobot] : 제거할 로봇 '{}'이 존재하지 않습니다.", namespace);
        }
    }
}
