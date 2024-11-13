package com.example.__2_IDLE.ros.data_sender.publisher;

import edu.wpi.rail.jrosbridge.Ros;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ROSPublisher {
    protected Ros ros;
    protected String hostname;

    public ROSPublisher(Ros ros) {
        this.ros = ros;
        this.hostname = ros.getHostname();
    }

    public void connect() {
        ros.connect();
        log.info("completely connected ROS");
    }

    public abstract void publish();
}
