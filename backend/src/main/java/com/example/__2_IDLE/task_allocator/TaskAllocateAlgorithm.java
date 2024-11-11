package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.task_allocator.controller.RobotController;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskAllocateAlgorithm {

    private TaskWave taskWave;
    private final RobotController robotController;
    private final StationService stationService;

    public void init(TaskWave taskWave) { // task wave의 모든 작업을 스테이션에 골고루 분배
        this.taskWave = taskWave;

        List<PickingTask> wave = taskWave.getWave();
        int stationCount = Station.values().length;
        AtomicReference<Long> stationId = new AtomicReference<>(1L);

        for (PickingTask task : wave) {
            Station station = Station.getById(stationId.get());
            station.addTask(task);
            updateStationId(stationId, stationCount);
        }
    }

    private void updateStationId(AtomicReference<Long> stationId, int stationCount) {
        stationId.set(stationId.get() + 1);
        if (stationId.get() > stationCount) {
            stationId.set(1L);
        }
    }

    public void step1() {
        Map<String, Robot> allRobots = robotController.getAllRobots();
        for (Robot robot : allRobots.values()) {
            Optional<PickingTask> optionalNearestTask = taskWave.getWave().stream()
                    .filter(task -> !task.isAllocated())
                    .min(Comparator.comparing(task -> Pose.distance(robot.getCurrentPose(), task.getPose())));
            PickingTask nearestTask = optionalNearestTask.get();
            allocateTask(robot, nearestTask);
        }
    }

    private void allocateTask(Robot robot, PickingTask task) {
        robot.allocateTask(task);
        task.setAllocateTrue();
    }

    private void allocateAllTask(Robot robot, List<PickingTask> tasks) {
        for (PickingTask task : tasks) {
            allocateTask(robot, task);
        }
    }

    public void step2() {
        Map<String, Robot> allRobots = robotController.getAllRobots();
        for (Robot robot : allRobots.values()) {
            PickingTask lastTask = robot.getLastTask();
            Optional<Station> optionalStation = getStationByTask(lastTask);
            if (optionalStation.isEmpty()) {
                break;
            }
            Station station = optionalStation.get();
            List<PickingTask> stronglyCorrelatedTask = stationService.getNotAllocatedTaskSameItemOf(lastTask, station);
            allocateAllTask(robot, stronglyCorrelatedTask);
        }
    }

    private Optional<Station> getStationByTask(PickingTask lastTask) {
        Optional<Station> optionalStation = stationService.getStationHasTask(lastTask);
        if (optionalStation.isEmpty()) {
            log.info("해당 작업을 할당받은 스테이션이 없습니다");
            return Optional.empty();
        }
        return optionalStation;
    }

    public void step3() {
        Map<String, Robot> allRobots = robotController.getAllRobots();
        for (Robot robot : allRobots.values()) {
            PickingTask lastTask = robot.getLastTask();
            Optional<Station> optionalLastTaskStation = getStationByTask(lastTask);
            if (optionalLastTaskStation.isEmpty()) {
                break;
            }
            Station lastTaskStation = optionalLastTaskStation.get();
            List<Station> otherStations = stationService.getAllStationsExcept(lastTaskStation);
            for (Station station : otherStations) {
                List<PickingTask> weaklyCorrelatedTask = stationService.getNotAllocatedTaskSameItemOf(lastTask, station);
                allocateAllTask(robot, weaklyCorrelatedTask);
            }
        }
    }

    public void step4() {
        Map<String, Robot> allRobots = robotController.getAllRobots();
        for (Robot robot : allRobots.values()) {
            Optional<Station> optionalStationHasMaxTimeCost = stationService.getStationHasMaxTimeCost();
            if (optionalStationHasMaxTimeCost.isEmpty()) {
                log.info("모든 작업이 할당되었습니다");
                return;
            }
            Station stationHasMaxTimeCost = optionalStationHasMaxTimeCost.get();
            Optional<PickingTask> optionalPickingTask = stationHasMaxTimeCost.pickOneUnallocatedTask();
            if (optionalPickingTask.isEmpty()) {
                log.info("모든 작업이 할당되었습니다");
                return;
            }
            allocateTask(robot, optionalPickingTask.get());
        }
    }


    public boolean isDone() {
        return taskWave.isAllTaskAllocated();
    }
}
