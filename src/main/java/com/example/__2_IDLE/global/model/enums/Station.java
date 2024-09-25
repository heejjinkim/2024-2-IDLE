package com.example.__2_IDLE.global.model.enums;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.STATION_NOT_FOUND;

import com.example.__2_IDLE.global.exception.RestApiException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Station {
  STATION_A(1L, "Station A", 100, 200), // TODO: 좌표 수정 필요
  STATION_B(2L, "Station B", 150, 250),
  STATION_C(3L, "Station C", 200, 300);

  private final Long id;
  private final String name;
  private final int locationX;
  private final int locationY;

  Station(Long id, String name, int locationX, int locationY) {
    this.id = id;
    this.name = name;
    this.locationX = locationX;
    this.locationY = locationY;
  }

  public static Station getById(Long id) {
    return Arrays.stream(Station.values())
        .filter(station -> station.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RestApiException(STATION_NOT_FOUND));
  }
}
