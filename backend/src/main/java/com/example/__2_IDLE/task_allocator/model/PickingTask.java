package com.example.__2_IDLE.task_allocator.model;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.enums.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PickingTask {

    private final Long id;
    private final Long orderId;
    private final Item item;

    public Pose getPose() {
        return item.getShelf().getPose();
    }

    public boolean isSameItem(PickingTask pickingTask) {
        return this.item == pickingTask.getItem();
    }
}
