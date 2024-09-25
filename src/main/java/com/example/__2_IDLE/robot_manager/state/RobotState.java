package com.example.__2_IDLE.robot_manager.state;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class RobotState {
    protected String stateName = "wait";
    public String stateName() { return stateName; }
}
