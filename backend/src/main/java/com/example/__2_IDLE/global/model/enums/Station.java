package com.example.__2_IDLE.global.model.enums;

import com.example.__2_IDLE.global.exception.RestApiException;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.STATION_NOT_FOUND;

@Getter
public enum Station {
    STATION_A(1L, "Station A", new Pose(-3, -1.5)), // TODO: 좌표 수정 필요
    STATION_B(2L, "Station B", new Pose(-3, 0)),
    STATION_C(3L, "Station C", new Pose(-3, 1.5));

    private final Long id;
    private final String name;
    private Pose pose;
    private List<PickingTask> tasks = new ArrayList<>();

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

    public void addTask(PickingTask pickingTask) {
        tasks.add(pickingTask);
    }

    public List<PickingTask> getTasksByItem(Item targetItem) {
        return tasks.stream()
                .filter(task -> task.getItem() == targetItem)
                .filter(task -> !task.isAllocated())
                .collect(Collectors.toList());
    }

    public boolean hasTask(PickingTask task) { // todo: unallocated가 아니라 전체 작업인 Container 대상으로
        return tasks.contains(task);
    }

    public int countUnallocatedTask() {
        return (int) tasks.stream()
                .filter(task -> !task.isAllocated())
                .count();
    }

    public Optional<PickingTask> pickOneUnallocatedTask() {
        return tasks.stream()
                .filter(task -> !task.isAllocated())
                .findFirst();
    }

    public List<Long> completeTask(PickingTask pickingTask) {
        List<Long> removedIds = tasks.stream()
                .filter(task -> task.getItem().equals(pickingTask.getItem()))
                .filter(PickingTask::isAllocated)
                .map(PickingTask::getId)
                .toList();

        tasks.removeIf(task -> task.getItem().equals(pickingTask.getItem()));

        return removedIds;
    }
}
