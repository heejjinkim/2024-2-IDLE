package com.example.__2_IDLE.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum RobotErrorCode implements ErrorCode {
    ROBOT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 로봇을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
