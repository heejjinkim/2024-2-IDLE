package com.example.__2_IDLE.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TaskErrorCode implements ErrorCode {
  STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Station not exists"),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not exists"),
  NO_AVAILABLE_ROBOT(HttpStatus.NOT_FOUND, "No available robot found for task allocation"),
  UNAVAILABLE_COST(HttpStatus.SERVICE_UNAVAILABLE, "No available cost function"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
