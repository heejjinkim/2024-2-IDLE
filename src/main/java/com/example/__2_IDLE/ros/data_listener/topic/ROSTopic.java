package com.example.__2_IDLE.ros.data_listener.topic;

import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_value.MessageValue;

public interface ROSTopic<T extends MessageValue> {

    T topic(ROSMessageHandler<T> rosMessageHandler);
}
