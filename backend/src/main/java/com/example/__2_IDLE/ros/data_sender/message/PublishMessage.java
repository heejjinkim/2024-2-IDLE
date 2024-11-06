package com.example.__2_IDLE.ros.data_sender.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.rail.jrosbridge.messages.Message;

public abstract class PublishMessage {

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    public Message toRosMessage() {
        return new Message(toJson());
    }
}
