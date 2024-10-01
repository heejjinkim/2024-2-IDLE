package com.example.__2_IDLE.ros.message_handler.robot;

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

    public TopicRobotPoseMessageHandler(String namespace) {
        this.messageName = "/tf";
        this.messageType = "tf2_msgs/TFMessage";
        this.namespace = namespace;
    }
    @Override
    public RobotPoseMessageValue processMessage(String message) {
        return parsingMessage(message);
    }

    @Override
    protected RobotPoseMessageValue parsingMessage(String message) {
        RobotPoseMessageValue messageValue = new RobotPoseMessageValue();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(message);
            JsonNode transformsNode = rootNode.path("transforms").get(0);
            String frameId = transformsNode.path("header").path("frame_id").asText();

            if (frameId.equals("odom")) {
                log.info("frameId: {}", frameId);
                JsonNode translationNode = transformsNode
                        .path("transform")
                        .path("translation");
                double x = translationNode.path("x").asDouble();
                double y = translationNode.path("y").asDouble();
                messageValue.setX(Math.round(x * 100.0) / 100.0);
                messageValue.setY(Math.round(y * 100.0) / 100.0);
                return messageValue;
            } else {
                log.warn("Received message with different frame_id: {}", frameId);
                return messageValue;  // 빈 값을 반환하거나 예외를 던질 수 있음
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
