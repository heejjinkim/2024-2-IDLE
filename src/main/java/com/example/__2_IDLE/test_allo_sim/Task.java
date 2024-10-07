package com.example.__2_IDLE.test_allo_sim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Task {

    private long taskId;
    private Item item;
    private Robot targetRobot;
    private Station targetStation;
}
