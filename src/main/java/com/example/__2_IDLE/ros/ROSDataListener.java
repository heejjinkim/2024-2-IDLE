package com.example.__2_IDLE.ros;

import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_handler.TopicRobotPoseMessageHandler;
import com.example.__2_IDLE.ros.message_value.MessageValue;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
@Slf4j
public class ROSDataListener<T extends MessageValue> {

    private Ros ros;
    private String hostname;                     // localhost 또는 ip주소
    private T messageValue;           // ros 메시지로부터 필요한 값만 저장하는 변수(실시간 변화)

    public ROSDataListener(String hostname, ROSMessageHandler<T> rosMessageHandler) {
        this.hostname = hostname;
        this.ros = new Ros(hostname);
    }

    public void connect() {
        ros.connect();
        log.info("completely connected ROS");
    }

    public T topic(ROSMessageHandler<T> rosMessageHandler) {
        CompletableFuture<T> future = new CompletableFuture<>();
        String topicName = getTopicName(rosMessageHandler);
        String messageType = rosMessageHandler.getMessageType();

        Topic echoBack = new Topic(ros, topicName, messageType);
        echoBack.subscribe(new TopicCallback() { // topic subscribe
            @Override
            public void handleMessage(Message message) { // topic publisher가 publish할 경우 실행
                T value = rosMessageHandler.processMessage(message.toString());
                if (value != null) {
                    future.complete(value);
                    echoBack.unsubscribe();
                }
            }
        });

        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTopicName(ROSMessageHandler<T> rosMessageHandler) {
        String topicName;
        if (rosMessageHandler instanceof TopicRobotPoseMessageHandler) {
            topicName = ((TopicRobotPoseMessageHandler) rosMessageHandler).getNamespace() + rosMessageHandler.getMessageName();
        } else {
            topicName = rosMessageHandler.getMessageName();
        }
        return topicName;
    }
}
