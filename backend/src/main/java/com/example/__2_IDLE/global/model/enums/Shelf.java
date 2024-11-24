package com.example.__2_IDLE.global.model.enums;

import com.example.__2_IDLE.global.model.Pose;
import lombok.Getter;

@Getter
public enum Shelf {
  SHELF_A(1L, new Pose(8.0, -1.0)),
  SHELF_B(2L, new Pose(2.5, -1.0)),
  SHELF_C(3L, new Pose(-3.0, -1.0)),
  SHELF_D(4L, new Pose(-9.0, -1.0)),
  SHELF_E(5L, new Pose(8.5, 4.0)),
  SHELF_F(6L, new Pose(2.5, 4.0)),
  SHELF_G(7L, new Pose(-3.0, 4.0)),
  SHELF_H(8L, new Pose(-9.0, 4.0)),
  ;

  private final Long id;
  private Pose pose;

  Shelf(Long id, Pose pose) {
    this.id = id;
    this.pose = pose;
  }
}
