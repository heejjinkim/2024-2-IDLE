package com.example.__2_IDLE.robot_manager.robot;

import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.robot_manager.state.RobotState;
import com.example.__2_IDLE.robot_manager.state.RobotStateContainer;
import com.example.__2_IDLE.task.model.RobotTask;
import java.util.LinkedList;
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
    private Pose pose;             // 현재 위치
    private RobotState state;    // 현재 상태
    private Shelf shelf;         // 현재 운반 중인 선반
    private RobotTask robotTask; // 현재 할당 받은 작업
    private LinkedList<RobotTask> taskQueue; // 로봇의 작업 큐

    public Robot(String namespace, Pose pose) {
        this.namespace = namespace;
        this.pose = pose;
        this.state = RobotStateContainer.getWaitStateInstance();
        this.shelf = null;
        this.robotTask = null;
        this.taskQueue = new LinkedList<>();
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
