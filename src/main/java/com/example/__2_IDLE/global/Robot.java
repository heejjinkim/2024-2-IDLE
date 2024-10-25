package com.example.__2_IDLE.global;

import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.Pose;
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
    private Pose currentPose;           // 현재 위치
    private Shelf shelf;         // 현재 운반 중인 선반
    private RobotTask robotTask; // 현재 할당 받은 작업
    private LinkedList<RobotTask> taskQueue; // 로봇의 작업 큐

    public Robot(String namespace, Pose pose) {
        this.namespace = namespace;
        this.currentPose = pose;
        this.shelf = null;
        this.robotTask = null;
        this.taskQueue = new LinkedList<>();
    }
}
