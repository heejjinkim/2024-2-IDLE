package com.example.__2_IDLE.ros.message_handler.robot;

import com.example.__2_IDLE.ros.message_value.MessageValue;
import com.example.__2_IDLE.ros.message_value.RobotPoseMessageValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class TopicRobotPoseMessageHandler extends RobotMessageHandler<RobotPoseMessageValue>{

    private RobotPoseMessageValue messageValue = new RobotPoseMessageValue();
    private boolean lock;

    public TopicRobotPoseMessageHandler(String namespace) {
        this.messageName = "/odom";
        this.messageType = "nav_msgs/Odometry";
        this.namespace = namespace;
        this.messageMethod = "topic";
    }

    @Override
    public void processMessage() {
        JsonNode positionNode = parsingMessage();
        double x = positionNode.path("x").asDouble();
        double y = positionNode.path("y").asDouble();
        messageValue.setX(x);
        messageValue.setY(y);
    }

    @Override
    protected JsonNode parsingMessage() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(message);
            JsonNode positionNode = rootNode
                    .path("pose")
                    .path("pose")
                    .path("position");
            return positionNode;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RobotPoseMessageValue getValue() {
        return messageValue;
    }
}
