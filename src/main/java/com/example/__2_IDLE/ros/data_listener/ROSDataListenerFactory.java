package com.example.__2_IDLE.ros.data_listener;

import com.example.__2_IDLE.ros.data_listener.topic.TopicDataListener;

public class ROSDataListenerFactory {

    public ROSDataListener createROSDataListener(String messageMethod) {
        if (messageMethod.equals("topic")) {
            return new TopicDataListener();
        }
        throw new IllegalArgumentException("Invalid message method: " + messageMethod);
    }
}
