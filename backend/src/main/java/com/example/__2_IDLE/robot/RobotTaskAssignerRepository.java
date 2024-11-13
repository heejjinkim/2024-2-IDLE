package com.example.__2_IDLE.robot;

import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.robot.model.RobotTaskAssigner;
import com.example.__2_IDLE.task_allocator.StationService;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class RobotTaskAssignerRepository {
    private final Map<String, RobotTaskAssigner> taskAssignerMap = new HashMap<>();

    public void init(Ros ros, List<Robot> robotList, StationService stationService, OrderService orderService) {
        for (Robot robot : robotList) {
            addRobotTaskAssigner(robot.getNamespace(), new RobotTaskAssigner(robot, ros, stationService, orderService));
        }
    }

    private void addRobotTaskAssigner(String robotName, RobotTaskAssigner assigner) {
        taskAssignerMap.put(robotName, assigner);
    }

    public RobotTaskAssigner getRobotTaskAssigner(String robotName) {
        return taskAssignerMap.get(robotName);
    }

    public void clear() {
        taskAssignerMap.clear();
    }

    public boolean isTaskAssignerMapEmpty() {
        return taskAssignerMap.isEmpty();
    }

    public void startAllTaskAssigners() {
        taskAssignerMap.forEach((robotName, taskAssigner) -> {
            taskAssigner.start();
        });
    }
}
