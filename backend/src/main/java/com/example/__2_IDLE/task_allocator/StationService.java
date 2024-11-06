package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class StationService {

    public List<PickingTask> getNotAllocatedTaskSameItemOf(PickingTask task, Station station) {
        List<PickingTask> result = new ArrayList<>();
        Item item = task.getItem();

        List<PickingTask> tasks = station.getTasks();
        for (PickingTask stationTask : tasks) {
            if (stationTask.getItem() == item && !stationTask.isAllocated()) {
                result.add(stationTask);
            }
        }

        return result;
    }

    // 다른 Station 정보 가져오기
    public List<Station> getAllStationsExcept(Station station) {
        return Arrays.stream(Station.values()).filter(s -> !s.equals(station))
                .toList();
    }

    public Optional<Station> getStationHasMaxTimeCost() {
        return Arrays.stream(Station.values())
                .max(Comparator.comparing(Station::countUnallocatedTask));
    }

    public Optional<Station> getStationHasTask(PickingTask task) {
        return Arrays.stream(Station.values())
                .filter(station -> station.hasTask(task))
                .findFirst();
    }
}
