package com.example.__2_IDLE.test_allo_sim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Item {

    private Pose pose;
    private String name;

    public boolean isEqual(Item item) {
        return this.name.equals(item.getName());
    }
}
