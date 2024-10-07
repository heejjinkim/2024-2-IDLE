package com.example.__2_IDLE.test_allo_sim;

import com.example.__2_IDLE.test_allo_sim.manager.Mediator;
import com.example.__2_IDLE.test_allo_sim.manager.TaskManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Getter
@Setter
public class Robot {

    private Pose pose;
    private Item item;
    private String namespace;
    private Station targetStation;
    private LinkedList<Task> taskQueue;
    private Task nowTask;
    private Mediator mediator;
    private LocalDateTime taskStartTime;
    private Pose destination;
    private int totalTaskCount;
    private boolean isMoving;

    public Robot(String namespace, Pose pose, Mediator mediator) {
        this.pose = pose;
        this.item = item;
        this.namespace = namespace;
        this.targetStation = null;
        this.taskQueue = new LinkedList<>();
        this.nowTask = null;
        this.mediator = mediator;
        this.taskStartTime = LocalDateTime.now();
        this.destination = null;
        this.totalTaskCount = 0;
        this.isMoving = false;
    }

    public boolean isQueueEmpty() {
        return taskQueue.isEmpty();
    }

    public Task getLastTask() {
        if (taskQueue.isEmpty()) {
            return nowTask;
        }
        return taskQueue.getLast();
    }

    /**
     * 시뮬레이션 시간의 0.001초를 실제 시간의 1초로 가정
     * 거리1 당 이동시간 1초라고 가정
     * @throws InterruptedException
     */
    public void doTask() throws InterruptedException {
        log.info("[{} - {}] 작업 수행", this.namespace, this.nowTask.getTaskId());
        Item item = nowTask.getItem();
        Station targetStation = nowTask.getTargetStation();
        // item으로 이동
        moveToItem(item);
        // station으로 이동
        moveToStation(targetStation);
        // 작업 종료
        long taskId = nowTask.getTaskId();
        mediator.finishTask(nowTask);
        log.info("[{} - {}] 작업 종료", this.namespace, taskId);
    }

    private void moveToStation(Station targetStation) throws InterruptedException {
        long waitTime;
        waitTime = calcWaitTime(targetStation.getPose());
        this.destination = targetStation.getPose();
        moveRobot(waitTime);
        // station에서 일정 시간 대기
        this.isMoving = false;
        waitRobot(5);
    }

    private void moveToItem(Item item) throws InterruptedException {
        long waitTime = calcWaitTime(item.getPose());
        this.destination = item.getPose();
        moveRobot(waitTime);
        this.pose = item.getPose();
    }

    /**
     * 로봇이 이동 중일 때 호출
     * 이동 중이 아니면 pose 그대로 갖다 쓰기
     * @return
     */
    public Pose nowPose() {
        if (this.isMoving){
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(taskStartTime, now);
            long milliseconds = duration.toMillis();
            log.info("milliseconds: {}", milliseconds);

            double distanceX = destination.getX() - pose.getX();
            double distanceY = destination.getY() - pose.getY();
//            if (distanceX == 0) {
//                nowX =
//            }
            double nowX = pose.getX() + milliseconds / distanceX;
            double nowY = pose.getY() + milliseconds / distanceY;
            return new Pose(nowX, nowY);
        }
        return this.pose;

    }

    /**
     * nowTask 값 바꾼 후 null이 아닌 경우 doTask 호출
     * @return
     * @throws InterruptedException
     */
    public void finishTask() throws InterruptedException {
        nowTask = taskQueue.poll();
        if (nowTask != null) {
            doTask();
        }
    }

    public void addTask(Task task) throws InterruptedException {
        log.info("[{} - {}] {}번 작업을 할당하였습니다.", task.getTargetRobot().getNamespace(), task.getTaskId(), task.getTaskId());
        if (nowTask != null) {
            taskQueue.add(task);
            return;
        }
        if (taskQueue.isEmpty()) {
            nowTask = task;
            doTask(); // 바로 작업 수행
            return;
        }
        taskQueue.add(task); // 지금 진행 중인 task는 없지만 taskQueue가 비어 있지 않은 경우
    }

    private void moveRobot(long waitTime) throws InterruptedException {
        this.isMoving = true;
        this.taskStartTime = LocalDateTime.now();
        waitRobot(waitTime);
    }

    private void waitRobot(long waitTime) throws InterruptedException {
        Thread.sleep(waitTime);
    }

    private long calcWaitTime(Pose pose) {
        long waitTime = (long) (Math.abs(pose.getX() - this.pose.getX()) + Math.abs(pose.getY() - this.pose.getY()));
        log.info("{} wait {} ms", this.namespace, waitTime);
        return waitTime;
    }
}
