package com.example.__2_IDLE.simulator.controller;

import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.robot.RobotService;
import com.example.__2_IDLE.simulator.response.DeliveryStatusResponse;
import com.example.__2_IDLE.simulator.response.RobotStatusResponse;
import com.example.__2_IDLE.simulator.response.StationStatusResponse;
import com.example.__2_IDLE.simulator.service.SimulatorService;
import com.example.__2_IDLE.task_allocator.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/simulator")
public class SimulatorController {
    private final SimulatorService simulatorService;
    private final StationService stationService;
    private final RobotService robotService;
    private final OrderService orderService;

    // 시뮬레이션 시작
    @PostMapping("/start")
    public ResponseEntity<Void> start() {
        simulatorService.run();
        return ResponseEntity.ok().build();
    }

    // 랜덤 주문 추가 생성
    @PostMapping("/add/orders")
    public ResponseEntity<Void> addOrder() {
        simulatorService.addRandomOrders();
        return ResponseEntity.ok().build();
    }

    // 피킹 스테이션 정보 반환
    @GetMapping("/station")
    public ResponseEntity<List<StationStatusResponse>> getStation() {
        stationService.getAllStationStatus();
        return ResponseEntity.ok(stationService.getAllStationStatus());
    }

    // 로봇 상태 정보 반환
    @GetMapping("/robots")
    public ResponseEntity<List<RobotStatusResponse>> getRobots() {
        return ResponseEntity.ok(robotService.getRobotsStatus());
    }

    // 당일배송, 일반배송 처리량 반환
    @GetMapping("/delivery")
    public ResponseEntity<DeliveryStatusResponse> getProcessedDeliveryCount() {
        return ResponseEntity.ok(orderService.getProcessedDeliveryCount());
    }
}
