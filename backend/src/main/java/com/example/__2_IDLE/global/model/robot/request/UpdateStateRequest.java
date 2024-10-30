package com.example.__2_IDLE.global.model.robot.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStateRequest {
    private String namespace;
    private String state;
}
