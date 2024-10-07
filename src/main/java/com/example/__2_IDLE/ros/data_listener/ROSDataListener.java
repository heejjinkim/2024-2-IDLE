package com.example.__2_IDLE.ros.data_listener;

import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import com.example.__2_IDLE.ros.message_value.MessageValue;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public abstract class ROSDataListener {

    protected Ros ros;
    protected ROSMessageHandler messageHandler;

    public void connect() {
        ros.connect();
        log.info("completely connected ROS");
    }

    public abstract void listen();
}
