package com.example.__2_IDLE.ros.message_handler;

import com.example.__2_IDLE.ros.message_handler.robot.TopicRobotPoseMessageHandler;
import com.example.__2_IDLE.ros.message_value.MessageValue;

public class ROSMessageHandlerFactory {

    public  ROSMessageHandler createMessageHandler(String messageName) {
        throw new IllegalArgumentException("Unknown message name: " + messageName);
    }

    public  ROSMessageHandler createMessageHandler(String messageName, String namespace) {
        if (messageName.equals("/odom")) {
            return new TopicRobotPoseMessageHandler(namespace);
        }
        throw new IllegalArgumentException("Unknown message name: " + messageName);
    }
}
