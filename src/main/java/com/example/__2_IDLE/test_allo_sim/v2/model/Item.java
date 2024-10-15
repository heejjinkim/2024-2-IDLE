package com.example.__2_IDLE.test_allo_sim.v2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Item {

    private String name;

    public boolean isEqual(Item item) {
        return this.name.equals(item.getName());
    }
}
