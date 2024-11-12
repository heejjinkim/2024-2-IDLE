package com.example.__2_IDLE.simulator.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryStatusResponse {
    private int SameDayDeliveryCount;
    private int StandardDeliveryCount;
}
