package com.example.__2_IDLE.test_allo_sim.v2;

import com.example.__2_IDLE.test_allo_sim.v2.manager.*;
import com.example.__2_IDLE.test_allo_sim.v2.model.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Getter
@Setter
public class Simulator {

    private static final int ORDER_COUNT = 5;         // 생성할 주문의 수
    private static final int ITEM_TYPE_COUNT = 10;      // 물품 종류의 수
    private static final int ROBOT_COUNT = 3;           // 운용할 로봇 수
    private static final int STATION_COUNT = 3;         // 운용할 피킹 스테이션 수
    private static final int STATION_Y = 0;             // 피킹 스테이션이 위치할 y좌표
    private static final int ITEM_Y = 10;               // 물품 이 위치할 y좌표
    private static final int ROBOT_Y = 15;              // 로봇이 위치할 y좌표
    private static final int ROBOT_INTERVAL = 2;        // 로봇 사이 간격
    private static final int ITEM_INTERVAL = 1;         // 선반 사이의 간격
    private static final int STATION_INTERVAL = 2;      // 선반 사이 간격
    private static final int MAX_ITEM_PER_ORDER = 5;    // 주문 하나 당 최대 물품 수
    private static final int PICKING_TIME = 1;          // 피킹하는 데 걸리는 시간

    private ItemManager itemManager;
    private RobotManager robotManager;
    private TaskManager taskManager;
    private StationManager stationManager;
    private OrderManager orderManager;

    public Simulator() {
        setManagers();
        makeOrders(MAX_ITEM_PER_ORDER, ORDER_COUNT);
        setUnallocatedTasks();
    }

    private void setManagers() {
        itemManager = new ItemManager(ITEM_TYPE_COUNT, ITEM_INTERVAL, ITEM_Y);
        robotManager = new RobotManager(ROBOT_COUNT, ROBOT_INTERVAL, ROBOT_Y, itemManager);
        stationManager = new StationManager(STATION_COUNT, STATION_INTERVAL, STATION_Y, PICKING_TIME);
        taskManager = new TaskManager(ITEM_TYPE_COUNT);
        orderManager = new OrderManager(MAX_ITEM_PER_ORDER, ORDER_COUNT);
    }

    public void run() {
        allocateTaskStep();
        simulationStep();
    }

    private void simulationStep() {

    }

    private void allocateTaskStep() {
        log.info("start step1");
        step1();
        robotManager.printAllocatedTasks();
        while (true) {
            log.info("start step2");
            step2();
            robotManager.printAllocatedTasks();
            if (!stationManager.hasUnallocatedTask()) { // 모든 작업이 할당된 경우
                break;
            }
            log.info("start step3");
            step3();
            robotManager.printAllocatedTasks();
            if (!stationManager.hasUnallocatedTask()) { // 모든 작업이 할당된 경우
                break;
            }
        }
//        robotManager.printAllocatedTasks();
//        log.info("unallocated");
//        stationManager.printUnallocatedTasks();
        step4();
    }

    private void step1() { // 각 로봇에서 가장 가까운 작업을 작업으로 할당
        List<Robot> robotList = robotManager.getRobotList();
        for (Robot robot : robotList) {
            Pose robotPose = robot.getInitalPose();
            Task allocatedTask = null;
            allocatedTask = getAllocatedTask(robotPose, allocatedTask);
            allocateTask(robot, allocatedTask);
        }
    }

    private void step2() {
        List<Task> lastTaskList = robotManager.getLastTasks();
        List<Robot> robotList = robotManager.getRobotList(); // 각 로봇의 마지막 작업
        setLastTaskList(robotList, lastTaskList);
        findStrongCorrelatedTasks(robotList, lastTaskList);
        findWeakCorrelatedTasks(robotList, lastTaskList);
    }

    private void step3() {
        allocateMinCostTask();
    }

    private void allocateMinCostTask() {
        List<Robot> robotList = robotManager.getRobotList();
        for (Robot robot : robotList) {
            Task lastTask = robot.getLastTask();
            Task minCostTask = findMinCostTask(lastTask, robot); // todo 여기서 null 넘어오는 경우 있음
            allocateTask(robot, minCostTask);
        }
    }

    private Task findMinCostTask(Task robotLastTask, Robot robot) {
        Pose robotPose = robot.getInitalPose();
        if (robotLastTask == null) { // 첫 번째 작업인 경우
            log.info("첫 번째 작업인 경우");
            return whenFirstTask(robotPose);
        }
        log.info(" 두 번째 작업인 경우");
        return whenAfterSecondTask(robotLastTask);
    }

    private Task whenAfterSecondTask(Task robotLastTask) {
        Task targetTask = null;
        Station maxCompletionTimeStation = findStationByMaxCompletionTime(); // 피킹 시간이 가장 늦는 피킹 스테이션
        Map<Integer, String> maxCompletionTimeUnalloTasks = maxCompletionTimeStation.getUnallocatedTasks(); // 위 피킹 스테이션의 unallocated 집합
        int minTimeCost = Integer.MAX_VALUE;
        for (Integer taskId : maxCompletionTimeUnalloTasks.keySet()) {
            Task task = taskManager.getTaskById(taskId);
            if (robotLastTask.getItemName().equals(task.getItemName())) { // 같은 상품인 경우
                log.info("같은 상품일 경우");
                int timeCost = robotLastTask.getStation().getPose().calculateDistance(maxCompletionTimeStation.getPose());
                if (timeCost < minTimeCost) {
                    minTimeCost = timeCost;
                    targetTask = task;
                }
            } else { // 다른 상품인 경우
                log.info("다른 상품일 경우");
                Pose nextItemPose = itemManager.getPose(task.getItemName());
                int timeCost = calcTimeCost(robotLastTask, nextItemPose, maxCompletionTimeStation);
                if (timeCost < minTimeCost) {
                    minTimeCost = timeCost;
                    targetTask = task;
                }
            }
        }
        return targetTask;
    }

    private int calcTimeCost(Task robotLastTask, Pose nextItemPose, Station maxCompletionTimeStation) {
        int beforeItemToNextItem = robotLastTask.getStation().getPose().calculateDistance(nextItemPose);
        int nextItemToStation = nextItemPose.calculateDistance(maxCompletionTimeStation.getPose());
        int timeCost = beforeItemToNextItem + 2 * nextItemToStation;
        return timeCost;
    }

    private Task whenFirstTask(Pose robotPose) {
        Task targetTask = null;
        Station maxCompletionTimeStation = findStationByMaxCompletionTime();
        Map<Integer, String> maxCompletionTimeUnalloTasks = maxCompletionTimeStation.getUnallocatedTasks();
        int minTimeCost = Integer.MAX_VALUE;
        for (Integer taskId : maxCompletionTimeUnalloTasks.keySet()) {
            Task task = taskManager.getTaskById(taskId);
            String itemName = task.getItemName();
            Pose itemPose = itemManager.getPose(itemName);
            int robotToItemTime = robotPose.calculateDistance(itemPose);
            int itemToStationTime = itemPose.calculateDistance(maxCompletionTimeStation.getPose());
            int timeCost = robotToItemTime + 2 * itemToStationTime;
            if (timeCost < minTimeCost) {
                minTimeCost = timeCost;
                targetTask = task;
            }
        }
        return targetTask;
    }

    private Station findStationByMaxCompletionTime() {
        List<Station> stationList = stationManager.getStationList();
        List<Robot> robotList = robotManager.getRobotList();

        Station maxTimeCostStation = null;
        int maxTimeCost = 0;
        for (Station station : stationList) {
            int maxTotalTimeCost = 0;
            for (Robot robot : robotList) {
                int totalTimeCost = robot.getMaxCompletionTime(station);
                if (totalTimeCost > maxTotalTimeCost) {
                    maxTotalTimeCost = totalTimeCost;
                }
            }
           if (maxTotalTimeCost > maxTimeCost) {
               maxTimeCost = maxTotalTimeCost;
               maxTimeCostStation = station;
           }
        }
        return maxTimeCostStation;
    }

    private void step4() {}

    public void makeOrders(int maxItemsPerOrder, int orderCount) {
        for (int i = 0; i < orderCount; i++) {
            Order order = new Order(new ArrayList<>());
            addItem(i, maxItemsPerOrder, order);
            orderManager.addOrder(order);
        }
    }

    private void addItem(int i, int maxItemsPerOrder, Order order) {
        Random random = new Random();
        int itemCount = random.nextInt(1, maxItemsPerOrder + 1);
        for (int j = 0; j < itemCount; j++) {
            int randomItemName = random.nextInt(ITEM_TYPE_COUNT);
            Item item = new Item("item" + randomItemName);
            order.addItem(item);
            itemManager.addItem(item.getName(), new Pose(randomItemName * ITEM_INTERVAL, ITEM_Y));
        }
    }

    private void setUnallocatedTasks() {
        List<Order> orderList = orderManager.getOrderList();
        int taskId = 0;
        int stationIndex = 0; // order를 부여할 station(0~STATION_COUNT-1 순회)
        for (Order order : orderList) {
            List<Item> orderItemList = order.getItemList();
            Station station = stationManager.getStationByIndex(stationIndex);
            converseItemToTask(orderItemList, taskId, station);
            taskId += orderItemList.size();
            stationIndex = (stationIndex + 1) % STATION_COUNT;
        }
    }

    private void converseItemToTask(List<Item> orderItemList, int taskId, Station station) {
        for (Item item : orderItemList) {
            Task task = new Task(taskId++, item.getName(), null);
            station.addTaskToUnallocatedTasks(task);
            taskManager.addTask(task);
        }
    }

    private void allocateTask(Robot robot, Task allocatedTask) {
        robot.allocateTask(allocatedTask);
        stationManager.allocateTask(allocatedTask);
    }

    private Task getAllocatedTask(Pose robotPose, Task allocatedTask) {
        List<Task> taskList = taskManager.getTaskList();
        int minDistance = Integer.MAX_VALUE;

        for (Task task : taskList) {
            String taskItemName = task.getItemName();
            Pose itemPose = itemManager.getPose(taskItemName);
            int distance = robotPose.calculateDistance(itemPose);
            if (distance < minDistance) {
                minDistance = distance;
                allocatedTask = task;
            }
        }

        taskManager.allocateTask(allocatedTask);

        return allocatedTask;
    }

    private void findStrongCorrelatedTasks(List<Robot> robotList, List<Task> lastTaskList) {
        for (int i = 0; i < ROBOT_COUNT; i++) {
            Robot robot = robotList.get(i);      // i번째 로봇
            Task lastTask = lastTaskList.get(i); // i번째 로봇의 마지막 task
            Station lastTaskStation = lastTask.getStation();
            List<Integer> strongCorrelatedTaskIds = lastTaskStation.findStrongCorrelatedTasks(lastTask);
            List<Task> strongCorrelatedTasks = taskManager.getTaskById(strongCorrelatedTaskIds);
            for (Task task : strongCorrelatedTasks) {
                allocateTask(robot, task);
            }
        }
    }

    private void setLastTaskList(List<Robot> robotList, List<Task> lastTaskList) {
        for (Robot robot : robotList) {
            Task lastTask = robot.getLastTask();
            lastTaskList.add(lastTask);
        }
    }

    private void findWeakCorrelatedTasks(List<Robot> robotList, List<Task> lastTaskList) {
        for (int i = 0; i < ROBOT_COUNT; i++) {
            Robot robot = robotList.get(i);
            Task lastTask = lastTaskList.get(i);
            List<Task> allocatedTasks = compareIsSameItem(lastTask);
            for (Task task : allocatedTasks) {
                taskManager.allocateTask(task);
                allocateTask(robot, task);
            }
        }
    }

    private List<Task> compareIsSameItem(Task lastTask) {
        List<Task> taskList = taskManager.getTaskList();
        List<Task> allocatedTask = new ArrayList<>();
        for (Task task : taskList) {
            String lastTaskItemName = lastTask.getItemName();
            if (lastTaskItemName.equals(task.getItemName())) {
                allocatedTask.add(task);
            }
        }
        return allocatedTask;
    }
}
