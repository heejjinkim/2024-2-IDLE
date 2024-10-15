package com.example.__2_IDLE.test_allo_sim.v2.manager;

import com.example.__2_IDLE.test_allo_sim.v2.model.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class TaskManager {

    private List<Task> taskList;

    public TaskManager(int itemTypeCount) {
        taskList = new ArrayList<Task>();
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void allocateTask(Task allocatedTask) {
        taskList.remove(allocatedTask);
    }

    public List<Task> getTaskById(List<Integer> taskIds) {
        List<Task> tasks = new ArrayList<>();
        for (Integer taskId : taskIds) {
            for (Task task : taskList) {
                if (task.getId() == taskId) {
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    public Task getTaskById(int taskId) {
        for (Task task : taskList) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }
}
