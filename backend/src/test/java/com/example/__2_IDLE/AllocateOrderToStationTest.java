package com.example.__2_IDLE;

import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.order.OrderRepository;
import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.schedule.ScheduleRepository;
import com.example.__2_IDLE.schedule.ScheduleService;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class AllocateOrderToStationTest {

    private OrderService orderService = new OrderService(new OrderRepository());
    private ScheduleService scheduleService = new ScheduleService(new ScheduleRepository());

    @BeforeEach
    void beforeEach() {
        List<Order> orders = orderService.generateRandomOrders();
        scheduleService.createScheduleTasks(orders);
    }

    @Test
    void test() {
        TaskWave taskWave = scheduleService.getTaskWave();
        Arrays.stream(Station.values())
                .forEach(station -> System.out.println(station.tasksToString()));
    }
}
