package com.example.__2_IDLE.ros.message_handler;

import com.example.__2_IDLE.ros.message_value.MessageValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ROSMessageHandler<T extends MessageValue> {

    protected String message;
    protected String messageName;
    protected String messageType;

    abstract public void processMessage();
    abstract protected JsonNode parsingMessage();
    abstract public T getValue();
}
