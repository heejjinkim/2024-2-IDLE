package com.example.__2_IDLE.ros.message_value;

import lombok.Getter;
import lombok.Setter;

/**
 * ros의 "/tf" topic message의 x, y 값 저장
 */
@Getter
@Setter
public class RobotPoseMessageValue implements MessageValue {
    private double x;
    private double y;
}
