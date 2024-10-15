package com.example.__2_IDLE.test_allo_sim.v2.manager;

import com.example.__2_IDLE.test_allo_sim.v2.model.Pose;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    private Map<String, Pose> itemPoses;

    public ItemManager(int itemTypeCount, int itemInterval, int y) {
        this.itemPoses = new HashMap<String, Pose>();
        makeItems(itemTypeCount, itemInterval, y);
    }

    public Pose getPose(String itemType) {
        return itemPoses.get(itemType);
    }

    public void addItem(String itemType, Pose pose) {
        itemPoses.put(itemType, pose);
    }

    private void makeItems(int itemTypeCount, int itemInterval, int y) {
        for (int i = 0; i < itemTypeCount; i++) {
            Pose pose = new Pose(i * itemInterval, y);
            String itemName = "item" + i;
            itemPoses.put(itemName, pose);
        }
    }
}
