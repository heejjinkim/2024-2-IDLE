package com.example.__2_IDLE.test_allo_sim.v2.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {

    private List<Item> itemList;

    public void addItem(Item item) {
        itemList.add(item);
    }
}
