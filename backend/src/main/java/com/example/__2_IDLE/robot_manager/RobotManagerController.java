package com.example.__2_IDLE.robot_manager;

import com.example.__2_IDLE.robot_manager.request.AddRobotRequest;
import com.example.__2_IDLE.robot_manager.robot.Robot;
import com.example.__2_IDLE.robot_manager.state.RobotState;
import com.example.__2_IDLE.robot_manager.state.RobotStateContainer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class RobotManagerController {
    private RobotManagerService robotManagerService;


    //로봇 추가 (FRONT -> SPRING -> ROS)
    @PostMapping("/robot/add")
    public void addRobot(@RequestBody AddRobotRequest request) {
        // TODO ROS 서버로 요청 보내고 응답 받기
        Robot robot = new Robot(request.getNamespace(), request.getPose());
        robotManagerService.addRobot(robot);
    }

    // 로봇 제거 (FRONT -> SPRING -> ROS)
    @DeleteMapping("/robot/remove")
    public void removeRobot(@RequestParam String namespace) {
        // TODO ROS 서버로 요청 보내고 응답 받기
        robotManagerService.removeRobot(namespace);
    }

    // 특정 상태의 로봇들 찾기
    @GetMapping("/robot/get-by-state")
    public Map<String, Robot> getRobotsByState (@RequestParam String stateName) {
        RobotState state = RobotStateContainer.getState(stateName);
        return robotManagerService.getRobotByState(state);
    }
}
