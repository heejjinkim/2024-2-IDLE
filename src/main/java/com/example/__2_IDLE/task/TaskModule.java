//package com.example.__2_IDLE.task;
//
//import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.NO_AVAILABLE_ROBOT;
//import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.UNAVAILABLE_COST;
//
//import com.example.__2_IDLE.global.exception.RestApiException;
//import com.example.__2_IDLE.global.model.Order;
//import com.example.__2_IDLE.global.model.enums.Item;
//import com.example.__2_IDLE.global.model.enums.Station;
//import com.example.__2_IDLE.global.model.Pose;
//import com.example.__2_IDLE.global.Robot;
//import com.example.__2_IDLE.robot_manager.robot.RobotContainer;
//import com.example.__2_IDLE.task.model.RobotTask;
//import java.util.LinkedList;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class TaskModule {
//
//    private final RobotContainer robotContainer;
//
//    public void taskAllocation(Order order, Station station) {
//        List<Item> orderItems = order.getOrderItems();
//
//        for (Item item : orderItems) {
//            RobotTask newTask = RobotTask.of(item, new LinkedList<>(List.of(station)));
//            Robot bestRobot = findBestRobot(newTask);
//
//            // 비용이 가장 작은 로봇의 작업 큐에 작업 삽입
//            if (bestRobot != null) {
//                bestRobot.getTaskQueue().add(newTask);
//            } else {
//                throw new RestApiException(NO_AVAILABLE_ROBOT);
//            }
//        }
//    }
//
//    private Robot findBestRobot(RobotTask newTask) {
//        // 현재 로봇의 작업 상태 받아오기
//        List<Robot> allRobots = robotContainer.getAllRobots();
//        Robot bestRobot = null;
//        double minCost = Double.MAX_VALUE;
//
//        // 한 task에 대해 각 로봇마다의 비용 계산
//        for (Robot robot : allRobots) {
//            int l = robot.getState().isWaiting() ? 1 : 2; // 로봇이 대기 중인지 확인
//            double cost = calculateCost(robot, newTask, l);
//
//            if (cost < minCost) {
//                minCost = cost;
//                bestRobot = robot;
//            }
//        }
//        return bestRobot;
//    }
//
//    private double calculateCost(Robot robot, RobotTask task, int l) {
//        double tPrime = 3; // 피킹 시간(3초라고 가정)
//        double vPrime = 1.0; // TODO: 로봇의 속도?
//
//        // 거리 계산
//        double distanceToShelf = calculateDistance(robot.getCurrentPose(), task.getDestinations().getFirst()); // 로봇과 선반 사이의 거리
//        double shelfToStationDistance = calculateShelfToStation(task); // 새로운 작업 선반과 피킹 스테이션 사이의 거리
//        double previousToNewShelfDistance = calculatePreviousShelfToNewShelf(robot, task); // 이전 작업 선반과 새로운 작업 선반 사이의 거리
//
//        // 작업 상관성 (같은 선반에 위치하면 1, 아니면 0)
//        int B = isTaskCorrelated(task, robot) ? 1 : 0;
//
//        // 비용 계산식 적용
//        double cost;
//        if (l == 1) { // Task가 하나인 경우
//            // 로봇위치→선반위치+2×(선반위치→피킹스테이션) / 로봇 속도 + 피킹 시간
//            cost = (distanceToShelf  + 2 * shelfToStationDistance ) / vPrime + tPrime;
//        } else if (l >= 2 && B == 0) { // Task가 두 개 이상이고 같은 선반이 아닌 경우
//            //(이전작업선반→새작업선반)+2×(새작업선반→피킹스테이션) / 로봇 속도 + 피킹 시간
//            cost = (previousToNewShelfDistance  + 2 * shelfToStationDistance ) / vPrime + tPrime;
//        } else if (l >= 2 && B == 1) { // Task가 두 개 이상이고 같은 선반인 경우
//            //(이전피킹스테이션→새로운작업선반)+(피킹스테이션→새작업선반)−(피킹스테이션→이전작업선반) / 로봇 속도 + 피킹 시간
//            cost = (calculateStationDistance(robot, task) + shelfToStationDistance  - previousToNewShelfDistance ) / vPrime + tPrime;
//        } else {
//            throw new RestApiException(UNAVAILABLE_COST);
//        }
//        return cost;
//    }
//
//    // 두 Pos 간의 거리 계산 (유클리드)
//    private double calculateDistance(Pose startPose, Pose endPose) {
//        return Math.sqrt(Math.pow(endPose.getX() - startPose.getX(), 2) + Math.pow(endPose.getY() - startPose.getY(), 2));
//    }
//
//    // 선반과 피킹 스테이션 사이의 거리
//    private double calculateShelfToStation(RobotTask task) {
//        return calculateDistance(task.getDestinations().get(0), task.getDestinations().get(1));
//    }
//
//    // 이전 작업의 선반과 새로운 작업 선반 간 거리 계산
//    private double calculatePreviousShelfToNewShelf(Robot robot, RobotTask currentTask) {
//        RobotTask previousTask = robot.getTaskQueue().getLast(); // 이전 작업
//        if (previousTask != null) {
//            Pose previousPose = previousTask.getDestinations().getFirst(); // 이전 작업의 선반 위치
//            Pose currentPose = currentTask.getDestinations().getFirst();   // 새로운 작업의 선반 위치
//
//            return calculateDistance(previousPose, currentPose);
//        }
//        return 0;
//    }
//
//    // 이전 작업과의 상관성 계산 (같은 선반인지 여부)
//    private boolean isTaskCorrelated(RobotTask task, Robot robot) {
//        RobotTask previousTask = robot.getTaskQueue().getLast(); // 이전 작업
//        if (previousTask != null) {
//            Pose previousPose = previousTask.getDestinations().getFirst(); // 이전 작업의 선반 위치
//            Pose currentPose = task.getDestinations().getFirst(); // 새로운 작업의 선반 위치
//
//            return previousPose.equals(currentPose);
//        }
//        return false;
//    }
//
//    // 로봇과 작업의 스테이션 간 거리 계산
//    private double calculateStationDistance(Robot robot, RobotTask task) {
//        return calculateDistance(robot.getCurrentPose(),
//            task.getDestinations().get(task.getDestinations().size() - 1));
//    }
//}
