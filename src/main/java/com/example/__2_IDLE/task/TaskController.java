package com.example.__2_IDLE.task;

import com.example.__2_IDLE.task.entity.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("task")
public class TaskController {

  private final TaskService taskService;

  @GetMapping("/allocation")
  public void allocation() {
    // Given: 피킹 스테이션 아이디, 온라인 주문 아이디
    // 주문 데이터가 저장되는 곳은 어디인가?
    Long orderId = 1L;
    Long stationId = 1L;

    taskService.taskAllocation(stationId, orderId);

  }
}
