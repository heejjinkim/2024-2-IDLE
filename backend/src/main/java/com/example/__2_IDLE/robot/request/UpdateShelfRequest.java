package com.example.__2_IDLE.robot.request;

import com.example.__2_IDLE.global.model.enums.Shelf;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateShelfRequest {
    private String namespace;
    private Shelf shelf;
}
