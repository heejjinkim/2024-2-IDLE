package com.example.__2_IDLE.ros.data_listener.service;

import com.example.__2_IDLE.ros.message_value.MessageValue;

public interface ROSService<T extends MessageValue> {

    T service();
}
