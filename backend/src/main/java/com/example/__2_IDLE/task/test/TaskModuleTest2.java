//package com.example.__2_IDLE.task.test;
//
//import com.example.__2_IDLE.global.model.Customer;
//import com.example.__2_IDLE.global.model.Order;
//import com.example.__2_IDLE.global.model.enums.Item;
//import com.example.__2_IDLE.global.model.enums.Shelf;
//import com.example.__2_IDLE.global.model.enums.Station;
//import com.example.__2_IDLE.global.model.Pose;
//import com.example.__2_IDLE.global.model.robot.Robot;
//import com.example.__2_IDLE.global.model.robot.RobotRepository;
//import com.example.__2_IDLE.robot.state.CarryState;
//import com.example.__2_IDLE.task.TaskModule;
//import com.example.__2_IDLE.task.model.RobotTask;
//import java.util.LinkedList;
//import java.util.List;
//
//public class TaskModuleTest2 {
//
//    public static void main(String[] args) {
//        // 로봇 3대 생성
//        Robot robot1 = new Robot("Robot1", new Pose(5, 5));
//        Robot robot2 = new Robot("Robot2", new Pose(10, 5));
//        Robot robot3 = new Robot("Robot3", new Pose(15, 5));
//        robot3.setState(new CarryState());
//        robot3.setShelf(Shelf.SHELF_A);
//        robot3.setTaskQueue(new LinkedList<>(
//            List.of(RobotTask.of(Item.ITEM_A, new LinkedList<>(List.of(Station.STATION_A))))));
//
//        // 로봇 컨테이너에 로봇 추가
//        RobotRepository robotRepository = new RobotRepository();
//        robotRepository.addRobot(robot1);
//        robotRepository.addRobot(robot2);
//        robotRepository.addRobot(robot3);
//
//        // TaskModule 생성
//        TaskModule taskModule = new TaskModule(robotRepository);
//
//        // 물품 3개 생성
//        Item item1 = Item.ITEM_A;
//        Item item2 = Item.ITEM_B;
//        Item item3 = Item.ITEM_C;
//
//        // 주문 생성
//        Order order = Order.builder()
//            .id(1L)
//            .customer(new Customer())
//            .orderItems(List.of(item1, item2, item3))
//            .isSameDayDelivery(false)
//            .build();
//
//        // 피킹 스테이션 A를 선택
//        Station station = Station.STATION_A;
//
//        // TaskModule로 작업 할당
//        taskModule.taskAllocationWithHungarian(order, station);
//
//        printRobotTaskQueues(robotRepository);
//    }
//
//    private static void printRobotTaskQueues(RobotRepository robotRepository) {
//        List<Robot> allRobots = robotRepository.getAllRobots();
//        for (Robot robot : allRobots) {
//            System.out.println(robot.getNamespace() + " 작업 큐:");
//            if (robot.getTaskQueue().isEmpty()) {
//                System.out.println(" - 작업 없음");
//            } else {
//                for (RobotTask task : robot.getTaskQueue()) {
//                    System.out.println(
//                        " - Task for item: " + task.getDestinations().getFirst() + " -> 피킹 스테이션: "
//                            + task.getDestinations().getLast());
//                }
//            }
//            System.out.println("현재 상태: " + robot.getState().stateName());
//            System.out.println(
//                "현재 작업: " + (robot.getRobotTask() != null ? robot.getRobotTask().getDestinations()
//                    .getFirst() : "없음"));
//        }
//    }
//}
