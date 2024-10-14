package com.example.__2_IDLE.global.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleTask {
    private int id;
    private LocalDateTime createTime;
    private double priority = 0;
    private int urgency = 0;
    private Order order;
}
