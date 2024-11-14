package com.example.__2_IDLE.schedule;

import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.schedule.model.ScheduleTask;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScheduleService {

    private static final double WAIT_TIME_WEIGHT = 0.2;
    private static final double SCALE = 100.00; // 소수 셋째자리에서 반올림
    public static final int WAVE_SIZE = 5;

    private final ScheduleRepository scheduleRepository;

    private double calculatePriority(double waitTime, int urgency) {
        double priority = WAIT_TIME_WEIGHT * waitTime + (1 - WAIT_TIME_WEIGHT) * urgency;
        return Math.round(priority * SCALE) / SCALE;
    }

    public boolean run() {
        updatePriority();
        scheduleRepository.sortTasksByPriority();
        return true;
    }

    public void createScheduleTasks(List<Order> orders) {
        List<ScheduleTask> tasks = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            ScheduleTask task = new ScheduleTask();
            task.setId(i);
            if (orders.get(i).isSameDayDelivery()) {
                task.setUrgency(1);
            }
            task.setCreateTime(LocalDateTime.now());
            task.setOrder(orders.get(i));
            tasks.add(task);
        }
        scheduleRepository.addTasks(tasks);
    }

    public void addAllTask(List<ScheduleTask> tasks) {
        scheduleRepository.addTasks(tasks);
    }

    private void updatePriority() {
        LocalDateTime now = LocalDateTime.now();
        List<ScheduleTask> taskQueue = scheduleRepository.getAllTasks();

        for (ScheduleTask task : taskQueue) {
            double waitTime = java.time.Duration.between(task.getCreateTime(), now).getSeconds();
            int urgency = task.getUrgency();
            task.setPriority(calculatePriority(waitTime, urgency));
        }
    }

    public TaskWave getTaskWave() {
        List<ScheduleTask> taskQueue = scheduleRepository.getAllTasks();
        if (taskQueue.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("taskQueue is empty");
        }
        List<ScheduleTask> wave = taskQueue.subList(0, Math.min(WAVE_SIZE, taskQueue.size()));
        TaskWave taskWave = TaskWave.of(wave);
        scheduleRepository.removeTasks(wave);

        return taskWave;
    }

    public boolean isTaskQueueEmpty() {
        return scheduleRepository.isTaskQueueEmpty();
    }
}
