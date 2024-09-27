package com.example.__2_IDLE.global.model.enums;

import lombok.Getter;

@Getter
public enum Item {
  ITEM_A(1L, "Product A", Shelf.SHELF_A),
  ITEM_B(2L, "Product B", Shelf.SHELF_B),
  ITEM_C(3L, "Product C", Shelf.SHELF_C);

  private final Long id;
  private final String name;
  private final Shelf shelf; 

  Item(Long id, String name, Shelf shelf) {
    this.id = id;
    this.name = name;
    this.shelf = shelf;
  }
}
