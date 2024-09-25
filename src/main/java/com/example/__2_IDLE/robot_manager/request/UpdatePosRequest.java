package com.example.__2_IDLE.robot_manager.request;

import com.example.__2_IDLE.robot_manager.pos.Pos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePosRequest {
    private String namespace;
    private Pos pos;
}
