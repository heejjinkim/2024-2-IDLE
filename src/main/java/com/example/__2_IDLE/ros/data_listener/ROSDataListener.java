package com.example.__2_IDLE.ros.data_listener;

import com.example.__2_IDLE.ros.message_handler.ROSMessageHandler;
import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ROSDataListener {

    protected Ros ros;
    protected String hostname;                     // localhost 또는 ip주소

    public ROSDataListener(Ros ros) {
        this.ros = ros;
        this.hostname = ros.getHostname();
    }

    public void connect() {
        ros.connect();
        log.info("completely connected ROS");
    }
}
