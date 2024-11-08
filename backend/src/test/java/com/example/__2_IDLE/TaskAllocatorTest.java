package com.example.__2_IDLE;

import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.robot.RobotMapRepository;
import com.example.__2_IDLE.robot.RobotService;
import com.example.__2_IDLE.schedule.ScheduleRepository;
import com.example.__2_IDLE.schedule.ScheduleService;
import com.example.__2_IDLE.task_allocator.TaskAllocateAlgorithm;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
import com.example.__2_IDLE.task_allocator.controller.RobotController;
import com.example.__2_IDLE.task_allocator.StationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TaskAllocatorTest {

    TaskAllocator taskAllocator;
    TaskAllocateAlgorithm taskAllocateAlgorithm;

    @Test
    void taskAllocatorStartTest() {
        taskAllocator.start();
        Assertions.assertTrue(taskAllocateAlgorithm.isDone());
    }

    @BeforeEach
    void initField() {
        this.taskAllocateAlgorithm = makeTaskAllocateAlgorithm();

        ScheduleRepository scheduleRepository = new ScheduleRepository();
        ScheduleService scheduleService = new ScheduleService(scheduleRepository);
        OrderService orderService = new OrderService();

        List<Order> orders = orderService.generateRandomOrders();
        scheduleService.createScheduleTasks(orders);

        this.taskAllocator = new TaskAllocator(taskAllocateAlgorithm, scheduleService);
    }

    private static TaskAllocateAlgorithm makeTaskAllocateAlgorithm() {
        RobotMapRepository robotMapRepository = new RobotMapRepository();
        RobotService robotService = new RobotService(robotMapRepository);

        StationService stationService = new StationService();
        RobotController robotController = new RobotController(robotService);

        addRobots(robotMapRepository);

        return new TaskAllocateAlgorithm(robotController, stationService);
    }

    private static void addRobots(RobotMapRepository robotMapRepository) {
        Robot robot1 = new Robot("/tb1", new Pose(0, 0));
        Robot robot2 = new Robot("/tb2", new Pose(0, 5));
        Robot robot3 = new Robot("/tb3", new Pose(0, 10));
        robotMapRepository.addRobot(robot1);
        robotMapRepository.addRobot(robot2);
        robotMapRepository.addRobot(robot3);
    }


}
