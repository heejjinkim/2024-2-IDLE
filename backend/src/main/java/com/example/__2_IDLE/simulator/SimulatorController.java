package com.example.__2_IDLE.simulator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/simulator")
public class SimulatorController {
    private final SimulatorService simulatorService;

    @PostMapping("/start")
    public ResponseEntity<Void> start() {
        simulatorService.run();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/orders")
    public ResponseEntity<Void> addOrder() {
        simulatorService.addRandomOrders();
        return ResponseEntity.ok().build();
    }
}
