package com.example.__2_IDLE.robot_manager.robot;

import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.robot_manager.pos.Pos;
import com.example.__2_IDLE.robot_manager.state.RobotState;
import com.example.__2_IDLE.robot_manager.state.RobotStateContainer;
import com.example.__2_IDLE.task.model.RobotTask;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Robot {

    private String namespace;   
    private Pos pos;             // 현재 위치
    private RobotState state;    // 현재 상태
    private Shelf shelf;         // 현재 운반 중인 선반
    private RobotTask robotTask; // 현재 할당 받은 작업

    public Robot(String namespace, Pos pos) {
        this.namespace = namespace;
        this.pos = pos;
        this.state = RobotStateContainer.getWaitStateInstance();
        this.shelf = null;
        this.robotTask = null;
    }

    public void setRobotTask(RobotTask robotTask) {
        this.robotTask = robotTask;
        this.state = RobotStateContainer.getCarryStateInstance();
    }

    public void startTask() { // 로봇의 작업 수행
        state.startTask(this);
    }

    public void resetRobotTask() {
        this.robotTask = null;
        this.shelf = null;
    }
}
