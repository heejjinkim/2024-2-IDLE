package com.example.__2_IDLE.simulator.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TaskStatus {
    private Long id;
    private String item;
}
