package com.example.__2_IDLE.test_allo_sim.v2.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task {

    private int id;
    private String itemName;
    private Station station;
}
