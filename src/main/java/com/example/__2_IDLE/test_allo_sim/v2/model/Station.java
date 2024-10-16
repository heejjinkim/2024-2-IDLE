package com.example.__2_IDLE.test_allo_sim.v2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@AllArgsConstructor
public class Station {

    private String name;
    private Pose pose;
    private Map<Integer, String> unallocatedTasks;
    private List<Robot> waitingRobots;
    private int pickingTime;

    public void addTaskToUnallocatedTasks(Task task) {
        task.setStation(this);
        unallocatedTasks.put(task.getId(), task.getItemName());
    }

    public void doTask() {
        while (!waitingRobots.isEmpty()) {
            try {
                Thread.sleep(pickingTime * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            finishTask();
        }
    }

    public void addWaitingRobot(Robot robot) {
        waitingRobots.add(robot);
    }

    public void finishTask() {
        Robot removedRobot = waitingRobots.remove(0);
//        removedRobot.doNextTask();
    }

    public boolean hasTaskInUnallocatedTasks(Task task) {
        return unallocatedTasks.containsKey(task.getId());
    }

    public void allocateTask(Task task) {
        unallocatedTasks.remove(task.getId());
    }

    public List<Integer> findStrongCorrelatedTasks(Task lastTask) {
        List<Integer> strongCorrelatedTasks = new ArrayList<>();
        for (Integer taskId : unallocatedTasks.keySet()) {
            String itemName = unallocatedTasks.get(taskId);
            if (itemName.equals(lastTask.getItemName())) {
                strongCorrelatedTasks.add(taskId);
            }
        }
        return strongCorrelatedTasks;
    }

    public void printUnallocatedTasks() {
        log.info("{}==========", this.name);
        for (Integer taskId : unallocatedTasks.keySet()) {
            String itemName = unallocatedTasks.get(taskId);
            log.info("[{}, {}]", taskId, itemName);
        }
    }
}
