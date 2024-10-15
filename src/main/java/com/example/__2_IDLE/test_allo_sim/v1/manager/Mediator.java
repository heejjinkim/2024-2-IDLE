package com.example.__2_IDLE.test_allo_sim.v1.manager;

import com.example.__2_IDLE.test_allo_sim.v1.Item;
import com.example.__2_IDLE.test_allo_sim.v1.Robot;
import com.example.__2_IDLE.test_allo_sim.v1.Station;
import com.example.__2_IDLE.test_allo_sim.v1.Task;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;

@Slf4j
@AllArgsConstructor
public class Mediator {
    ItemManager itemManager;
    RobotManager robotManager;
    StationManager stationManager;
    TaskManager taskManager;

    public LinkedList<Task> generateTask(int itemCount, int taskCount) {
        ArrayList<Item> randomItemList = itemManager.generateRandomItem(taskCount);
        return taskManager.generateTask(randomItemList, taskCount);
    }

    public Task getNextTask() {
        return taskManager.getNextTask();
    }

    public void finishTask(Task finishTask) throws InterruptedException {
        taskManager.finishedTask(finishTask);
    }

    /**
     * 작업이 생성됐을 때마다 호출
     * @param task
     * @throws InterruptedException
     */
    public int allocateTask(Task task) throws InterruptedException {
        log.info(task.getTaskId() + "번 task 할당 과정 시작");
        // 선반 겹치는지 확인
        Item item = task.getItem();
        boolean isNowTask = robotManager.isNowTask(item);

        Station station = stationManager.getStationForTask(); // station 선택
        task.setTargetStation(station);
        Robot robot = robotManager.getRobotForTask(task);
        task.setTargetRobot(robot);
        robot.setTotalTaskCount(robot.getTotalTaskCount() + 1);
        taskManager.allocateTask(task, station, robot);
        if (isNowTask){
            return 1;
        }
        return 0;
    }

    public Item getItemByIndex(Integer index) {
        return itemManager.getItemByIndex(index);
    }
}
