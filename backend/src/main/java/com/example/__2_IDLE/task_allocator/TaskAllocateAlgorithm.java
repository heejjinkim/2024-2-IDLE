package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.robot.RobotMapRepository;
import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskAllocateAlgorithm {

    private TaskWave taskWave;
    private Map<String, Robot> allRobots;
    private final StationService stationService;

    public void init(TaskWave taskWave, Map<String, Robot> allRobots) {
        this.taskWave = taskWave;
        this.allRobots = allRobots;
    }

    public void step1() {
        List<Item> allocatedItems = new ArrayList<>();
        for (Robot robot : allRobots.values()) {
            List<Item> items = Arrays.asList(Item.values());
            items.sort(Comparator.comparingDouble(item ->
                    Pose.distance(robot.getCurrentPose(), item.getShelf().getPose()))
            );
            for (Item item : items) {
                if (allocatedItems.contains(item)) {
                    continue;
                }
                try {
                    PickingTask task = taskWave.getAnyTaskByItem(item);
                    allocateTask(robot, task);
                    allocatedItems.add(task.getItem());
                } catch (NoSuchElementException e) {
                    log.info("생성된 작업의 item 종류가 로봇의 개수보다 적습니다.");
                }
                break;
            }
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
