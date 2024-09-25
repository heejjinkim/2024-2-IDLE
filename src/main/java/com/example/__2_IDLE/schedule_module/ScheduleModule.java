package com.example.__2_IDLE.schedule_module;

public class ScheduleModule {
    private final double WAIT_TIME_WEIGHT = 0.2;
    private final double SCALE = 100.00; // 소수 셋째자리에서 반올림

    private double calculatePriority(double waitTime, double urgency) {
        double priority = WAIT_TIME_WEIGHT * waitTime + (1 - WAIT_TIME_WEIGHT) * urgency;
        return Math.round(priority * SCALE) / SCALE;
    }

    public void sortTask() {}
}
