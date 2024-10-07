package com.example.__2_IDLE.ros.message_value;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * ros의 "/tf" topic message의 x, y 값 저장
 */
@Getter
@Setter
public class RobotPoseMessageValue implements MessageValue {

    private Map<String, String> values = new HashMap<>(); // TODO x, y 저장

    @Override
    public String getValueByKey(String key) {
        return values.get(key);
    }

    @Override
    public void setValueByKey(String key, String value) {
        values.put(key, value);
    }
}
