package com.example.__2_IDLE.test_allo_sim;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class Station {

    private Pose pose;
    private String name;
    private boolean occupied;
    private Queue<Task> taskQueue;
    private Task nowTask;

    public Station(Pose pose, String name) {
        this.pose = pose;
        this.name = name;
        this.occupied = false;
        this.taskQueue = new LinkedList<>();
    }

    public boolean isEqual(Station station) {
        return this.name.equals(station.getName());
    }

    /**
     * nowTask값만 수정
     */
    public void finishTask() {
        nowTask = taskQueue.poll();
    }

    /**
     * 현재 진행 중인 작업이 있는 경우 taskQueue에 add
     * 현재 진행중인 작업이 없고 taskQeue가 비어있는 경우 바로 현재 진행 중인 작업으로
     * 나머지 경우 taskQueue에 add
     * @param task
     */
    public void addTask(Task task) {
        if (nowTask != null) {
            taskQueue.add(task);
            return;
        }
        if (taskQueue.isEmpty()) {
            nowTask = task;
            return;
        }
        taskQueue.add(task); // 지금 진행 중인 task는 없지만 taskQueue가 비어 있지 않은 경우
    }
}
