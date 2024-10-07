package com.example.__2_IDLE.test_allo_sim.manager;

import com.example.__2_IDLE.test_allo_sim.Pose;
import com.example.__2_IDLE.test_allo_sim.Station;
import com.example.__2_IDLE.test_allo_sim.Task;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Queue;

@Slf4j
@Getter
public class StationManager {

    private ArrayList<Station> stationList = new ArrayList<>();

    public void createStation(Pose pose, String name) {
        this.stationList.add(new Station(pose, name));
    }

    public Station getStationForTask() {
        Station targetStation = null;
        int minSize = Integer.MAX_VALUE;
        for (Station station : stationList) {
            Queue<Task> taskQueue = station.getTaskQueue();
            if (taskQueue.size() < minSize) {
                minSize = taskQueue.size();
                targetStation = station;
            }
        }
        log.info("선택된 station: {}", targetStation.getName());
        return targetStation;
    }


}
