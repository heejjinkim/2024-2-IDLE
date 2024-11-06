package com.example.__2_IDLE.task_allocator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllocatedTaskList {

    private final List<PickingTask> tasks = new ArrayList<>();

    public void addTask(PickingTask pickingTask) {
        tasks.add(pickingTask);
    }

    public void addAllTask(List<PickingTask> pickingTasks) {
        tasks.addAll(pickingTasks);
    }

    public List<Long> removeSameItemTasks(PickingTask pickingTask) {
        List<Long> removedTaskIds = new ArrayList<>();
        Iterator<PickingTask> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            PickingTask task = iterator.next();
            if (task.getItem().equals(pickingTask.getItem())) {
                removedTaskIds.add(task.getId());
                iterator.remove();
            }
        }

        return removedTaskIds;
    }
}
