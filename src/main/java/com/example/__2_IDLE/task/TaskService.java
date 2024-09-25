package com.example.__2_IDLE.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

  public void taskAllocation(Long stationId, Long orderId) {

    log.info("station, order found success");

    // 하나의 온라인 주문에 관련된 로봇의 task들 생성

    // 로봇의 task 저장

    // 현재 로봇의 작업 상태 받아오기 - 로봇 관리 모듈로부터

    // 한 task에 대해 각 로봇마다의 비용 계산

    // 비용이 가장 작은 로봇의 작업 큐에 작업 삽입

  }
}
