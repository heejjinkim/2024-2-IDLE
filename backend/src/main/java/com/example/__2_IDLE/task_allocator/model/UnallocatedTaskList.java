package com.example.__2_IDLE.task_allocator.model;

import com.example.__2_IDLE.global.model.enums.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UnallocatedTaskList {

    private final List<PickingTask> tasks = new ArrayList<>();

    public void add(PickingTask pickingTask) {
        tasks.add(pickingTask);
    }

    public boolean has(PickingTask pickingTask) {
        return tasks.contains(pickingTask);
    }

    public List<PickingTask> getTasksByItem(Item targetItem) {
        return tasks.stream()
                .filter(task -> {
                    Item item = task.getItem();
                    return item == targetItem;
                })
                .toList();
    }

    public Stream<PickingTask> stream() {
        return tasks.stream();
    }

    public void remove(PickingTask pickingTask) {
        tasks.remove(pickingTask);
    }
}
