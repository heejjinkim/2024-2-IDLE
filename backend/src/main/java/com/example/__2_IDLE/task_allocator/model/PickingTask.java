package com.example.__2_IDLE.task_allocator.model;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import lombok.Getter;

import java.util.Random;

@Getter
public class PickingTask {

    private final Long id;
    private final Long orderId;
    private final Item item;
    private boolean isAllocated;
    private final int timeCost;

    public PickingTask(Long id, Long orderId, Item item) {
        this.id = id;
        this.orderId = orderId;
        this.item = item;
        this.isAllocated = false;
        this.timeCost = genRandomTimeCost();
    }

    public Pose getPose() {
        return item.getShelf().getPose();
    }

    public void setAllocateTrue() {
        this.isAllocated = true;
    }

    private int genRandomTimeCost() {
        Random random = new Random();
        return random.nextInt(3) + 1;
    }
}
