package com.example.__2_IDLE.task.model;

import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobotTask {

  private Long id;
  private int itemX;
  private int itemY;
  private int stationX;
  private int stationY;

  public static RobotTask of(Item item, Station station) {
    return RobotTask.builder()
        .itemX(item.getShelf().getLocationX())
        .itemY(item.getShelf().getLocationY())
        .stationX(station.getLocationX())
        .stationY(station.getLocationY())
        .build();
  }
}
