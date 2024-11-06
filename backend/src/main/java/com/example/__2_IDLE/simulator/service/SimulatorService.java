package com.example.__2_IDLE.simulator.service;


import com.example.__2_IDLE.global.exception.RestApiException;
import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.robot.RobotService;
import com.example.__2_IDLE.robot.RobotTaskAssignerRepository;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.schedule.ScheduleService;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
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
    private final Ros ros = new Ros("localhost");

    public void run() {
        isRunning = true;
        robotService.initRobotMap(ros);
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
                if (taskAllocator.start()) {
                    initializeTaskAssignersIfNeeded();
                    RobotTaskAssignerRepository.startAllTaskAssigners();
                }
            }
        }
    }

    private void initializeTaskAssignersIfNeeded() {
        if (RobotTaskAssignerRepository.isTaskAssignerMapEmpty()) {
            List<Robot> robotList = new ArrayList<>(robotService.getAllRobots().values());
            RobotTaskAssignerRepository.init(ros, robotList);
        }
    }

    private void printRobotTaskQueueSize() {
        Map<String, Robot> allRobots = robotService.getAllRobots();
        allRobots.forEach((namespace, robot) ->
                System.out.println("robot " + namespace + " taskQueue size = " + robot.getTaskQueue().size())
        );
    }
}
