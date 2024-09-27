package com.example.__2_IDLE.robot_manager.state;

import com.example.__2_IDLE.robot_manager.pos.Pos;
import com.example.__2_IDLE.robot_manager.robot.Robot;
import com.example.__2_IDLE.task.model.RobotTask;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class RobotState {
    protected String stateName = "wait";
    public String stateName() { return stateName; }
    abstract public void startTask(Robot robot);
    public boolean isWaiting() {
        return "wait".equals(stateName);
    }
}
