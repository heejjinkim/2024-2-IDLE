package com.example.__2_IDLE.global.model.robot;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.ros.ROSValueGetter;
import com.example.__2_IDLE.ros.data_listener.topic.TopicDataListener;
import com.example.__2_IDLE.ros.message_handler.robot.TopicRobotPoseMessageHandler;
import com.example.__2_IDLE.ros.message_value.RobotPoseMessageValue;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class RobotRepository {
    private Map<String, Robot> robotMap = new HashMap<>();

    public void initRobotMap(Ros ros, List<String> namespaces) {
        for (String namespace : namespaces) {
            TopicRobotPoseMessageHandler messageHandler = new TopicRobotPoseMessageHandler(namespace);
            TopicDataListener topicDataListener = new TopicDataListener(ros, messageHandler);
            ROSValueGetter<RobotPoseMessageValue> rosValueGetter = new ROSValueGetter<>(topicDataListener, messageHandler);

            RobotPoseMessageValue poseValue = rosValueGetter.getValue();
            if (poseValue != null) {
                Pose pose = new Pose(poseValue.getX(), poseValue.getY());
                Robot robot = new Robot(namespace, pose);
                addRobot(robot);
                log.info("[RobotContainer] Initialized robot '{}' with position x: {}, y: {}", namespace, pose.getX(), pose.getY());
            }
        }
    }

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

    // 모든 로봇을 리스트로 반환하는 함수
    public List<Robot> getAllRobots() {
        return new ArrayList<>(robotMap.values());
    }
}
