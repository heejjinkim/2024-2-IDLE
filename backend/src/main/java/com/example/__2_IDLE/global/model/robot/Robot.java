package com.example.__2_IDLE.global.model.robot;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.task_allocator.controller.StationController;
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

    public double doTask(StationController stationController) {

        double WAIT_TIME = 5;
        double result = 0;

        int currentTaskIndex = 0;
        int taskCount = taskQueue.size();

        PickingTask currentTask = taskQueue.get(currentTaskIndex);
        double firstTaskTime = Pose.distance(currentPose, currentTask.getPose())
                + Pose.distance(currentTask.getPose(), findStationPose(stationController, currentTask));

        currentTaskIndex += 1;
        int nextTaskIndex = 2;
        while (nextTaskIndex < taskCount) {
            currentTask = taskQueue.get(currentTaskIndex);
            PickingTask nextTask = taskQueue.get(nextTaskIndex);
            if (isSameItem(currentTask, nextTask)){
                result += Pose.distance(findStationPose(stationController, currentTask), findStationPose(stationController, nextTask));
            } else {
                result += Pose.distance(findStationPose(stationController, currentTask), nextTask.getPose())
                        + Pose.distance(nextTask.getPose(), findStationPose(stationController, nextTask));
            }
            result += WAIT_TIME;
            currentTaskIndex += 1;
            nextTaskIndex += 1;
        }

        return result;
    }

    public void addTask(PickingTask task) {
        this.taskQueue.add(task);
    }

    public void clearTaskQueue() {
        taskQueue.clear();
    }

    private Pose findStationPose(StationController stationController, PickingTask currentTask) {
        return stationController.getStationHasTask(currentTask).get().getPose();
    }

    private boolean isSameItem(PickingTask currentTask, PickingTask nextTask) {
        return currentTask.getItem() == nextTask.getItem();
    }

    private boolean isSameStation(StationController stationController, PickingTask currentTask, PickingTask nextTask) {
        return stationController.getStationHasTask(currentTask).get().equals(stationController.getStationHasTask(nextTask).get());
    }
}
