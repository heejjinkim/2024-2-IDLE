package com.example.__2_IDLE.test_allo_sim.v2.model;

import com.example.__2_IDLE.test_allo_sim.v2.manager.ItemManager;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class Robot {

    private String namespace;
    private Pose initalPose;
    private Pose nowPose;
    private List<Task> taskList;
    private Task nowTask;
    private ItemManager itemManager;

    public Robot (String namespace, Pose initalPose, Pose nowPose, List<Task> taskList, ItemManager itemManager) {
        this.namespace = namespace;
        this.initalPose = initalPose;
        this.nowPose = nowPose;
        this.taskList = taskList;
        nowTask = null;
        this.itemManager = itemManager;
    }

    public void allocateTask(Task task) {
        taskList.add(task);
    }

    public void allocateAllTask(List<Task> task) {
        taskList.addAll(task);
    }

//    public void doNextTask() {
//        Task nextTask = taskList.get(0);
//        String itemName = nextTask.getItemName();
//        Pose itemPose = itemManager.getPose(itemName);
//        Station station = nextTask.getStation();
//        if (nowTask == null) {
//            int initialToItem = initalPose.calculateDistance(itemPose);
//            int itemToStation = itemPose.calculateDistance(station.getPose());
//            int time = initialToItem + itemToStation;
//            try {
//                Thread.sleep(time * 1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            nowPose = station.getPose();
//            station.
//        }
//    }

    public Task getLastTask() {
        return taskList.get(taskList.size() - 1);
    }

    public void printAllocatedTasks() {
        log.info("{}===============", namespace);
        for (Task task : taskList) {
            log.info("[{}, {}]", task.getId(), task.getItemName());
        }
    }

    public int getMaxCompletionTime(Station station) {
        int lastTaskInStationIndex = -1;
        for (int i = taskList.size() - 1; i >= 0; i--) {
            Task task = taskList.get(i);
            if (task.getStation().equals(station)) {
                lastTaskInStationIndex = i;
                break;
            }
        }
        if (lastTaskInStationIndex == -1) {
            return 0;
        }
        return calcTotalTimeCost(lastTaskInStationIndex);
    }

    private int calcTotalTimeCost(int lastTaskInStationIndex) {
        Pose startPose = this.initalPose;
        int totalTimeCost = 0;
        for (int i = 0; i < lastTaskInStationIndex; i++) {
            Task task = taskList.get(i);
            Station destStation = task.getStation();
            Pose destStationPose = destStation.getPose();
            int timeCost = startPose.calculateDistance(destStationPose);
            totalTimeCost += timeCost;
        }
        return totalTimeCost;
    }
}
