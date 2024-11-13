package com.example.__2_IDLE.task_allocator.model;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.schedule.model.ScheduleTask;
import com.example.__2_IDLE.global.model.enums.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public class TaskWave {

    private List<PickingOrder> wave;

    private TaskWave(List<PickingOrder> pickingOrders) {
        this.wave = pickingOrders;
        distributeOrderToStations();
    }

    public static TaskWave of(List<ScheduleTask> scheduleTasks) {
        List<PickingOrder> pickingOrders = createPickingOrderList(scheduleTasks);
        return new TaskWave(pickingOrders);
    }

    public void distributeOrderToStations() {
        int stationCount = Station.values().length;
        int stationIndex = 0;
        for (PickingOrder pickingOrder : wave) {
            Station station = Station.values()[stationIndex];
            station.allocateOrder(pickingOrder);
            stationIndex += 1;
            if (stationIndex == stationCount) {
                stationIndex = 0;
            }
        }
    }

    public int size() {
        return wave.size();
    }

    public PickingTask getNearestPickingTask(Robot robot) {
        PickingTask nearestPickingTask = null;
        long minDistance = Long.MAX_VALUE;

        for (PickingOrder pickingOrder : wave) {
            PickingTask pickingTask = pickingOrder.findNearestTask(robot);
            double distance = pickingTask.calcDistance(robot);
            if (distance < minDistance) {
                nearestPickingTask = pickingTask;
            }
        }
        return nearestPickingTask;
    }

    public boolean isEmpty() {
        return wave.isEmpty();
    }

    public boolean isAllTaskAllocated() {
        for (PickingOrder order : wave) {
            if (!order.isAllTaskAllocated()) {
                return false;
            }
        }
        return true;
    }

    private static List<PickingOrder> createPickingOrderList(List<ScheduleTask> scheduleTasks) {
        AtomicLong pickingTaskId = new AtomicLong(0);
        List<PickingOrder> pickingOrders = new ArrayList<>();

        for (ScheduleTask scheduleTask : scheduleTasks) {
            PickingOrder pickingOrder = convertScheduleOrderToPickingOrder(scheduleTask, pickingTaskId);
            pickingOrders.add(pickingOrder);
        }
        return pickingOrders;
    }

    private static PickingOrder convertScheduleOrderToPickingOrder(ScheduleTask scheduleTask, AtomicLong pickingTaskId) {
        Order order = scheduleTask.getOrder();
        Long orderId = order.getId();
        List<Item> orderItems = order.getOrderItems();

        List<PickingTask> pickingTasks = orderItems.stream()
                .map(item -> new PickingTask(pickingTaskId.getAndIncrement(), orderId, item))
                .collect(Collectors.toList());
        return new PickingOrder(pickingTasks);
    }
}
