package com.example.__2_IDLE.task_allocator.model;

import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.enums.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Getter
@Setter
public class TaskWave {

    private final List<PickingTask> wave;

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
                .flatMap(scheduleTask -> {
                    pickingTaskId.updateAndGet(v -> v + 1);
                    return convertScheduleTaskToPickingTask(pickingTaskId.get(), scheduleTask);
                })
                .toList();
    }

    private static Stream<PickingTask> convertScheduleTaskToPickingTask(Long pickingTaskId, ScheduleTask scheduleTask) {
        Order order = scheduleTask.getOrder();
        Long orderId = order.getId();
        List<Item> orderItems = order.getOrderItems();

        return orderItems.stream()
                .map(orderItem -> new PickingTask(pickingTaskId, orderId, orderItem));
    }

    public int size() {
        return wave.size();
    }

    public PickingTask getNext() {
        return wave.get(0);
    }

    public boolean hasNext() {
        return !wave.isEmpty();
    }

    public List<PickingTask> getWave() {
        return List.copyOf(wave);
    }

    public void removeTask(PickingTask pickingTask) {
        wave.remove(pickingTask);
    }

    public PickingTask getById(Long id) {
        return wave.stream()
                .filter(pickingTask -> pickingTask.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("일치하는 PickingTask가 존재하지 않습니다."));
    }

    public void removeAllTask(List<PickingTask> stronglyCorrelatedTasks) {
        wave.removeAll(stronglyCorrelatedTasks);
    }

    public boolean isEmpty() {
        return wave.isEmpty();
    }
}
