package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class TaskAllocateAlgorithm {

    private final TaskAllocatorUtils taskAllocatorUtils;
    private TaskWave taskWave;

    public void setUnallocatedList() {
        Long stationCount = taskAllocatorUtils.getStationCount();
        AtomicLong atomicStationId = new AtomicLong(0L);
        taskWave.getWave()
                .forEach(pickingTask -> {
                    long stationId = atomicStationId.get();
                    long nextStationId = stationId + 1;
                    taskAllocatorUtils.addToUnallocatedList(stationId, pickingTask);
                    updateStationId(atomicStationId, nextStationId, stationCount);
                });
    }

    public void step1() {
        Map<String, Robot> allRobots = taskAllocatorUtils.getAllRobots();
        for (Robot robot : allRobots.values()) {
            Pose robotCurrentPose = robot.getCurrentPose();
            PickingTask nearestTask = findNearestTask(robotCurrentPose);

            allocateTask(robot, nearestTask);
        }
    }

    public void step2() {
        Map<String, Robot> allRobots = taskAllocatorUtils.getAllRobots();
        List<Station> allStations = taskAllocatorUtils.getAllStations();
        try {
            for (Robot robot : allRobots.values()) {
                PickingTask lastTask = robot.getLastTask();
                Station targetStation = findTargetStation(allStations, lastTask);
                Item targetItem = lastTask.getItem();
                List<PickingTask> stronglyCorrelatedTasks = targetStation.getTasksByItem(targetItem);
                allocateAllTask(robot, stronglyCorrelatedTasks);
            }
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
        }
    }

    public void step3() {
        Map<String, Robot> allRobots = taskAllocatorUtils.getAllRobots();
        List<Station> allStations = taskAllocatorUtils.getAllStations();
        for (Robot robot : allRobots.values()) {
            PickingTask lastTask = robot.getLastTask();
            Item targetItem = lastTask.getItem();
            List<PickingTask> weaklyCorrelatedTasks = allStations.stream()
                    .flatMap(station -> station.getTasksByItem(targetItem).stream())
                    .toList();
            allocateAllTask(robot, weaklyCorrelatedTasks);
        }
    }

    public void step4() {
        Map<String, Robot> allRobots = taskAllocatorUtils.getAllRobots();
        for (Robot robot : allRobots.values()) {
            if (robot.hasTask()) {
                Optional<PickingTask> minCostTaskInMaxTimeCostStation = taskAllocatorUtils.getMinCostTaskInMaxTimeCostStation(robot);
                allocateTask(robot, minCostTaskInMaxTimeCostStation.get());
            }
        }
    }

    private void allocateTask(Robot robot, PickingTask pickingTask) {
        robot.allocateTask(pickingTask);
        taskWave.removeTask(pickingTask);
        taskAllocatorUtils.allocateTask(pickingTask);
    }

    private void allocateAllTask(Robot robot, List<PickingTask> pickingTaskList) {
        robot.allocateAllTask(pickingTaskList);
        taskWave.removeAllTask(pickingTaskList);
        taskAllocatorUtils.allocateAllTask(pickingTaskList);
    }

    private static Station findTargetStation(List<Station> allStations, PickingTask pickingTask) {
        Optional<Station> optionalTargetStation = allStations.stream()
                .filter(station -> station.getUnallocatedTaskList().has(pickingTask))
                .findFirst();
        return optionalTargetStation.get();
    }

    private PickingTask findNearestTask(Pose robotCurrentPose) {
        Optional<PickingTask> optionalNearestTask = taskWave.getWave().stream()
                .min(Comparator.comparing(
                        pickingTask -> Pose.distance(pickingTask.getPose(), robotCurrentPose))
                );
        return optionalNearestTask.get();
    }

    private void updateStationId(AtomicLong atomicStationId, long nextStationId, Long stationCount) {
        atomicStationId.set(nextStationId % stationCount == 0 ? 1L : nextStationId % stationCount);
    }

    public boolean isAllTasksAllocated() {
        return taskWave.isEmpty();
    }
}
