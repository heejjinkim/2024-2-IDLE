package com.example.__2_IDLE.robot_manager.state;

import com.example.__2_IDLE.robot_manager.robot.Robot;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public class WaitState extends RobotState{
    @Override
    public void startTask(Robot robot) {
        log.info("현재 로봇이 할당 받은 작업이 없습니다.");
    }
}
