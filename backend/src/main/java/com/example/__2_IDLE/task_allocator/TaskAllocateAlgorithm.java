package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.task_allocator.controller.RobotController;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskAllocateAlgorithm {

    private TaskWave taskWave;
    private final RobotController robotController;
    private final StationService stationService;

    public void init(TaskWave taskWave) {
        this.taskWave = taskWave;
    }

    public void step1() {
        Map<String, Robot> allRobots = robotController.getAllRobots();
        for (Robot robot : allRobots.values()) {
            PickingTask nearestPickingTask = taskWave.getNearestPickingTask(robot);
            allocateTask(robot, nearestPickingTask);
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
