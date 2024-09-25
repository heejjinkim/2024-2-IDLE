package com.example.__2_IDLE.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  private Long id;

  private boolean isSameDayDelivery;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
  @Builder.Default
  private List<OrderItem> orderItems = new ArrayList<>();

  public static Order of(Customer customer, List<OrderItem> orderItems, boolean isSameDayDelivery) {
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
