package com.example.__2_IDLE.task;

import com.example.__2_IDLE.entity.Item;
import com.example.__2_IDLE.entity.Order;
import com.example.__2_IDLE.entity.OrderItem;
import com.example.__2_IDLE.task.entity.Station;
import com.example.__2_IDLE.task.repository.OrderRepository;
import com.example.__2_IDLE.task.repository.StationRepository;
import com.example.__2_IDLE.exception.RestApiException;
import com.example.__2_IDLE.exception.errorcode.TaskErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

  private final StationRepository stationRepository;
  private final OrderRepository orderRepository;

  public void taskAllocation(Long stationId, Long orderId){

    Station station = stationRepository.findById(stationId)
        .orElseThrow(() -> new RestApiException(TaskErrorCode.STATION_NOT_FOUND));

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RestApiException(TaskErrorCode.ORDER_NOT_FOUND));

    log.info("station, order found success");

    // 하나의 온라인 주문에 관련된 로봇의 task들 생성
    List<OrderItem> orderItems = order.getOrderItems();
    for (OrderItem orderItem : orderItems) {
      Item item = orderItem.getItem();
      item.getLocationX();
      item.getLocationY();
    }

    // TODO: 로봇의 task는 어디에 저장? Redis?


    // 현재 로봇의 작업 상태 받아오기 - 로봇 관리 모듈로부터
    
    
    // 한 task에 대해 각 로봇마다의 비용 계산
    
    
    // 비용이 가장 작은 로봇의 작업 큐에 작업 삽입

  }
}
