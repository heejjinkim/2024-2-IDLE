package com.example.__2_IDLE.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TaskErrorCode implements ErrorCode {
  STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "스테이션을 찾을 수 없습니다."),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
  SIMULATOR_NOT_RUNNING(HttpStatus.SERVICE_UNAVAILABLE, "시뮬레이터가 실행 중이지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
