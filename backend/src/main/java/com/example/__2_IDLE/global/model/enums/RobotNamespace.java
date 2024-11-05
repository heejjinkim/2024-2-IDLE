package com.example.__2_IDLE.global.model.enums;

import lombok.Getter;

@Getter
public enum RobotNamespace {
    ROBOT1("tb1"),
    ROBOT2("tb2"),
//    ROBOT3("tb3"),
    ;

    private final String namespace;

    RobotNamespace(String namespace) {
        this.namespace = namespace;
    }
}