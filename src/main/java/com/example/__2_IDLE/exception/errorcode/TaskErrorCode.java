package com.example.__2_IDLE.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TaskErrorCode implements ErrorCode {
  STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Station not exists"),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not exists"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
