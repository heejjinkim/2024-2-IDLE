package com.example.__2_IDLE.ros.message_handler;

import com.example.__2_IDLE.ros.message_value.MessageValue;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ROSMessageHandler<T extends MessageValue> {
    protected String messageName;
    protected String messageType;

    abstract public T processMessage(String message);
    abstract protected T parsingMessage(String message);
}
