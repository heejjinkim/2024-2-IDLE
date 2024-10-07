package com.example.__2_IDLE.ros.message_handler.robot;

import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_value.MessageValue;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class RobotMessageHandler extends ROSMessageHandler {
    protected String namespace;

}
