package com.example.__2_IDLE.schedule_module;

import com.example.__2_IDLE.global.model.Task;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class ScheduleModule {
    private final double WAIT_TIME_WEIGHT = 0.2;
    private final double SCALE = 100.00; // 소수 셋째자리에서 반올림
    private List<Task> taskQueue = new ArrayList<>();

    private double calculatePriority(double waitTime, int urgency) {
        double priority = WAIT_TIME_WEIGHT * waitTime + (1 - WAIT_TIME_WEIGHT) * urgency;
        return Math.round(priority * SCALE) / SCALE;
    }

    public void run(){
        scheduleTask();
        while(!taskQueue.isEmpty()){
            Task task = pollTask();
            printTask(task);

        }
    }

    public void addTask(List<Task> tasks) {
        taskQueue.addAll(tasks);
    }

    public Task pollTask() {
        if (!taskQueue.isEmpty()) {
            return taskQueue.remove(0);
        }
        return null;
    }

    public void scheduleTask() {
        updatePriority();
        taskQueue.sort(Comparator.comparingDouble(Task::getPriority).reversed());
    }

    private void updatePriority() {
        LocalDateTime now = LocalDateTime.now();
        for (Task task : taskQueue) {
            double waitTime = java.time.Duration.between(task.getCreateTime(), now).getSeconds();
            int urgency = task.getUrgency();
            task.setPriority(calculatePriority(waitTime, urgency));
        }
    }

    public void printTask(Task task) {
        System.out.println(task);
    }
}
