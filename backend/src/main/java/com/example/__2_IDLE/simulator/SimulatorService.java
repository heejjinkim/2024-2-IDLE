package com.example.__2_IDLE.simulator;


import com.example.__2_IDLE.global.model.Customer;
import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.RobotNamespace;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.global.model.robot.RobotRepository;
import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.task.TaskModule;
import edu.wpi.rail.jrosbridge.Ros;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimulatorService {
    private final ScheduleModule scheduleModule = new ScheduleModule();
    private final RobotRepository robotRepository = new RobotRepository();

    private void printOrderList(List<Order> orders) {
        System.out.println("생성된 Order 출력 : ");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println(orders.get(i).toString());
        }
    }

    private void printRobotTaskQueues() {
        List<Robot> allRobots = robotRepository.getAllRobots();
        for (Robot robot : allRobots) {
            System.out.println(robot.getNamespace() + " 작업 큐:");
            if (robot.getTaskQueue().isEmpty()) {
                System.out.println(" - 작업 없음");
            } else {
                for (PickingTask task : robot.getTaskQueue()) {
                    System.out.print(" - 선반: " + task.getDestinations().getFirst());

                    if (task.getDestinations().size() > 2) {
                        System.out.print(" -> 피킹 스테이션: ");
                        for (int i = 1; i < task.getDestinations().size() - 1; i++) {
                            System.out.print(task.getDestinations().get(i));
                            if (i < task.getDestinations().size() - 2) {
                                System.out.print(", ");
                            }
                        }
                    }

                    System.out.println(" -> 선반: " + task.getDestinations().getLast());
                }
            }
        }
    }

    public void run() {
        // ROS 객체 생성
        Ros ros = new Ros("localhost");

        // 로봇 네임스페이스 목록 생성
        List<String> namespaces = List.of(RobotNamespace.values())
                .stream()
                .map(RobotNamespace::getNamespace)
                .collect(Collectors.toList());

        // RobotRepository 초기화
        robotRepository.initRobotMap(ros, namespaces);

        // 주문 생성기
        List<Order> orders = generateRandomOrders(10);
        printOrderList(orders);

        // 스케쥴링
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

        System.out.println("스케쥴링된 주문 순서");
        System.out.println("-------------------------------------------");
        scheduleModule.addTask(tasks);
        scheduleModule.run();

        System.out.println("로봇에게 작업 할당");
        System.out.println("-------------------------------------------");

        // TaskModule 생성
        TaskModule taskModule = new TaskModule(ros, robotRepository);

        // 물품 3개
        Item item1 = Item.ITEM_A;
        Item item2 = Item.ITEM_B;
        Item item3 = Item.ITEM_C;

        // 스테이션 3개
        Station station1 = Station.STATION_A;
        Station station2 = Station.STATION_B;
        Station station3 = Station.STATION_C;
        Station[] stations = {station1, station2, station3};
        Random random = new Random();

        // TODO: 랜덤 피키 스테이션 할당 -> 남아있는 피킹스테이션에 할당
        // 스케줄링 된 작업에 대해 작업 할당 수행
        for (ScheduleTask task : tasks) {
            Order order = task.getOrder();
            // 랜덤한 피킹 스테이션 할당
            Station randomStation = stations[random.nextInt(stations.length)];
            taskModule.taskAllocationWithHungarian(order, randomStation);
        }

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
            orders.add(Order.of(i + 1, customer, productList, oneDayShipping));
        }

        return orders;
    }
}
