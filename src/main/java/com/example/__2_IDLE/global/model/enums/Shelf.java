package com.example.__2_IDLE.global.model.enums;

import lombok.Getter;

@Getter
public enum Shelf {
  SHELF_A(1L, 100, 200), // TODO: 좌표 수정 필요
  SHELF_B(2L, 150, 250),
  SHELF_C(3L, 200, 300);

  private final Long id;
  private final int locationX;
  private final int locationY;

  Shelf(Long id, int locationX, int locationY) {
    this.id = id;
    this.locationX = locationX;
    this.locationY = locationY;
  }
}
