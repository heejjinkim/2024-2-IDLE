package com.example.__2_IDLE.schedule.model;

import com.example.__2_IDLE.order.model.Order;
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
