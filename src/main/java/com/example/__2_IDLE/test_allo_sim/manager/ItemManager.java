package com.example.__2_IDLE.test_allo_sim.manager;

import com.example.__2_IDLE.test_allo_sim.Item;
import com.example.__2_IDLE.test_allo_sim.Pose;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Random;

@Getter
@NoArgsConstructor
public class ItemManager {

    private ArrayList<Item> itemList = new ArrayList<>();

    public void createItem(Pose pose, String name) {
        this.itemList.add(new Item(pose, name));
    }

    public Item getItemByIndex(int index) {
        return itemList.get(index);
    }

    public ArrayList<Item> generateRandomItem(int taskCount) {
        ArrayList<Item> randomItemList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < taskCount; i++) {
            int index = random.nextInt(itemList.size());
            randomItemList.add(itemList.get(index));
        }

        return randomItemList;
    }
}
