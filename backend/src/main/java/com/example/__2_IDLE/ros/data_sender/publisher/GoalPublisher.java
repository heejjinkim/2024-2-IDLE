package com.example.__2_IDLE.ros.data_sender.publisher;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.ros.data_sender.message.GoalMessage;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoalPublisher extends ROSPublisher {

    private String namespace;
    private Pose destination;

    public GoalPublisher(Ros ros, String namespace, Pose destination) {
        super(ros);
        this.namespace = namespace;
        this.destination = destination;
    }
    @Override
    public void publish() {
        String topicName = "/" + namespace + "/goal_pose";
        String messageType = "geometry_msgs/PoseStamped";

        Topic goalTopic = new Topic(ros, topicName, messageType);

        // 목적지 메시지를 생성
        GoalMessage goalMessage = new GoalMessage(destination);

        // 메시지 발행
        goalTopic.publish(goalMessage.toRosMessage());
    }
}