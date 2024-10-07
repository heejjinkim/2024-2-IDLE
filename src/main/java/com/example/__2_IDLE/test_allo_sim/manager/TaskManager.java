package com.example.__2_IDLE.test_allo_sim.manager;

import com.example.__2_IDLE.test_allo_sim.Item;
import com.example.__2_IDLE.test_allo_sim.Robot;
import com.example.__2_IDLE.test_allo_sim.Station;
import com.example.__2_IDLE.test_allo_sim.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;

@Slf4j
public class TaskManager {

    private LinkedList<Task> taskQueue = new LinkedList<>();

    public LinkedList<Task> generateTask(ArrayList<Item> randomItemList, int taskCount) {
        // TODO task 생성해야
        long taskId = 0;
        for (Item item : randomItemList) {
            Task newTask = new Task(taskId, item, null, null);
            taskQueue.add(newTask);
            taskId += 1;
        }
        return this.taskQueue;
    }

    public void allocateTask(Task task, Station station, Robot robot) throws InterruptedException {
        log.info("[{} - {}]작업 할당 시작", task.getTargetRobot().getNamespace(), task.getTaskId());
        station.addTask(task);
        robot.addTask(task);
    }

    public void finishedTask(Task task) throws InterruptedException {
        Robot robot = task.getTargetRobot();
        Station station = task.getTargetStation();

        robot.finishTask(); // 현재 진행중인 task를 다음 task 또는 null로 교체
        station.finishTask();
    }

    public Task getNextTask() {
        return taskQueue.poll();
    }
}
