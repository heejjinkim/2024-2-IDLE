package com.example.__2_IDLE.task.model;

import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.global.model.Pose;

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
    private LinkedList<Pose> destinations;
    private LinkedList<Station> stations;
    private Item item;

    public static RobotTask of(Item item, LinkedList<Station> stations) {
        LinkedList<Pose> poseList = new LinkedList<>();
        poseList.add(item.getShelf().getPose());
        for (Station station : stations) {
            poseList.add(station.getPose());
        }
        poseList.add(item.getShelf().getPose());
        return RobotTask.builder()
                .destinations(poseList)
                .stations(stations)
                .item(item)
                .build();
    }

    public String getStationNameByIndex(int destinationIndex) {
        if (destinationIndex > 0 && destinationIndex <= stations.size()) {
            return stations.get(destinationIndex - 1).name();
        }
        return "Unknown Station";
    }
}
