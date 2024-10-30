package com.example.__2_IDLE.task.allocator;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.ros.ROSValueGetter;
import com.example.__2_IDLE.ros.data_listener.topic.TopicDataListener;
import com.example.__2_IDLE.ros.data_sender.publisher.GoalPublisher;
import com.example.__2_IDLE.ros.message_handler.robot.TopicRobotPoseMessageHandler;
import com.example.__2_IDLE.ros.message_value.RobotPoseMessageValue;
import com.example.__2_IDLE.task.model.RobotTask;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskAllocator {

    private final Robot robot;
    private final Ros ros;
    private final double tolerance = 0.5; // 0.5 이내를 도착으로 간주
    private final TopicRobotPoseMessageHandler messageHandler;
    private final TopicDataListener dataListener;
    private final ROSValueGetter<RobotPoseMessageValue> valueGetter;
    private final ScheduledExecutorService scheduler;

    public TaskAllocator(Robot robot, Ros ros) {
        this.robot = robot;
        this.ros = ros;
        this.messageHandler = new TopicRobotPoseMessageHandler(robot.getNamespace());
        this.dataListener = new TopicDataListener(ros, messageHandler);
        this.valueGetter = new ROSValueGetter<>(dataListener, messageHandler);
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startAllocate() {
        RobotTask robotTask = robot.getFirstTaskInQueue();
        if (robotTask == null) {
            log.info("로봇 {}의 모든 작업이 완료되었습니다.", robot.getNamespace());
            return;
        }
        processDestinations(robotTask, 0);
    }

    private void processDestinations(RobotTask robotTask, int index) {
        List<Pose> destinations = robotTask.getDestinations();

        if (index >= destinations.size()) {
            // 현재 작업 완료 처리
            robot.completeCurrentTask();
            log.info("로봇 {}이 수행 중이던 작업을 완료했습니다.", robot.getNamespace());

            // 다음 작업 가져오기
            RobotTask nextTask = robot.getFirstTaskInQueue();
            if (nextTask == null) {
                log.info("로봇 {}의 모든 작업이 완료되었습니다.", robot.getNamespace());
                return;
            }
            log.info("다음 작업을 시작합니다.");
            processDestinations(nextTask, 0);
            return;
        }

        Pose destination = destinations.get(index);
        sendGoalToRobot(destination);

        startTrackingRobotPose(robotTask, index, () -> {
            log.info("5초 대기 후 다음 목적지로 이동합니다.", destination);
            scheduler.schedule(() -> processDestinations(robotTask, index + 1), 5, TimeUnit.SECONDS);
        });
    }

    private void sendGoalToRobot(Pose destination) {
        GoalPublisher goalPublisher = new GoalPublisher(ros, robot.getNamespace(), destination);
        goalPublisher.publish();
    }

    private void startTrackingRobotPose(RobotTask robotTask, int index, Runnable onGoalReached) {
        final ScheduledFuture<?>[] trackingTask = new ScheduledFuture<?>[1];

        trackingTask[0] = scheduler.scheduleAtFixedRate(() -> {
            RobotPoseMessageValue currentPosition = valueGetter.getValue();
            if (isGoalReached(currentPosition, robotTask.getDestinations().get(index))) {
                logTaskProgress(robotTask, index);
                dataListener.stopListening();
                trackingTask[0].cancel(false); // 현재 작업 취소
                onGoalReached.run(); // 다음 목적지 작업 수행
            }
        }, 0, 1, TimeUnit.SECONDS);  // 1초마다 주기적으로 위치 확인 작업 수행
    }

    private boolean isGoalReached(RobotPoseMessageValue currentPosition, Pose destination) {
        double dx = currentPosition.getX() - destination.getX();
        double dy = currentPosition.getY() - destination.getY();
        return Math.sqrt(dx * dx + dy * dy) <= tolerance;
    }

    private void logTaskProgress(RobotTask robotTask, int index) {
        List<Pose> destinations = robotTask.getDestinations();

        if (index == 0) {
            log.info("로봇 {}이 {}을 피킹했습니다, {}", robot.getNamespace(), robotTask.getItem().getShelf(), robotTask.getItem().getShelf().getPose());
        } else if (index == destinations.size() - 1) {
            log.info("로봇 {}이 {}을 제자리에 돌려놓았습니다, {}", robot.getNamespace(), robotTask.getItem().getShelf(), robotTask.getItem().getShelf().getPose());
        } else {
            log.info("로봇 {}이 {}을 {}에 서비스하였습니다, {}", robot.getNamespace(), robotTask.getItem(), robotTask.getStationNameByIndex(index), destinations.get(index));
        }
    }
}