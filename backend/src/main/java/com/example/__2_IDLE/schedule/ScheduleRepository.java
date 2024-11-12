package com.example.__2_IDLE.schedule;

import com.example.__2_IDLE.schedule.model.ScheduleTask;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class ScheduleRepository {
    private final List<ScheduleTask> taskQueue = new ArrayList<>();

    public List<ScheduleTask> getAllTasks() {
        return new ArrayList<>(taskQueue); // 방어적 복사
    }

    public void addTasks(List<ScheduleTask> tasks) {
        taskQueue.addAll(tasks);
    }

    public void removeTasks(List<ScheduleTask> tasks) {
        taskQueue.removeAll(tasks);
    }

    public boolean isTaskQueueEmpty() {
        return taskQueue.isEmpty();
    }

    public void sortTasksByPriority() {
        taskQueue.sort(Comparator.comparingDouble(ScheduleTask::getPriority).reversed());
    }
}
