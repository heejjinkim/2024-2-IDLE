package com.example.__2_IDLE.schedule_module;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class ScheduleModule {
    private final double WAIT_TIME_WEIGHT = 0.2;
    private final double SCALE = 100.00; // 소수 셋째자리에서 반올림
    private List<TempTask> taskQueue = new ArrayList<>();

    private double calculatePriority(double waitTime, int urgency) {
        double priority = WAIT_TIME_WEIGHT * waitTime + (1 - WAIT_TIME_WEIGHT) * urgency;
        return Math.round(priority * SCALE) / SCALE;
    }

    @Getter
    @Setter
    private class TempTask {
        private int id;
        private double waitTime;
        private double priority;
        private int urgency;
    }

    public void aging() {
        for (TempTask task : taskQueue) {
            task.setWaitTime(task.getWaitTime() + 1);
        }
    }

    public void addTask(TempTask task) {
        taskQueue.add(task);
    }

    public TempTask pollTask() {
        if (!taskQueue.isEmpty()) {
            return taskQueue.remove(0);
        }
        return null;
    }

    public void scheduleTask() {
        updatePriority();
        taskQueue.sort(Comparator.comparingDouble(TempTask::getPriority).reversed());
    }

    private void updatePriority() {
        for (TempTask task : taskQueue) {
            double waitTime = task.getWaitTime();
            int urgency = task.getUrgency();
            task.setPriority(calculatePriority(waitTime, urgency));
        }
    }
}
