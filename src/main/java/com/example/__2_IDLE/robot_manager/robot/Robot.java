package com.example.__2_IDLE.robot_manager.robot;

import com.example.__2_IDLE.robot_manager.pos.Pos;
import com.example.__2_IDLE.robot_manager.state.RobotState;
import com.example.__2_IDLE.robot_manager.state.RobotStateContainer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Robot {

    private String namespace;   
    private Pos pos;
    private RobotState state = RobotStateContainer.getWaitStateInstance();
    private int shelfId = -1;
    private int taskId = -1;

    public Robot(String namespace, Pos pos) {
        this.namespace = namespace;
        this.pos = pos;
    }
}
