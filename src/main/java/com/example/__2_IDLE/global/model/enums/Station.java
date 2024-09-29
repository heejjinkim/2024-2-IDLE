package com.example.__2_IDLE.global.model.enums;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.STATION_NOT_FOUND;

import com.example.__2_IDLE.global.exception.RestApiException;
import java.util.Arrays;

import com.example.__2_IDLE.global.model.Pose;
import lombok.Getter;

@Getter
public enum Station {
  STATION_A(1L, "Station A", new Pose(10, 20)), // TODO: 좌표 수정 필요
  STATION_B(2L, "Station B", new Pose(15, 20)),
  STATION_C(3L, "Station C", new Pose(20, 30));

  private final Long id;
  private final String name;
  private Pose pose;

  Station(Long id, String name, Pose pose) {
    this.id = id;
    this.name = name;
    this.pose = pose;
  }

  public static Station getById(Long id) {
    return Arrays.stream(Station.values())
        .filter(station -> station.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RestApiException(STATION_NOT_FOUND));
  }
}
