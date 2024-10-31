package com.example.__2_IDLE;

import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.global.model.robot.RobotRepository;
import com.example.__2_IDLE.global.model.robot.RobotService;
import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.simulator.SimulatorService;
import com.example.__2_IDLE.task_allocator.TaskAllocateAlgorithm;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
import com.example.__2_IDLE.task_allocator.controller.RobotController;
import com.example.__2_IDLE.task_allocator.controller.StationController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TaskAllocatorTest {

    static final int NUMBER_OF_ORDERS = 100;

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

        ScheduleModule scheduleModule = new ScheduleModule();
        List<ScheduleTask> scheduleTasks = createScheduleTasks();
        scheduleModule.addAllTask(scheduleTasks);
        this.taskAllocator = new TaskAllocator(taskAllocateAlgorithm, scheduleModule);
    }

    private static List<ScheduleTask> createScheduleTasks() {
        List<Order> orders = SimulatorService.generateRandomOrders(NUMBER_OF_ORDERS);
        return SimulatorService.createScheduleTasks(orders);
    }

    private static TaskAllocateAlgorithm makeTaskAllocateAlgorithm() {
        RobotRepository robotRepository = new RobotRepository();
        RobotService robotService = new RobotService(robotRepository);

        StationController stationController = new StationController();
        RobotController robotController = new RobotController(robotService);

        addRobots(robotRepository);

        return new TaskAllocateAlgorithm(robotController, stationController);
    }

    private static void addRobots(RobotRepository robotRepository) {
        Robot robot1 = new Robot("/tb1", new Pose(0, 0));
        Robot robot2 = new Robot("/tb2", new Pose(0, 5));
        Robot robot3 = new Robot("/tb3", new Pose(0, 10));
        robotRepository.addRobot(robot1);
        robotRepository.addRobot(robot2);
        robotRepository.addRobot(robot3);
    }


}
