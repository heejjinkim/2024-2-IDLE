package com.example.__2_IDLE.robot;

import com.example.__2_IDLE.robot.model.RobotTaskAssigner;
import com.example.__2_IDLE.robot.model.Robot;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class RobotTaskAssignerRepository {
    private static final Map<String, RobotTaskAssigner> taskAssignerMap = new HashMap<>();

    public static void init(Ros ros, List<Robot> robotList) {
        for (Robot robot : robotList) {
            addRobotTaskAssigner(robot.getNamespace(), new RobotTaskAssigner(robot, ros));
        }
    }

    private static void addRobotTaskAssigner(String robotName, RobotTaskAssigner assigner) {
        taskAssignerMap.put(robotName, assigner);
    }

    public static RobotTaskAssigner getRobotTaskAssigner(String robotName) {
        return taskAssignerMap.get(robotName);
    }

    public static void clear() {
        taskAssignerMap.clear();
    }

    public static boolean isTaskAssignerMapEmpty() {
        return taskAssignerMap.isEmpty();
    }

    public static void startAllTaskAssigners() {
        taskAssignerMap.forEach((robotName, taskAssigner) -> {
            taskAssigner.start();
        });
    }
}
