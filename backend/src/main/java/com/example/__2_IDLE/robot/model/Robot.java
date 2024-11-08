package com.example.__2_IDLE.robot.model;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@Setter
@ToString
public class Robot {

    private String namespace;
    private Pose currentPose;             // 현재 위치
    private final LinkedList<PickingTask> taskQueue = new LinkedList<>(); // 로봇의 작업 큐

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

    public PickingTask getFirstTask() {
        try {
            return taskQueue.getFirst();
        } catch (RuntimeException e) {
            throw new NoSuchElementException("할당된 작업이 없습니다");
        }
    }

    public List<Long> completeCurrentTask(Station station) {
        // 동일 Station, 동일 Item Task 다 제거
        PickingTask pickingTask = taskQueue.removeFirst();

        List<Long> completedTaskIds = station.completeTask(pickingTask);
        taskQueue.removeIf(task -> completedTaskIds.contains(task.getId()));

        return completedTaskIds;
    }
}
