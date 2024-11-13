package com.example.__2_IDLE.robot.request;

import com.example.__2_IDLE.global.model.Pose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRobotRequest {
    private String namespace;
    private Pose pose;
}
