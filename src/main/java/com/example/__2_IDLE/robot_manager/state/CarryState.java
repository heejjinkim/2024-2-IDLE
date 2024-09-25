package com.example.__2_IDLE.robot_manager.state;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CarryState extends RobotState {
    public CarryState() {
        stateName = "carry";
    }
}
