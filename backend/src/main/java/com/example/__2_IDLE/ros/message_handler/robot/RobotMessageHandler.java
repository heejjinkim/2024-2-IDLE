package com.example.__2_IDLE.ros.message_handler.robot;

import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_value.MessageValue;

public abstract class RobotMessageHandler<T extends MessageValue> extends ROSMessageHandler {
    protected String namespace;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
