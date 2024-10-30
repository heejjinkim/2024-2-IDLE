package com.example.__2_IDLE.global.model.robot;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.task.model.RobotTask;
import com.example.__2_IDLE.task_allocator.PickingTask;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Robot {

    private String namespace;
    private Pose currentPose;             // 현재 위치
    private final List<PickingTask> taskQueue = new LinkedList<>(); // 로봇의 작업 큐

    public Robot(String namespace, Pose pose) {
        this.namespace = namespace;
        this.currentPose = pose;
    }

    public RobotTask getFirstTaskInQueue() {
        return taskQueue.isEmpty() ? null : taskQueue.get(0);
    }

    public void completeCurrentTask() {
        taskQueue.remove(0);
    }
}
