package com.example.__2_IDLE.task_allocator.model;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.robot.model.Robot;

import java.util.List;
import java.util.Optional;

public class PickingOrder {

    private final List<PickingTask> pickingTasks;

    public PickingOrder(List<PickingTask> pickingTasks) {
        this.pickingTasks = pickingTasks;
    }

    public boolean isAllTaskAllocated() {
        for (PickingTask pickingTask : pickingTasks) {
            if (!pickingTask.isAllocated()) {
                return false;
            }
        }
        return true;
    }

    public List<PickingTask> getPickingTasks() {
        return pickingTasks;
    }

    public PickingTask findNearestTask(Robot robot) {
        PickingTask nearestPickingTask = null;
        long minDistance = Long.MAX_VALUE;
        for (PickingTask pickingTask : pickingTasks) {
            double distance = pickingTask.calcDistance(robot);
            if (distance < minDistance) {
                nearestPickingTask = pickingTask;
            }
        }
        return nearestPickingTask;
    }

    public Optional<PickingTask> getAnyTaskByItem(Item item) {
        return pickingTasks.stream()
                .filter(pickingTask -> pickingTask.getItem().equals(item))
                .findFirst();
    }
}
