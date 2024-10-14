package com.example.__2_IDLE.simulator;


import com.example.__2_IDLE.global.model.Customer;
import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.robot_manager.robot.Robot;
import com.example.__2_IDLE.robot_manager.robot.RobotContainer;
import com.example.__2_IDLE.robot_manager.state.CarryState;
import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.task.TaskModule;
import com.example.__2_IDLE.task.model.RobotTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SimulatorService {
    private final ScheduleModule scheduleModule = new ScheduleModule();
    private final RobotContainer robotContainer = new RobotContainer();
    private void printOrderList(List<Order> orders){
        System.out.println("생성된 Order 출력 : ");
        System.out.println("-------------------------------------------");
        for(int i = 0 ; i < orders.size() ; i++){
            System.out.println(orders.get(i).toString());
        }
    }

    private void printRobotTaskQueues() {
        List<Robot> allRobots = robotContainer.getAllRobots();
        for (Robot robot : allRobots) {
            System.out.println(robot.getNamespace() + " 작업 큐:");
            if (robot.getTaskQueue().isEmpty()) {
                System.out.println(" - 작업 없음");
            } else {
                for (RobotTask task : robot.getTaskQueue()) {
                    System.out.println(
                            " - Task for item: " + task.getDestinations().getFirst() + " -> 피킹 스테이션: "
                                    + task.getDestinations().getLast());
                }
            }
            System.out.println("현재 상태: " + robot.getState().stateName());
            System.out.println(
                    "현재 작업: " + (robot.getRobotTask() != null ? robot.getRobotTask().getDestinations()
                            .getFirst() : "없음"));
        }
    }

    public void run(){
        // 주문 생성기
        List<Order> orders = generateRandomOrders(10);
        printOrderList(orders);

        // 스케쥴링
        List<ScheduleTask> tasks = new ArrayList<>();
        for(int i = 0 ; i < orders.size() ; i++){
            ScheduleTask task = new ScheduleTask();
            task.setId(i);
            if(orders.get(i).isSameDayDelivery()){
                task.setUrgency(1);
            }
            task.setCreateTime(LocalDateTime.now());
            task.setOrder(orders.get(i));
            tasks.add(task);
        }

        System.out.println("스케쥴링된 주문 순서");
        System.out.println("-------------------------------------------");
        scheduleModule.addTask(tasks);
        scheduleModule.run();

        System.out.println("로봇에게 작업 할당");
        System.out.println("-------------------------------------------");
        Robot robot1 = new Robot("Robot1", new Pose(5, 5));
        Robot robot2 = new Robot("Robot2", new Pose(10, 5));
        Robot robot3 = new Robot("Robot3", new Pose(15, 5));

        // ??
//        robot3.setState(new CarryState());
//        robot3.setShelf(Shelf.SHELF_A);
//        robot3.setTaskQueue(new LinkedList<>(
//                List.of(RobotTask.of(Item.ITEM_A, new LinkedList<>(List.of(Station.STATION_A))))));

        // 로봇 컨테이너에 로봇 추가
        robotContainer.addRobot(robot1);
        robotContainer.addRobot(robot2);
        robotContainer.addRobot(robot3);

        // TaskModule 생성
        TaskModule taskModule = new TaskModule(robotContainer);

        // 물품 3개 생성
        Item item1 = Item.ITEM_A;
        Item item2 = Item.ITEM_B;
        Item item3 = Item.ITEM_C;

        // 주문 생성
        Order order = Order.builder()
                .id(1L)
                .customer(new Customer())
                .orderItems(List.of(item1, item2, item3))
                .isSameDayDelivery(false)
                .build();

        // 피킹 스테이션 A를 선택
        Station station = Station.STATION_A;

        // TaskModule로 작업 할당
        taskModule.taskAllocationWithHungarian(order, station);

        printRobotTaskQueues();

    }
    public static List<Order> generateRandomOrders(int numberOfOrders) {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();

        for (long i = 0; i < numberOfOrders; i++) {
            Customer customer = new Customer();

            List<Item> productList = new ArrayList<>();
            List<Item> items = List.of(Item.values());

            // 5개 랜덤하게 생성
            int numberOfProducts = random.nextInt(5) + 1;
            for (int j = 0; j < numberOfProducts; j++) {
                int randomIndex = random.nextInt(items.size());
                productList.add(items.get(randomIndex));
            }

            boolean oneDayShipping = random.nextBoolean();
            orders.add(Order.of(i+1, customer, productList, oneDayShipping));
        }

        return orders;
    }
}
