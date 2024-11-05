package com.example.__2_IDLE.simulator;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/simulator")
public class SimulatorController {
    private final SimulatorService simulatorService;

    @PostMapping("/start")
    public void start() {
        simulatorService.run();
    }


}
