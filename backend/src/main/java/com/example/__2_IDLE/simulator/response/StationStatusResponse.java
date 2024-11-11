package com.example.__2_IDLE.simulator.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class StationStatusResponse {
    private Long id;
    private String name;
    private List<OrderStatus> orders;
}
