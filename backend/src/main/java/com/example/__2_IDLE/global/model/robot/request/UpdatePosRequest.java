package com.example.__2_IDLE.global.model.robot.request;

import com.example.__2_IDLE.global.model.Pose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePosRequest {
    private String namespace;
    private Pose pose;
}
