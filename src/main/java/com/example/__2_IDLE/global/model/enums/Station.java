package com.example.__2_IDLE.global.model.enums;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.STATION_NOT_FOUND;

import com.example.__2_IDLE.global.exception.RestApiException;
import java.util.Arrays;

import com.example.__2_IDLE.robot_manager.pos.Pos;
import lombok.Getter;

@Getter
public enum Station {
  STATION_A(1L, "Station A", new Pos(5, 0)), // TODO: 좌표 수정 필요
  STATION_B(2L, "Station B", new Pos(15, 0)),
  STATION_C(3L, "Station C", new Pos(20, 0));

  private final Long id;
  private final String name;
  private Pos pos;

  Station(Long id, String name, Pos pos) {
    this.id = id;
    this.name = name;
    this.pos = pos;
  }

  public static Station getById(Long id) {
    return Arrays.stream(Station.values())
        .filter(station -> station.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RestApiException(STATION_NOT_FOUND));
  }
}
