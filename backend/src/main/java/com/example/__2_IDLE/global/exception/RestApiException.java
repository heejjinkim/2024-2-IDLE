package com.example.__2_IDLE.global.exception;

import com.example.__2_IDLE.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

  private final ErrorCode errorCode;
}
