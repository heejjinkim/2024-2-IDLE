package com.example.__2_IDLE.task_allocator.controller;

import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.robot.RobotService;
import com.example.__2_IDLE.robot.request.AddRobotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;

    //로봇 추가 (FRONT -> SPRING -> ROS)
    @PostMapping("/robot/add")
    public void addRobot(@RequestBody AddRobotRequest request) {
        // TODO ROS 서버로 요청 보내고 응답 받기
        Robot robot = new Robot(request.getNamespace(), request.getPose());
        robotService.addRobot(robot);
    }

    // 로봇 제거 (FRONT -> SPRING -> ROS)
    @DeleteMapping("/robot/remove")
    public void removeRobot(@RequestParam String namespace) {
        // TODO ROS 서버로 요청 보내고 응답 받기
        robotService.removeRobot(namespace);
    }

    // 로봇 리스트 가져오기
    public Map<String, Robot> getAllRobots() {
        return robotService.getAllRobots();
    }
}
