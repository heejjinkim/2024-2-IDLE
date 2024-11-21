package com.example.__2_IDLE;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.order.OrderRepository;
import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.schedule.ScheduleRepository;
import com.example.__2_IDLE.schedule.ScheduleService;
import com.example.__2_IDLE.task_allocator.StationService;
import com.example.__2_IDLE.task_allocator.TaskAllocateAlgorithm;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.Collectors;

public class TaskAllocatorTest {

    private Robot robot1;
    private Robot robot2;
    private Robot robot3;
    private static final String ROBOT1_NAME = "/tb1";
    private static final String ROBOT2_NAME = "/tb2";
    private static final String ROBOT3_NAME = "/tb3";
    private static final int TEST_COUNT = 1;
    private static Map<String, List<Integer>> totalTaskCount = new HashMap<>();
    private final ScheduleService scheduleService = new ScheduleService(new ScheduleRepository());
    private TaskAllocator taskAllocator;

    @BeforeAll
    static void beforeAll() {
        totalTaskCount.put(ROBOT1_NAME, new ArrayList<>());
        totalTaskCount.put(ROBOT2_NAME, new ArrayList<>());
        totalTaskCount.put(ROBOT3_NAME, new ArrayList<>());
    }

    @BeforeEach
    void beforeEach() {
        OrderService orderService = new OrderService(new OrderRepository());

        List<Order> orders = orderService.generateRandomOrders();
        List<List<Item>> listOfLists = orders.stream()
                .map(Order::getOrderItems)
                .collect(Collectors.toList());

        // 중첩 리스트를 하나의 리스트로 평탄화
        List<String> flatList = listOfLists.stream()
                .flatMap(List::stream)
                .map(item -> item.getName())
                .collect(Collectors.toList());

        System.out.println("생성된 모든 주문=" + String.join(", ", flatList));

        scheduleService.createScheduleTasks(orders);

        StationService stationService = new StationService(orderService);
        TaskAllocateAlgorithm taskAllocateAlgorithm = new TaskAllocateAlgorithm(stationService);
        taskAllocator = new TaskAllocator(taskAllocateAlgorithm);
    }

    @RepeatedTest(TEST_COUNT)
    void distinctFirstRobotTaskTest() {
        setRobots();
        runTaskAllocator();
        assertDistinctFirstRobotTask();

        LinkedList<PickingTask> tb1TaskQueue = robot1.getTaskQueue();
        LinkedList<PickingTask> tb2TaskQueue = robot2.getTaskQueue();
        LinkedList<PickingTask> tb3TaskQueue = robot3.getTaskQueue();

        List<String> tb1ItemNames = tb1TaskQueue.stream()
                .map(task -> task.getItem().getName())
                .toList();
        List<String> tb2ItemNames = tb2TaskQueue.stream()
                .map(task -> task.getItem().getName())
                .toList();
        List<String> tb3ItemNames = tb3TaskQueue.stream()
                .map(task -> task.getItem().getName())
                .toList();

        System.out.println("tb1에 할당된 item: " + String.join(",", tb1ItemNames));
        System.out.println("tb2에 할당된 item: " + String.join(",", tb2ItemNames));
        System.out.println("tb3에 할당된 item: " + String.join(",", tb3ItemNames));


        totalTaskCount.get(ROBOT1_NAME).add(robot1.getTaskQueue().size());
        totalTaskCount.get(ROBOT2_NAME).add(robot2.getTaskQueue().size());
        totalTaskCount.get(ROBOT3_NAME).add(robot3.getTaskQueue().size());
    }

    private void assertDistinctFirstRobotTask() {
        Item firstItem1 = robot1.getFirstTask().getItem();
        Item firstItem2 = robot2.getFirstTask().getItem();
        Item firstItem3 = robot3.getFirstTask().getItem();

        Set<Item> itemSet = new HashSet<>();
        itemSet.add(firstItem1);
        itemSet.add(firstItem2);
        itemSet.add(firstItem3);

        Assertions.assertEquals(3, itemSet.size());
    }

    private void runTaskAllocator() {
        Map<String, Robot> allRobots = Map.of(ROBOT1_NAME, robot1, ROBOT2_NAME, robot2, ROBOT3_NAME, robot3);
        TaskWave taskWave = scheduleService.getTaskWave();

        taskAllocator.initAlgorithm(taskWave, allRobots);
        taskAllocator.start();
    }

    private void setRobots() {
        robot1 = new Robot(ROBOT1_NAME, new Pose(0, 0));
        robot2 = new Robot(ROBOT2_NAME, new Pose(0, 5));
        robot3 = new Robot(ROBOT3_NAME, new Pose(0, 10));
    }

    @AfterAll
    static void printAvgTaskCount() {
        List<Double> standardDeviations = new ArrayList<>();
        List<Integer> tb1TotalTaskCount = totalTaskCount.get(ROBOT1_NAME);
        List<Integer> tb2TotalTaskCount = totalTaskCount.get(ROBOT2_NAME);
        List<Integer> tb3TotalTaskCount = totalTaskCount.get(ROBOT3_NAME);

        for (int i = 0; i < tb1TotalTaskCount.size(); i++) {
            int tb1TaskCount = tb1TotalTaskCount.get(i);
            int tb2TaskCount = tb2TotalTaskCount.get(i);
            int tb3TaskCount = tb3TotalTaskCount.get(i);

            double avgTaskCount = ((double) (tb1TaskCount + tb2TaskCount + tb3TaskCount)) / 3;

            double variance1 = tb1TaskCount - avgTaskCount;
            double variance2 = tb2TaskCount - avgTaskCount;
            double variance3 = tb3TaskCount - avgTaskCount;

            double standardDeviation = Math.sqrt(
                            calcPow(variance1, 2)
                            + calcPow(variance2, 2)
                            + calcPow(variance3, 2)
            );
            standardDeviations.add(standardDeviation);
        }

        double sumOfStandardDeviation = standardDeviations.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double avgStandardDeviation = sumOfStandardDeviation / TEST_COUNT;

        int avgTb1TaskCount = tb1TotalTaskCount.stream()
                .mapToInt(Integer::intValue)
                .sum();
        int avgTb2TaskCount = tb2TotalTaskCount.stream()
                .mapToInt(Integer::intValue)
                .sum();
        int avgTb3TaskCount = tb3TotalTaskCount.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("로봇1에 할당된 평균 작업 수=" + avgTb1TaskCount / TEST_COUNT);
        System.out.println("로봇2에 할당된 평균 작업 수=" + avgTb2TaskCount / TEST_COUNT);
        System.out.println("로봇3에 할당된 평균 작업 수=" + avgTb3TaskCount / TEST_COUNT);
        System.out.println("평균 표준 편차=" + avgStandardDeviation);
    }

    static double calcPow(double value, int ex) {
        return Math.pow(value, ex);
    }
}
