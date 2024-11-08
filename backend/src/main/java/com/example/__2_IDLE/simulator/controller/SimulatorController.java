package com.example.__2_IDLE.simulator.controller;

import com.example.__2_IDLE.robot.RobotService;
import com.example.__2_IDLE.simulator.service.SimulatorService;
import com.example.__2_IDLE.task_allocator.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/simulator")
public class SimulatorController {
    private final SimulatorService simulatorService;
    private final StationService stationService;
    private final RobotService robotService;

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

    // Todo: 피키 스테이션 정보 get(각 피킹스테이션 별로 현재 처리하고 있는 주문번호, 각 주문 처리 완료율)
    @GetMapping("/station")
    public ResponseEntity<String> getStation() {
        stationService.getAllStationStatus();
        return ResponseEntity.ok().build();
    }

    // todo: 로봇 상태 GET (각 로봇별로 운반하고 있는 물품 및 taskQueue에 들어있는 작업들 최대 2개, 로봇이 현재 작업 중인지 아닌지 여부)
//    @GetMapping("/robots")
//    public ResponseEntity<String> getRobots() {
//        robotService.
//    }

    // todo: 당일배송, 일반배송 처리량
}
