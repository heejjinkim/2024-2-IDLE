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
public class TopicDataListener extends ROSDataListener implements ROSTopic {

    private CompletableFuture<Void> future;

    public TopicDataListener(Ros ros, ROSMessageHandler messageHandler) {
        super(ros);
        this.messageHandler = messageHandler;
        this.future = new CompletableFuture<>();
    }

    @Override
    public void listen() {
        topic();
        // 콜백이 완료될 때까지 대기
        try {
            future.get(); // future가 완료될 때까지 대기
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error("Error while waiting for message processing", e);
        }
    }

    @Override
    public void topic() {
        String topicName = getTopicName();
        String messageType = messageHandler.getMessageType();

        Topic echoBack = new Topic(ros, topicName, messageType);
        echoBack.subscribe(new TopicCallback() { // topic subscribe
            @Override
            public void handleMessage(Message message) { // topic publisher가 publish할 경우 실행
                String messageToString = message.toString();
                messageHandler.setMessage(messageToString);
                future.complete(null); // future 완료하여 대기 중인 스레드를 해제
                echoBack.unsubscribe();
            }
        });
    }

    private String getTopicName() {
        String topicName;
        if (messageHandler instanceof TopicRobotPoseMessageHandler) {
            topicName = ((TopicRobotPoseMessageHandler) messageHandler).getNamespace() + messageHandler.getMessageName();
        } else {
            topicName = messageHandler.getMessageName();
        }
        return topicName;
    }
}
