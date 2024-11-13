package com.example.__2_IDLE.global.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Pose {
    double x;
    double y;

    public static double distance(Pose p1, Pose p2) {
        return Math.abs(p1.getX() - p2.getY()) + Math.abs(p1.getY() - p2.getY());
    }
}