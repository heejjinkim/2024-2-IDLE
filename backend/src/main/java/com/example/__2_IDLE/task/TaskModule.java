package com.example.__2_IDLE.task;

import com.example.__2_IDLE.global.exception.RestApiException;
import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.global.model.robot.RobotRepository;
import com.example.__2_IDLE.task.allocator.TaskAllocator;
import com.example.__2_IDLE.task.model.HungarianAlgorithm;
import com.example.__2_IDLE.task.model.RobotTask;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.NO_AVAILABLE_ROBOT;
import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.UNAVAILABLE_COST;

@Slf4j
@RequiredArgsConstructor
public class TaskModule {

    private final Ros ros;
    private final RobotRepository robotRepository;
    private final Map<String, TaskAllocator> taskAllocatorMap = new HashMap<>(); // 로봇 이름: TaskAllocator 맵

    // 선행 논문과 동일하게 큐의 마지막 작업과 새로운 작업 상관성만 고려
    public void taskAllocation(Order order, Station station) {
        List<Item> orderItems = order.getOrderItems();

        for (Item item : orderItems) {
            RobotTask newTask = RobotTask.of(item, new LinkedList<>(List.of(station)));
            log.info("new task item: {}", item.getName());
            Robot bestRobot = findBestRobot(newTask);
            log.info("best robot: {}", bestRobot.getNamespace());

            // 비용이 가장 작은 로봇의 작업 큐에 작업 삽입
            if (bestRobot != null) {
                // TODO: robotContainer를 통해 새로운 작업 add. 만약 로봇 상태 wait이었으면 전환하고, 실제 로봇 움직이게
                bestRobot.getTaskQueue().add(newTask);
            } else {
                throw new RestApiException(NO_AVAILABLE_ROBOT);
            }
        }
    }

    // 한 로봇에 작업이 몰리는 현상을 해결하기 위해 헝가리안 알고리즘 적용
    public void taskAllocationWithHungarian(Order order, Station station) {
        List<Item> orderItems = order.getOrderItems();
        List<Robot> allRobots = robotRepository.getAllRobots();
        int n = allRobots.size(); // 로봇 수
        int m = orderItems.size(); // 작업 수

        int startIndex = 0;

        // 여러 번의 매트릭스 처리를 위해 작업 분할
        while (startIndex < m) {
            // 이번 라운드에서 처리할 작업의 개수를 결정 (로봇의 수 또는 남은 작업 수 중 작은 값)
            int taskCount = Math.min(n, m - startIndex);
            int max = Math.max(n, taskCount); // 정방 행렬을 위한 최대 값
            int[][] costMatrix = new int[max][max]; // 정방 행렬 생성

            // 비용 행렬 생성 (정수로 변환)
            for (int i = 0; i < n; i++) {
                Robot robot = allRobots.get(i);
                for (int j = 0; j < taskCount; j++) {
                    RobotTask newTask = RobotTask.of(orderItems.get(startIndex + j), new LinkedList<>(List.of(station)));
                    int l = robot.getTaskQueue().isEmpty() ? 1 : 2;
                    costMatrix[i][j] = (int) (calculateCost(robot, newTask, l) * 100); // 정수로 스케일링
                }
                // 더미 작업에 높은 비용 할당
                for (int j = taskCount; j < max; j++) {
                    costMatrix[i][j] = Integer.MAX_VALUE; // 더미 작업
                }
            }

            // 더미 로봇을 위한 행 추가
            for (int i = n; i < max; i++) {
                for (int j = 0; j < max; j++) {
                    costMatrix[i][j] = Integer.MAX_VALUE; // 더미 로봇의 비용
                }
            }

            // 헝가리안 알고리즘 적용
            HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(costMatrix);
            int[][] result = hungarianAlgorithm.findOptimalAssignment();

            // 최종 작업 할당
            for (int[] assignment : result) {
                int robotIndex = assignment[0];
                int taskIndex = assignment[1];

                if (taskIndex < taskCount && robotIndex < n) { // 더미 작업 무시
                    Robot bestRobot = allRobots.get(robotIndex);
                    RobotTask newTask = RobotTask.of(orderItems.get(startIndex + taskIndex), new LinkedList<>(List.of(station)));

                    if (!bestRobot.getTaskQueue().isEmpty()) {
                        RobotTask lastTask = bestRobot.getTaskQueue().getLast();
                        // 로봇의 마지막 작업과 새로운 작업의 아이템이 동일한지 확인
                        if (lastTask.getItem().equals(newTask.getItem())) {
                            // 새로운 작업의 피킹 스테이션을 이전 작업의 마지막 목적지에 추가
                            lastTask.getDestinations().addLast(newTask.getDestinations().get(1));
                            continue;
                        }
                    }

                    bestRobot.getTaskQueue().add(newTask);

                    // TODO: TaskAllocator 생성 및 첫 번째 작업을 전송
                    // 로봇의 첫 작업인 경우에만 TaskAllocator를 생성하고 시작
                    if (!taskAllocatorMap.containsKey(bestRobot.getNamespace())) {
                        TaskAllocator taskAllocator = new TaskAllocator(bestRobot, ros);
                        taskAllocatorMap.put(bestRobot.getNamespace(), taskAllocator);
                        taskAllocator.startAllocate();
                        log.info("TaskAllocator 생성 및 시작: {}", bestRobot.getNamespace());
                    }

                    log.info("new task item: {}", orderItems.get(startIndex + taskIndex).getName());
                    log.info("best robot: {}", bestRobot.getNamespace());
                }
            }

            // 다음 작업을 처리하기 위해 시작 인덱스 업데이트
            startIndex += taskCount;
        }
    }

    private Robot findBestRobot(RobotTask newTask) {
        // 현재 로봇의 작업 상태 받아오기
        List<Robot> allRobots = robotRepository.getAllRobots();
        Robot bestRobot = null;
        double minCost = Double.MAX_VALUE;

        // 한 task에 대해 각 로봇마다의 비용 계산
        for (Robot robot : allRobots) {
            int l = robot.getTaskQueue().isEmpty() ? 1 : 2; // 로봇이 대기 중인지 확인
            double cost = calculateCost(robot, newTask, l);

            if (cost < minCost) {
                minCost = cost;
                bestRobot = robot;
            }
        }
        return bestRobot;
    }

    private double calculateCost(Robot robot, RobotTask task, int l) {
        double tPrime = 3; // 피킹 시간(3초라고 가정)
        double vPrime = 1.0; // TODO: 로봇의 속도?

        // 거리 계산
        double distanceToShelf = calculateDistance(robot.getCurrentPose(), task.getDestinations().getFirst()); // 로봇과 선반 사이의 거리
        double shelfToStationDistance = calculateShelfToStation(task); // 새로운 작업 선반과 피킹 스테이션 사이의 거리
        double previousToNewShelfDistance = calculatePreviousShelfToNewShelf(robot, task); // 이전 작업 선반과 새로운 작업 선반 사이의 거리

        // 작업 상관성 (같은 선반에 위치하면 1, 아니면 0)
        int B = isTaskCorrelated(task, robot) ? 1 : 0;

        // 비용 계산식 적용
        double cost;
        if (l == 1) { // Task가 하나인 경우
            // 로봇위치→선반위치+2×(선반위치→피킹스테이션) / 로봇 속도 + 피킹 시간
            cost = (distanceToShelf + 2 * shelfToStationDistance) / vPrime + tPrime;
        } else if (l >= 2 && B == 0) { // Task가 두 개 이상이고 같은 선반이 아닌 경우
            //(이전작업선반→새작업선반)+2×(새작업선반→피킹스테이션) / 로봇 속도 + 피킹 시간
            cost = (previousToNewShelfDistance + 2 * shelfToStationDistance) / vPrime + tPrime;
        } else if (l >= 2 && B == 1) { // Task가 두 개 이상이고 같은 선반인 경우
            //(이전피킹스테이션→새로운작업선반)+(피킹스테이션→새작업선반)−(피킹스테이션→이전작업선반) / 로봇 속도 + 피킹 시간
            cost = (calculateStationDistance(robot, task) + shelfToStationDistance - previousToNewShelfDistance) / vPrime + tPrime;
        } else {
            throw new RestApiException(UNAVAILABLE_COST);
        }
        return cost;
    }

    // 두 Pos 간의 거리 계산 (유클리드)
    private double calculateDistance(Pose startPose, Pose endPose) {
        return Math.sqrt(Math.pow(endPose.getX() - startPose.getX(), 2) + Math.pow(endPose.getY() - startPose.getY(), 2));
    }

    // 선반과 피킹 스테이션 사이의 거리
    private double calculateShelfToStation(RobotTask task) {
        return calculateDistance(task.getDestinations().get(0), task.getDestinations().get(1));
    }

    // 이전 작업의 선반과 새로운 작업 선반 간 거리 계산
    private double calculatePreviousShelfToNewShelf(Robot robot, RobotTask currentTask) {
        if (!robot.getTaskQueue().isEmpty()) {
            RobotTask previousTask = robot.getTaskQueue().getLast(); // 이전 작업
            Pose previousPos = previousTask.getDestinations().getFirst(); // 이전 작업의 선반 위치
            Pose currentPos = currentTask.getDestinations().getFirst();   // 새로운 작업의 선반 위치


            return calculateDistance(previousPos, currentPos);
        }
        return 0;
    }

    // 이전 작업과의 상관성 계산 (같은 선반인지 여부)
    private boolean isTaskCorrelated(RobotTask task, Robot robot) {
        if (!robot.getTaskQueue().isEmpty()) {
            RobotTask previousTask = robot.getTaskQueue().getLast(); // 이전 작업
            Pose previousPos = previousTask.getDestinations().getFirst(); // 이전 작업의 선반 위치
            Pose currentPos = task.getDestinations().getFirst(); // 새로운 작업의 선반 위치

            return previousPos.equals(currentPos);
        }
        return false;
    }

    // 로봇과 작업의 스테이션 간 거리 계산
    private double calculateStationDistance(Robot robot, RobotTask task) {
        return calculateDistance(robot.getCurrentPose(), task.getDestinations().get(1));
    }
}
