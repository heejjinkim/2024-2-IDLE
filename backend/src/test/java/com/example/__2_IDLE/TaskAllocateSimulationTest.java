package com.example.__2_IDLE;

import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.global.model.robot.RobotRepository;
import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.simulator.SimulatorService;
import com.example.__2_IDLE.task_allocator.TaskAllocateAlgorithm;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
import com.example.__2_IDLE.task_allocator.controller.StationController;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TaskAllocateSimulationTest {

    static final int NUMBER_OF_ORDERS = 10;

    TaskAllocator taskAllocator;
    TaskAllocateAlgorithm taskAllocateAlgorithm;

    @Test
    void taskAllocatorStartTest() {
        int testCount = 0;
        double totalAlgorithmTime = 0;
        double totalCommonTime = 0;
        while (testCount < 1000) {
            this.taskAllocateAlgorithm = makeTaskAllocateAlgorithm();

            ScheduleModule scheduleModule = new ScheduleModule();
            List<ScheduleTask> scheduleTasks = createScheduleTasks();
            scheduleModule.addAllTask(scheduleTasks);
            this.taskAllocator = new TaskAllocator(taskAllocateAlgorithm, scheduleModule);

            taskAllocator.start();
            RobotRepository robotRepository = taskAllocateAlgorithm.getRobotRepository();
            StationController stationController = taskAllocateAlgorithm.getStationController();
            Map<String, Robot> allRobots = robotRepository.getAllRobots();

            System.out.println("작업할당 알고리즘 적용");
            double algorithmTime = allRobots.values().stream()
                    .mapToDouble(robot -> robot.doTask(stationController))
                    .sum();
            allRobots.values().forEach(Robot::clearTaskQueue);

            System.out.println();
            System.out.println("작업할당 알고리즘 미적용");
            List<PickingTask> wave = taskAllocateAlgorithm.getTaskWave().getWave();
            int robotIndex = 1;
            for (PickingTask task : wave) {
                Optional<Robot> robot = robotRepository.getRobot("/tb" + robotIndex);
                robot.get().addTask(task);
                robotIndex += 1;
                if (robotIndex == 4) {
                    robotIndex = 1;
                }
            }
            double commonTime = allRobots.values().stream()
                    .mapToDouble(robot -> robot.doTask(stationController))
                    .sum();
            allRobots.values().forEach(Robot::clearTaskQueue);

            totalAlgorithmTime += algorithmTime;
            totalCommonTime += commonTime;
            testCount += 1;
        }

        System.out.println(totalAlgorithmTime / totalCommonTime);
    }

    @BeforeEach
    void initField() {

    }

    private static List<ScheduleTask> createScheduleTasks() {
        List<Order> orders = SimulatorService.generateRandomOrders(NUMBER_OF_ORDERS);
        return SimulatorService.createScheduleTasks(orders);
    }

    private static TaskAllocateAlgorithm makeTaskAllocateAlgorithm() {
        RobotRepository robotRepository = new RobotRepository();
        StationController stationController = new StationController();
        addRobots(robotRepository);
        return new TaskAllocateAlgorithm(robotRepository, stationController);
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
