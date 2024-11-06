package com.example.__2_IDLE.simulator;


import com.example.__2_IDLE.global.model.Customer;
import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.robot.RobotRepository;
import com.example.__2_IDLE.global.model.robot.RobotService;
import com.example.__2_IDLE.global.model.robot.RobotTaskAssignerRepository;
import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.task_allocator.TaskAllocateAlgorithm;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
import com.example.__2_IDLE.task_allocator.controller.RobotController;
import com.example.__2_IDLE.task_allocator.controller.StationController;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class SimulatorService {

    private static final int ORDER_NUM = 50;
    private final ScheduleModule scheduleModule = new ScheduleModule();
    private final RobotRepository robotRepository = new RobotRepository();
    private final RobotService robotService = new RobotService(robotRepository);
    private final RobotController robotController = new RobotController(robotService);
    private final StationController stationController = new StationController();
    private final TaskAllocateAlgorithm taskAllocateAlgorithm = new TaskAllocateAlgorithm(robotController, stationController);
    private final TaskAllocator taskAllocator = new TaskAllocator(taskAllocateAlgorithm, scheduleModule);


    public void run() {
        // ROS 객체 생성
        Ros ros = new Ros("localhost");

        // RobotRepository 초기화
        robotRepository.initRobotMap(ros);

        // 주문 생성 시작
        List<Order> orders = generateRandomOrders(ORDER_NUM); // 주문 50개 생성
//        printOrderList(orders);
        List<ScheduleTask> tasks = createScheduleTasks(orders);
        scheduleModule.addAllTask(tasks);

        processWaveSchedulingAndTaskAllocation(ros);
    }

    public static List<ScheduleTask> createScheduleTasks(List<Order> orders) {
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
        return tasks;
    }

    public static List<Order> generateRandomOrders(int numberOfOrders) {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();

        for (long i = 0; i < numberOfOrders; i++) {
            Customer customer = new Customer();

            List<Item> productList = new ArrayList<>();
            List<Item> items = List.of(Item.values());

            // TODO:맵 변경 후, 8로 변경 필요
            // 1~5개 랜덤하게 생성
            int numberOfProducts = random.nextInt(5) + 1;
            for (int j = 0; j < numberOfProducts; j++) {
                int randomIndex = random.nextInt(items.size());
                productList.add(items.get(randomIndex));
            }

            boolean oneDayShipping = random.nextBoolean();
            orders.add(Order.of(i + 1, customer, productList, oneDayShipping));
        }

        return orders;
    }

    private void processWaveSchedulingAndTaskAllocation(Ros ros) {
        while (!scheduleModule.isTaskQueueEmpty()) {
            if (scheduleModule.run()) {
                if (taskAllocator.start()) { // 한 wave에 대해 작업 할당
                    initializeTaskAssignersIfNeeded(ros);
                    RobotTaskAssignerRepository.startAllTaskAssigners();
                }
            }
        }
    }

    private void initializeTaskAssignersIfNeeded(Ros ros) {
        if (RobotTaskAssignerRepository.isTaskAssignerMapEmpty()) {
            RobotTaskAssignerRepository.init(ros, robotRepository.getAllRobots());
        }
    }

    private void printOrderList(List<Order> orders) {
        System.out.println("생성된 Order 출력 : ");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(orders.get(i).toString());
        }
    }
}
