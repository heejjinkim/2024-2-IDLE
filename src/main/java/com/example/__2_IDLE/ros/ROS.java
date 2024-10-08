package com.example.__2_IDLE.ros;

import com.example.__2_IDLE.ros.data_listener.ROSDataListener;
import com.example.__2_IDLE.ros.data_listener.ROSDataListenerFactory;
import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_handler.ROSMessageHandlerFactory;
import com.example.__2_IDLE.ros.message_value.MessageValue;
import edu.wpi.rail.jrosbridge.Ros;

public class ROS {

    private final Ros ros;
    private final ROSMessageHandlerFactory messageHandlerFactory;
    private final ROSDataListenerFactory dataListenerFactory;

    public ROS(String hostName) {
        this.ros = new Ros(hostName);
        this.messageHandlerFactory = new ROSMessageHandlerFactory();
        this.dataListenerFactory = new ROSDataListenerFactory();
    }

    public MessageValue getMessageFromROS(String messageName) {
        ROSMessageHandler messageHandler = messageHandlerFactory.createMessageHandler(messageName);
        return process(messageHandler);
    }

    public MessageValue getMessageFromROS(String messageName, String namespace) {
        ROSMessageHandler messageHandler = messageHandlerFactory.createMessageHandler(messageName, namespace);
        return process(messageHandler);
    }

    private MessageValue process(ROSMessageHandler messageHandler) {
        ROSDataListener dataListener = createDataListener(messageHandler);
        setDataListener(messageHandler, dataListener);
        setupConnection(dataListener);
        return getMessageValue(messageHandler);
    }

    private ROSDataListener createDataListener(ROSMessageHandler messageHandler) {
        String messageMethod = messageHandler.getMessageMethod();
        return dataListenerFactory.createROSDataListener(messageMethod);
    }

    private void setDataListener(ROSMessageHandler messageHandler, ROSDataListener dataListener) {
        dataListener.setRos(ros);
        dataListener.setMessageHandler(messageHandler);
    }

    private void setupConnection(ROSDataListener dataListener) {
        dataListener.connect();
        dataListener.listen();
    }

    public MessageValue getMessageValue(ROSMessageHandler messageHandler) {
        messageHandler.processMessage();
        return messageHandler.getMessageValue();
    }
}
