package com.example.__2_IDLE.simulator.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderStatus {
    private Long id;
    private int originalItemCount;
    private int completedItemCount;
}
