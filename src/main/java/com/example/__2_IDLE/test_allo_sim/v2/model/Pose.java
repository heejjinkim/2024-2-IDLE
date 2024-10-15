package com.example.__2_IDLE.test_allo_sim.v2.model;

import lombok.*;

@Data
@AllArgsConstructor
public class Pose {
    private int x;
    private int y;

    public int calculateDistance(Pose p) {
        return Math.abs(x - p.x) + Math.abs(y - p.y);
    }
}
