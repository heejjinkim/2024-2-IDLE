package com.example.__2_IDLE.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Item {
  @Id
  private Long id;
  private String name;
  private int locationX;
  private int locationY;

  public static Item of(Long id, String name, int locationX, int locationY) {
    return Item.builder()
        .id(id)
        .name(name)
        .locationX(locationX)
        .locationY(locationY)
        .build();
  }
}
