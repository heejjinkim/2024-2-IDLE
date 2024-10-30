package com.example.__2_IDLE;

import com.example.__2_IDLE.task_allocator.controller.ItemController;
import org.junit.jupiter.api.Test;

public class ItemControllerTest {

    ItemController itemController = new ItemController();

    @Test
    public void getAllItemTest() {
        System.out.println(itemController.getAllItems());
    }
}
