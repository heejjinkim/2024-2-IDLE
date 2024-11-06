package com.example.__2_IDLE.schedule_module;

import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class ScheduleModule {

    private static final double WAIT_TIME_WEIGHT = 0.2;
    private static final double SCALE = 100.00; // 소수 셋째자리에서 반올림
    public static final int WAVE_SIZE = 10;

    private List<ScheduleTask> taskQueue = new ArrayList<>();

    private double calculatePriority(double waitTime, int urgency) {
        double priority = WAIT_TIME_WEIGHT * waitTime + (1 - WAIT_TIME_WEIGHT) * urgency;
        return Math.round(priority * SCALE) / SCALE;
    }

    public boolean run() {
        updatePriority();
        taskQueue.sort(Comparator.comparingDouble(ScheduleTask::getPriority).reversed());
        return true;
    }

    public void addAllTask(List<ScheduleTask> tasks) {
        taskQueue.addAll(tasks);
    }

    private void updatePriority() {
        LocalDateTime now = LocalDateTime.now();
        for (ScheduleTask task : taskQueue) {
            double waitTime = java.time.Duration.between(task.getCreateTime(), now).getSeconds();
            int urgency = task.getUrgency();
            task.setPriority(calculatePriority(waitTime, urgency));
        }
    }

    public TaskWave getTaskWave() {
        if (taskQueue.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("taskQueue is empty");
        }
        List<ScheduleTask> wave = taskQueue.subList(0, Math.min(WAVE_SIZE, taskQueue.size()));
        TaskWave taskWave = TaskWave.of(wave);
        taskQueue.removeAll(wave);

        return taskWave;
    }

    public boolean isTaskQueueEmpty() {
        return taskQueue.isEmpty();
    }
}
