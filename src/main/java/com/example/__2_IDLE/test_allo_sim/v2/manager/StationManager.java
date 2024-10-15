package com.example.__2_IDLE.test_allo_sim.v2.manager;

import com.example.__2_IDLE.test_allo_sim.v2.model.Pose;
import com.example.__2_IDLE.test_allo_sim.v2.model.Station;
import com.example.__2_IDLE.test_allo_sim.v2.model.Task;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Slf4j
public class StationManager {

    private List<Station> stationList;

    public StationManager(int stationCount, int stationInterval, int y, int pickingTime) {
        stationList = new ArrayList<Station>();
        makeStations(stationCount, stationInterval, y, pickingTime);
    }

    public void printUnallocatedTasks() {
        for (Station station : stationList) {
            station.printUnallocatedTasks();
        }
    }

    private void makeStations(int stationCount, int stationInterval, int y, int pickingTime) {
        for (int i = 0; i < stationCount; i++) {
            Pose pose = new Pose(i * stationInterval, y);
            Station station = new Station("station" + i, pose, new HashMap<>(), new ArrayList<>(), pickingTime);
            stationList.add(station);
        }
    }

    public Station getStationByIndex(int index) {
        return stationList.get(index);
    }

    public void allocateTask(Task task) {
        for (Station station : stationList) {
            if (station.hasTaskInUnallocatedTasks(task)) {
              station.allocateTask(task);
              task.setStation(station);
            }
        }
    }

    public boolean hasUnallocatedTask() {
        boolean hasUnallocatedTask = false;
        for (Station station : stationList) {
            if (!station.getUnallocatedTasks().isEmpty()) {
                hasUnallocatedTask = true;
                break;
            }
        }
        return hasUnallocatedTask;
    }
}
