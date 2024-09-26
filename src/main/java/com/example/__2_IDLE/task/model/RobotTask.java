package com.example.__2_IDLE.task.model;

import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.robot_manager.pos.Pos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobotTask {

  private Long id;
  private ArrayList<Pos> destinations;

  public static RobotTask of(Item item, ArrayList<Station> stations) {
    ArrayList<Pos> posList = new ArrayList<>();
    posList.add(item.getShelf().getPos());
    for (Station station : stations) {
      posList.add(station.getPos());
    }
    return RobotTask.builder()
        .destinations(posList)
        .build();
  }
}
