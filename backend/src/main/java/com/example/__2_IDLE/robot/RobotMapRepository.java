package com.example.__2_IDLE.robot;

import com.example.__2_IDLE.global.exception.RestApiException;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.RobotNamespace;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.ros.ROSValueGetter;
import com.example.__2_IDLE.ros.data_listener.topic.TopicDataListener;
import com.example.__2_IDLE.ros.message_handler.robot.TopicRobotPoseMessageHandler;
import com.example.__2_IDLE.ros.message_value.RobotPoseMessageValue;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.example.__2_IDLE.global.exception.errorcode.RobotErrorCode.ROBOT_NOT_FOUND;

@Slf4j
@Getter
@Repository
@RequiredArgsConstructor
public class RobotMapRepository {
    private Map<String, Robot> robotMap = new HashMap<>();

    public void init(Ros ros) {
        for (RobotNamespace robotNamespace : RobotNamespace.values()) {
            String namespace = robotNamespace.getNamespace();
            Robot robot = getRobotFromRos(ros, namespace);
            addRobot(robot);
        }
    }

    private Robot getRobotFromRos(Ros ros, String namespace) {
        TopicRobotPoseMessageHandler messageHandler = new TopicRobotPoseMessageHandler(namespace);
        TopicDataListener topicDataListener = new TopicDataListener(ros, messageHandler);
        ROSValueGetter<RobotPoseMessageValue> rosValueGetter = new ROSValueGetter<>(topicDataListener, messageHandler);

        RobotPoseMessageValue poseValue = rosValueGetter.getValue();
        if (poseValue != null) {
            Pose pose = new Pose(poseValue.getX(), poseValue.getY());
            return new Robot(namespace, pose);
        }
        throw new RestApiException(ROBOT_NOT_FOUND);
    }

    // 로봇을 리턴하는 함수
    public Optional<Robot> getRobot(String namespace) {
        Robot robot = robotMap.get(namespace);
        return Optional.ofNullable(robot);
    }

    // 로봇을 추가하는 함수
    public void addRobot(Robot robot) {
        String namespace = robot.getNamespace();
        robotMap.put(namespace, robot);
        log.info("[RobotRepository-addRobot] : 로봇 '{}'을 추가하였습니다.", namespace);
    }

    // 로봇을 제거하는 함수
    public void removeRobot(String namespace) {
        Robot robot = robotMap.remove(namespace);
        if (robot != null) {
            log.info("[RobotRepository-removeRobot] : 로봇 '{}'를 제거하였습니다.", namespace);
        } else {
            log.info("[RobotRepository-removeRobot] : 제거할 로봇 '{}'이 존재하지 않습니다.", namespace);
        }
    }
}
