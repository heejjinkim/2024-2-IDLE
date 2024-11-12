package com.example.__2_IDLE.robot;

import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.robot.model.RobotTaskAssigner;
import com.example.__2_IDLE.simulator.response.RobotStatusResponse;
import com.example.__2_IDLE.simulator.response.TaskStatus;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotService {

    private final RobotMapRepository robotMapRepository;
    private final RobotTaskAssignerRepository robotTaskAssignerRepository;

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

    public List<RobotStatusResponse> getRobotsStatus() {
        return robotMapRepository.getRobotMap().values().stream()
                .map(this::mapToRobotStatusResponse)
                .collect(Collectors.toList());
    }

    private RobotStatusResponse mapToRobotStatusResponse(Robot robot) {
        RobotTaskAssigner assigner = robotTaskAssignerRepository.getRobotTaskAssigner(robot.getNamespace());

        return RobotStatusResponse.builder()
                .name(robot.getNamespace())
                .working(isRobotWorking(assigner))
                .tasks(getTaskStatuses(robot))
                .build();
    }

    private boolean isRobotWorking(RobotTaskAssigner assigner) {
        return assigner != null && assigner.isRunning();
    }

    private List<TaskStatus> getTaskStatuses(Robot robot) {
        return robot.getTaskQueue().stream()
                .limit(3)
                .map(this::mapToTaskStatus)
                .collect(Collectors.toList());
    }

    private TaskStatus mapToTaskStatus(PickingTask task) {
        return TaskStatus.builder()
                .id(task.getId())
                .item(task.getItem().name())
                .build();
    }

}
