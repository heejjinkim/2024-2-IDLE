package com.example.__2_IDLE.task_allocator.model;

import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.enums.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@Setter
public class TaskWave {

    private List<PickingTask> wave;

    private TaskWave(List<PickingTask> pickingTasks) {
        this.wave = pickingTasks;
    }

    public static TaskWave of(List<ScheduleTask> scheduleTasks) {
        List<PickingTask> pickingTaskList = createPickingTaskList(scheduleTasks);
        return new TaskWave(pickingTaskList);
    }

    private static List<PickingTask> createPickingTaskList(List<ScheduleTask> scheduleTasks) {
        AtomicReference<Long> pickingTaskId = new AtomicReference<>(0L);
        return scheduleTasks.stream()
                .flatMap(scheduleTask -> convertScheduleTaskToPickingTask(pickingTaskId, scheduleTask))
                .collect(Collectors.toList());
    }

    private static Stream<PickingTask> convertScheduleTaskToPickingTask(AtomicReference<Long> pickingTaskId, ScheduleTask scheduleTask) {
        Order order = scheduleTask.getOrder();
        Long orderId = order.getId();
        List<Item> orderItems = order.getOrderItems();

        return orderItems.stream()
                .map(orderItem -> {
                    PickingTask task = new PickingTask(pickingTaskId.get(), orderId, orderItem);
                    pickingTaskId.updateAndGet(v -> v + 1);
                    return task;
                });
    }

    public int size() {
        return wave.size();
    }

    public List<PickingTask> getWave() {
        return wave;
    }

    public boolean isEmpty() {
        return wave.isEmpty();
    }

    public boolean isAllTaskAllocated() {
        for (PickingTask task : wave) {
            if (!task.isAllocated()) {
                return false;
            }
        }
        return true;
    }
}
