package com.example.__2_IDLE.global.model.robot;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.task_allocator.model.PickingTask;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

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

    public void allocateTask(PickingTask pickingTask) {
        taskQueue.add(pickingTask);
    }

    public PickingTask getLastTask() {
        try {
            return taskQueue.get(taskQueue.size() - 1);
        } catch (RuntimeException e) {
            throw new NoSuchElementException("할당된 작업이 없습니다");
        }
    }

    public void allocateAllTask(List<PickingTask> stronglyCorrelatedTasks) {
        taskQueue.addAll(stronglyCorrelatedTasks);
    }

    public boolean hasTask() {
        return !taskQueue.isEmpty();
    }
}
