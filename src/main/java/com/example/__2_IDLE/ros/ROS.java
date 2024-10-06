package com.example.__2_IDLE.ros;

import com.example.__2_IDLE.ros.data_listener.ROSDataListener;
import com.example.__2_IDLE.ros.data_listener.topic.TopicDataListener;
import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_handler.ROSMessageHandlerFactory;
import com.example.__2_IDLE.ros.message_value.MessageValue;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import lombok.AllArgsConstructor;

public class ROS<T extends MessageValue> {

    private String hostName;
    private Ros ros;
    private ROSDataListener dataListener;
    private ROSMessageHandler<T> messageHandler;
    private ROSMessageHandlerFactory messageHandlerFactory

    public ROS() {
        this.hostName = "192.168.111.128";
        this.ros = new Ros(hostName);
    }

    public void setComponent(String messageName) {
        ROSMessageHandler messageHandler = messageHandlerFactory.createMessageHandler(messageName);
    }

    public void setComponent(String messageName, String namespace) {
        messageHandlerFactory.createMessageHandler(messageName, namespace);
    }

    public T getValue() {
        // 데이터 수신 시작
        dataListener.connect();
        dataListener.listen();
        messageHandler.processMessage();

        return messageHandler.getValue();
    }
}
