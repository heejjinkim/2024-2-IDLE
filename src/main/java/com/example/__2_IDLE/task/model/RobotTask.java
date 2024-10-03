package com.example.__2_IDLE.task.model;

import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.robot_manager.pos.Pos;
import java.util.LinkedList;
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
    private LinkedList<Pos> destinations;

    public static RobotTask of(Item item, LinkedList<Station> stations) {
        LinkedList<Pos> posList = new LinkedList<>();
        posList.add(item.getShelf().getPos());
        for (Station station : stations) {
            posList.add(station.getPos());
        }
        posList.add(item.getShelf().getPos());
        return RobotTask.builder()
            .destinations(posList)
            .build();
    }
}
