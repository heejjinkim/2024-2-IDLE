package com.example.__2_IDLE.task_allocator.model;

import java.util.ArrayList;
import java.util.List;

public class AllocatedTaskList {

    private final List<PickingTask> tasks = new ArrayList<>();

    public void addTask(PickingTask pickingTask) {
        tasks.add(pickingTask);
    }

    public void addAllTask(List<PickingTask> pickingTasks) {
        tasks.addAll(pickingTasks);
    }
}
