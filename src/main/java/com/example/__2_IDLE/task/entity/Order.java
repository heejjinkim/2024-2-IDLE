package com.example.__2_IDLE.task.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  private Long id;
  private String customer;
  private boolean isSameDayDelivery;

  @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
  @Builder.Default
  private List<OrderItem> orderItems = new ArrayList<>();

  public static Order of(String customer, List<OrderItem> orderItems, boolean isSameDayDelivery) {
    Order order = Order.builder()
        .customer(customer)
        .orderItems(orderItems)
        .isSameDayDelivery(isSameDayDelivery)
        .build();

    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }

    return order;
  }

  private void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }
}
