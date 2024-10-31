package com.example.__2_IDLE.global.model.enums;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.STATION_NOT_FOUND;

import com.example.__2_IDLE.global.exception.RestApiException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.task_allocator.model.AllocatedTaskList;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import com.example.__2_IDLE.task_allocator.model.UnallocatedTaskList;
import lombok.Getter;

@Getter
public enum Station {
  STATION_A(1L, "Station A", new Pose(-3.5, 2.0)), // TODO: 좌표 수정 필요
  STATION_B(2L, "Station B", new Pose(-3.5, 0.0)),
  STATION_C(3L, "Station C", new Pose(-3.5, -2.0));

  private final Long id;
  private final String name;
  private Pose pose;
  private final UnallocatedTaskList unallocatedTaskList = new UnallocatedTaskList();
  private final AllocatedTaskList allocatedTaskList = new AllocatedTaskList();
  // todo: 할당된 작업들 보관 장소 만들어야
  private int timeCost;

  Station(Long id, String name, Pose pose) {
    this.id = id;
    this.name = name;
    this.pose = pose;
    this.timeCost = 0;
  }

  public static Station getById(Long id) {
    return Arrays.stream(Station.values())
        .filter(station -> station.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RestApiException(STATION_NOT_FOUND));
  }

  public void addToUnallocatedList(PickingTask pickingTask) {
    unallocatedTaskList.add(pickingTask);
  }

  public List<PickingTask> getTasksByItem(Item targetItem) {
    return unallocatedTaskList.getTasksByItem(targetItem);
  }

  public Stream<PickingTask> unallocatedTaskStream() {
    return unallocatedTaskList.stream();
  }

  public boolean hasTaskInUnallocatedList(PickingTask task) {
    return unallocatedTaskList.has(task);
  }

  public void allocateTask(PickingTask pickingTask) {
    allocatedTaskList.addTask(pickingTask);
    unallocatedTaskList.remove(pickingTask);
  }

  public void completeTask(PickingTask pickingTask) {
    allocatedTaskList.removeSameItemTasks(pickingTask);
  }
}
