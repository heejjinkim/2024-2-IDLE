package com.example.__2_IDLE.ros.data_listener.action;

import com.example.__2_IDLE.ros.message_value.MessageValue;

public interface ROSAction<T extends MessageValue> {

    T action();
}
