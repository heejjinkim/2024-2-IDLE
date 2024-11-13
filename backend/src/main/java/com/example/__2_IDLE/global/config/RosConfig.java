package com.example.__2_IDLE.global.config;
import edu.wpi.rail.jrosbridge.Ros;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RosConfig {

    @Bean
    public Ros ros() {
        return new Ros("localhost");
    }
}
