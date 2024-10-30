package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.task_allocator.controller.ItemController;
import com.example.__2_IDLE.task_allocator.controller.RobotController;
import com.example.__2_IDLE.task_allocator.controller.StationController;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class TaskAllocatorUtils {


    private final RobotController robotController;
    private final ItemController itemController;
    private final StationController stationController;

    public Long getStationCount() {
        return stationController.getStationCount();
    }

    public void addToUnallocatedList(long stationId, PickingTask pickingTask) {
        stationController.addToUnallocatedList(stationId, pickingTask);
    }

    public Map<String, Robot> getAllRobots() {
        return robotController.getAllRobots();
    }

    public List<Station> getAllStations() {
        return stationController.getAllStations();
    }

    public List<Item> getAllItems() {
        return itemController.getAllItems();
    }

    private Station getHasMaxTimeCostStation() {
        return stationController.getStationHasMaxTimeCost().get();
    }

    private Station getStationHasTask(PickingTask task) {
        Optional<Station> optionalStationHasTask = stationController.getStationHasTask(task);
        return optionalStationHasTask.get();
    }

    public Optional<PickingTask> getMinCostTaskInMaxTimeCostStation(Robot robot) {

        Station hasMaxTimeCostStation = getHasMaxTimeCostStation();
        return hasMaxTimeCostStation.unallocatedTaskStream()
                .min((firstTask, secondTask) ->
                        Double.compare(
                                calcTimeCostInStep4(robot, firstTask, hasMaxTimeCostStation),
                                calcTimeCostInStep4(robot, secondTask, hasMaxTimeCostStation)
                        )
                );

    }

    private double calcTimeCostInStep4(Robot robot, PickingTask nextTask, Station nextTaskStation) {
        PickingTask lastTask = robot.getLastTask();
        if (!robot.hasTask()) { // 첫 번째 작업인 경우
            return Pose.distance(robot.getCurrentPose(), nextTask.getPose())
                    + Pose.distance(nextTask.getPose(), nextTaskStation.getPose());
        }
        Station lastTaskStation = getStationHasTask(lastTask);
        if (nextTask.isSameItem(lastTask)) { // 같은 선반인 경우
            return Pose.distance(lastTaskStation.getPose(), nextTaskStation.getPose());
        }
        return Pose.distance(lastTask.getPose(), nextTask.getPose())
                + Pose.distance(nextTask.getPose(), nextTaskStation.getPose());
    }

    public void allocateTask(PickingTask pickingTask) {
        Optional<Station> stationHasTask = stationController.getStationHasTask(pickingTask);
        stationHasTask.get().allocateTask(pickingTask);
    }

    public void allocateAllTask(List<PickingTask> pickingTaskList) {
        for (PickingTask pickingTask : pickingTaskList) {
            allocateTask(pickingTask);
        }
    }
}
