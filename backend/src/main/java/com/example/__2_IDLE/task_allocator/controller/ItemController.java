package com.example.__2_IDLE.task_allocator.controller;

import com.example.__2_IDLE.global.model.enums.Item;

import java.util.List;

public class ItemController {

    // 모든 Item의 위치 정보 가져오기
    public List<Item> getAllItems() {
        return List.of(Item.values());
    }
}
