package com.example.__2_IDLE.test_allo_sim.v1.manager;

import com.example.__2_IDLE.test_allo_sim.v1.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@Getter
@NoArgsConstructor
public class RobotManager {

    private ArrayList<Robot> robotList = new ArrayList<>();

    public void createRobot(Pose pose, String namespace, Mediator mediator) {
        this.robotList.add(new Robot(namespace, pose, mediator));
    }

    public void printTotalTaskCount() {
        for (Robot robot : robotList) {
            log.info("{} processed {} tasks", robot.getNamespace(), robot.getTotalTaskCount());
        }
    }

    public Robot getRobotForTask(Task task) {
        Robot robot = null;
        double minTime = Double.MAX_VALUE;
        for (Robot r : robotList) {
            // 시간 비용 계산
            double time = calcTime(r, task);
            if (time < minTime) {
                minTime = time;
                robot = r;
            }
        }
        log.info("[? - {}]선택된 로봇: {}", task.getTaskId(), robot.getNamespace());
        return robot;
    }

    private double calcTime(Robot robot, Task task) {
        Item nextTaskItem = task.getItem();
        Station nextStation = task.getTargetStation();

        double time = 0;
        Pose robotNowPose = robot.nowPose();
        // 첫 작업인 경우
        if (robot.getNowTask() == null && robot.isQueueEmpty()) {
            log.info("[{} - {}] 첫 작업인 경우, 현재위치: {}, {}",
                    robot.getNamespace(),
                    task.getTaskId(),
                    robotNowPose.getX(),
                    robotNowPose.getY());
            time = calcTimeUtil(robotNowPose, nextTaskItem.getPose())
                    + calcTimeUtil(nextTaskItem.getPose(), nextStation.getPose()) * 2;
            return time;
        }

        Task lastTask = robot.getLastTask();
        Item lastItem = lastTask.getItem();

        // 이전 작업과 현재 작업이 다른 선반에 위치해 있는 경우
        if (!lastItem.isEqual(nextTaskItem)) {
            log.info("[{} - {}] 다른 선반에 위치해 있는 경우, 현재위치: {}, {}",
                    robot.getNamespace(),
                    task.getTaskId(),
                    robotNowPose.getX(),
                    robotNowPose.getY());
            Pose lastItemPose = lastItem.getPose();
            Station lastTaskTargetStation = lastTask.getTargetStation();
            Pose nextItemPose = nextTaskItem.getPose();
            Station nextTaskTargetStation = task.getTargetStation();
            time = calcTimeUtil(lastItemPose, lastTaskTargetStation.getPose())
                    + calcTimeUtil(lastTaskTargetStation.getPose(), nextItemPose)
                    + calcTimeUtil(nextItemPose, nextTaskTargetStation.getPose());
            return time;
        }
        // 이전 작업과 현재 작업이 같은 선반에 있는 경우
        log.info("[{} - {}] 같은 선반에 있는 경우, 현재위치: {}, {}",
                robot.getNamespace(),
                task.getTaskId(),
                robotNowPose.getX(),
                robotNowPose.getY());
        time = calcTimeUtil(robotNowPose, lastTask.getTargetStation().getPose())
                + calcTimeUtil(lastTask.getTargetStation().getPose(), nextStation.getPose());
        return time;
    }

    private double calcTimeUtil(Pose p1, Pose p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    public boolean isNowTask(Item item) {
        for (Robot robot : robotList) {
            if (robot.getNowTask() != null || robot.getNowTask().getItem().getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }
}
