package com.example.__2_IDLE.simulator.service;


import com.example.__2_IDLE.global.exception.RestApiException;
import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.robot.RobotService;
import com.example.__2_IDLE.robot.RobotTaskAssignerRepository;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.schedule.ScheduleService;
import com.example.__2_IDLE.task_allocator.StationService;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.SIMULATOR_NOT_RUNNING;

@RequiredArgsConstructor
@Service
@Slf4j
public class SimulatorService {

    private boolean isRunning = false;
    private final ScheduleService scheduleService;
    private final RobotService robotService;
    private final OrderService orderService;
    private final TaskAllocator taskAllocator;
    private final StationService stationService;
    private final RobotTaskAssignerRepository robotTaskAssignerRepository;
    private final Ros ros;

    public void run() {
        isRunning = true;
        addRandomOrders();
    }

    public void addRandomOrders() {
        if (!isRunning) {
            throw new RestApiException(SIMULATOR_NOT_RUNNING);
        }

        List<Order> orders = orderService.generateRandomOrders();
        scheduleService.createScheduleTasks(orders);

        processWaveSchedulingAndTaskAllocation();
    }

    private void processWaveSchedulingAndTaskAllocation() {
        while (!scheduleService.isTaskQueueEmpty()) {
            if (scheduleService.run()) {
                TaskWave taskWave = scheduleService.getTaskWave();
                Map<String, Robot> allRobots = robotService.getAllRobots();
                taskAllocator.initAlgorithm(taskWave, allRobots);
                if (taskAllocator.start()) {
                    initializeTaskAssignersIfNeeded();
                    robotTaskAssignerRepository.startAllTaskAssigners();
                }
            }
        }
    }

    private void initializeTaskAssignersIfNeeded() {
        if (robotTaskAssignerRepository.isTaskAssignerMapEmpty()) {
            List<Robot> robotList = new ArrayList<>(robotService.getAllRobots().values());
            robotTaskAssignerRepository.init(ros, robotList, stationService, orderService);
        }
    }

    private void printRobotTaskQueueSize() {
        Map<String, Robot> allRobots = robotService.getAllRobots();
        allRobots.forEach((namespace, robot) ->
                System.out.println("robot " + namespace + " taskQueue size = " + robot.getTaskQueue().size())
        );
    }
}
