package com.example.__2_IDLE.robot_manager.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateShelfRequest {
    private String namespace;
    private int shelfId;
}
