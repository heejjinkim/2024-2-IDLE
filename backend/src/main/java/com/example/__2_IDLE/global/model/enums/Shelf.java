package com.example.__2_IDLE.global.model.enums;

import com.example.__2_IDLE.global.model.Pose;
import lombok.Getter;

@Getter
public enum Shelf {
  SHELF_A(1L, new Pose(-1.0, -1.5)), // TODO: 좌표 수정 필요
  SHELF_B(2L, new Pose(-1.0, 1.5)),
  SHELF_C(3L, new Pose(1.0, -1.5)),
  SHELF_D(4L, new Pose(1.0, 1.5)),
  SHELF_E(5L, new Pose(3.0, -1.5)),
  SHELF_F(6L, new Pose(3.0, 1.5));
  ;

  private final Long id;
  private Pose pose;

  Shelf(Long id, Pose pose) {
    this.id = id;
    this.pose = pose;
  }
}
