package com.example.__2_IDLE.global.model.robot;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Shelf;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.ros.ROSValueGetter;
import com.example.__2_IDLE.ros.data_listener.topic.TopicDataListener;
import com.example.__2_IDLE.ros.data_sender.publisher.GoalPublisher;
import com.example.__2_IDLE.ros.message_handler.robot.TopicRobotPoseMessageHandler;
import com.example.__2_IDLE.ros.message_value.RobotPoseMessageValue;
import com.example.__2_IDLE.task_allocator.controller.StationController;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RobotTaskAssigner {

    private final Robot robot;
    private final Ros ros;
    private static final double tolerance = 0.5; // 0.5 이내를 도착으로 간주
    private final TopicRobotPoseMessageHandler messageHandler;
    private final TopicDataListener dataListener;
    private final ROSValueGetter<RobotPoseMessageValue> valueGetter;
    private final ScheduledExecutorService scheduler;
    private final StationController stationController;
    private boolean isRunning = false;

    public RobotTaskAssigner(Robot robot, Ros ros) {
        this.robot = robot;
        this.ros = ros;
        this.messageHandler = new TopicRobotPoseMessageHandler(robot.getNamespace());
        this.dataListener = new TopicDataListener(ros, messageHandler);
        this.valueGetter = new ROSValueGetter<>(dataListener, messageHandler);
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.stationController = new StationController(); // TODO: 수정필요
    }

    public void start() {
        if (isRunning) {
            log.info("로봇 {}의 TaskAssigner가 이미 실행 중입니다.", robot.getNamespace());
            return;
        }
        isRunning = true;
        doNextTask(robot.getFirstTask());
    }

    private void doNextTask(PickingTask nextTask) {
        if (nextTask == null) {
            log.info("로봇 {}의 모든 작업이 완료되었습니다.", robot.getNamespace());
            isRunning = false;
            return;
        }
        moveToShelfOrStation(nextTask, false);
    }

    private void moveToShelfOrStation(PickingTask currentTask, boolean skipShelf) {
        Shelf shelf = currentTask.getItem().getShelf();
        Station station = stationController.getStationHasTask(currentTask).get();

        if (!skipShelf) {
            log.info("로봇 {}: 선반 {}로 이동합니다.", robot.getNamespace(), shelf);
            moveToLocation(shelf.getPose(), () -> {
                log.info("로봇 {}: {}을 피킹하고 있습니다", robot.getNamespace(), shelf);
                scheduleAfterDelay(() -> moveToShelfOrStation(currentTask, true), 5);
            });
        } else {
            log.info("로봇 {}: 스테이션 {}로 이동합니다.", robot.getNamespace(), station);
            moveToLocation(station.getPose(), () -> {
                log.info("로봇 {}: {}을 {}에 서비스하고 있습니다.", robot.getNamespace(), currentTask.getItem(), station);
                scheduleAfterDelay(() -> completeOrContinue(currentTask, station), 5);
            });
        }
    }

    private void completeOrContinue(PickingTask currentTask, Station station) {
        robot.completeCurrentTask(station);
        PickingTask nextTask = robot.getFirstTask();

        if (nextTask != null && nextTask.getItem().equals(currentTask.getItem())) {
            log.info("로봇 {}: 동일한 선반을 다른 스테이션으로 이동시킵니다.", robot.getNamespace());
            moveToShelfOrStation(nextTask, true);
        } else {
            log.info("로봇 {}: {}을 원위치 시키기 위해 이동합니다.", robot.getNamespace(), currentTask.getItem().getShelf());
            moveToLocation(currentTask.getItem().getShelf().getPose(), () -> {
                log.info("로봇 {}: 선반 {}을 원위치 시키고 있습니다.", robot.getNamespace(), currentTask.getItem().getShelf());
                scheduleAfterDelay(() -> doNextTask(nextTask), 5);
            });
        }
    }

    private void moveToLocation(Pose destination, Runnable onGoalReached) {
        sendGoalToRobot(destination);
        trackRobotPosition(destination, onGoalReached);
    }

    private void sendGoalToRobot(Pose destination) {
        new GoalPublisher(ros, robot.getNamespace(), destination).publish();
    }

    private void trackRobotPosition(Pose destination, Runnable onGoalReached) {
        final ScheduledFuture<?>[] trackingTask = new ScheduledFuture<?>[1];

        trackingTask[0] = scheduler.scheduleAtFixedRate(() -> {
            RobotPoseMessageValue currentPosition = valueGetter.getValue();
            if (isGoalReached(currentPosition, destination)) {
                dataListener.stopListening();
                trackingTask[0].cancel(false); // 현재 작업 취소
                onGoalReached.run(); // 다음 목적지 작업 수행
            }
        }, 0, 1, TimeUnit.SECONDS); // 1초마다 주기적으로 위치 확인 작업 수행
    }

    private boolean isGoalReached(RobotPoseMessageValue currentPosition, Pose destination) {
        double dx = currentPosition.getX() - destination.getX();
        double dy = currentPosition.getY() - destination.getY();
        return Math.sqrt(dx * dx + dy * dy) <= tolerance;
    }

    private void scheduleAfterDelay(Runnable task, int delayInSeconds) {
        scheduler.schedule(task, delayInSeconds, TimeUnit.SECONDS);
    }
}