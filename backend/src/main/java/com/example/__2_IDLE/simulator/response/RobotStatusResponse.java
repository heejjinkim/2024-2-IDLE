package com.example.__2_IDLE.simulator.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RobotStatusResponse {
    private String name;
    private boolean working;
    private List<TaskStatus> tasks;
}
