package com.example.__2_IDLE.robot_manager.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class RobotStateContainer {
    private static class WaitStateHolder {
        private static final WaitState WAIT_INSTANCE = new WaitState();
    }
    private static class CarryStateHolder {
        private static final CarryState CARRY_INSTANCE = new CarryState();
    }
    public static WaitState getWaitStateInstance () { return WaitStateHolder.WAIT_INSTANCE; }
    public static CarryState getCarryStateInstance () { return CarryStateHolder.CARRY_INSTANCE; }
    public static RobotState getState(String stateName) {
        RobotState state;
        if (stateName.equals("wait")) {
            state = getWaitStateInstance();
        } else if (stateName.equals("carry")) {
            state = getCarryStateInstance();
        } else {
            log.error("[RobotManager-getRobotsByState] : 잘못된 상태 '{}'", stateName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 상태 값: " + stateName);
        }
        return state;
    }
}
