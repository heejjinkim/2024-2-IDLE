package com.example.__2_IDLE.task_allocator.controller;

import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.task_allocator.model.PickingTask;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StationController {

    // 전체 Station 리스트 가져오기
    public List<Station> getAllStations() {
        return List.of(Station.values());
    }

    // 다른 Station 정보 가져오기
    public List<Station> getAllStationsExcept(Station station) {
        return Arrays.stream(Station.values()).filter(s ->
                !s.equals(station))
                .toList();
    }

    // 가장 시간이 오래 드는 Station 가져오기
    public Optional<Station> getStationHasMaxTimeCost() {
        return Arrays.stream(Station.values())
                .max(Comparator.comparing(Station::getTimeCost));
    }

    public Long getStationCount() {
        return (long) List.of(Station.values()).size();
    }

    public void addToUnallocatedList(Long stationId, PickingTask pickingTask) {
        Station station = Station.getById(stationId);
        station.addToUnallocatedList(pickingTask);
    }

    public Optional<Station> getStationHasTask(PickingTask task) {
        return Arrays.stream(Station.values())
                .filter(station -> station.hasTaskInUnallocatedList(task))
                .findFirst();
    }
}
