package com.example.__2_IDLE.ros.message_handler;

import com.example.__2_IDLE.ros.message_value.MessageValue;
import com.example.__2_IDLE.ros.message_value.RobotPoseMessageValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ROSMessageHandler {

    protected String message;
    protected String messageName;
    protected String messageType;
    protected String messageMethod;
    protected RobotPoseMessageValue messageValue;

    abstract public void processMessage();
    abstract protected JsonNode parsingMessage();
}
