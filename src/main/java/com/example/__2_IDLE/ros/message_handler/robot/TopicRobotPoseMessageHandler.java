package com.example.__2_IDLE.ros.message_handler.robot;

import com.example.__2_IDLE.ros.message_value.MessageValue;
import com.example.__2_IDLE.ros.message_value.RobotPoseMessageValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

@Getter
@Setter
@Slf4j
public class TopicRobotPoseMessageHandler extends RobotMessageHandler{

    private boolean lock;

    public TopicRobotPoseMessageHandler(String namespace) {
        this.messageName = "/odom";
        this.messageType = "nav_msgs/Odometry";
        this.namespace = namespace;
        this.messageMethod = "topic";
        this.messageValue = new RobotPoseMessageValue();
    }

    @Override
    public void processMessage() {
        JsonNode positionNode = parsingMessage();
        Double x = positionNode.path("x").asDouble();
        Double y = positionNode.path("y").asDouble();

        String formatX = String.format("%.2f", x);
        String formatY = String.format("%.2f", y);

        messageValue.setValueByKey("x", formatX);
        messageValue.setValueByKey("y", formatY);
    }

    @Override
    protected JsonNode parsingMessage() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(message);
            log.info("message: {}", message);
            return rootNode
                    .path("pose")
                    .path("pose")
                    .path("position");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
