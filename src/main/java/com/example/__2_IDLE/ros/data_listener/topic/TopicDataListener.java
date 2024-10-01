package com.example.__2_IDLE.ros.data_listener.topic;

import com.example.__2_IDLE.ros.data_listener.ROSDataListener;
import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_handler.robot.TopicRobotPoseMessageHandler;
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
public class TopicDataListener<T extends MessageValue> extends ROSDataListener implements ROSTopic<T> {

    public TopicDataListener(Ros ros) {
        super(ros);
    }

    @Override
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
