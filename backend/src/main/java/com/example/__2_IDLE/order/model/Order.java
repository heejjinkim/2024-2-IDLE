package com.example.__2_IDLE.order.model;

import com.example.__2_IDLE.global.model.enums.Item;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Builder
public class Order {

    private Long id;
    private boolean isSameDayDelivery;
    private Customer customer;
    private boolean isCompleted;
    private int originalItemCount;
    private int completedItemCount;

    @Builder.Default
    private List<Item> orderItems = new ArrayList<>();

    public static Order of(Long id, Customer customer, List<Item> orderItems, boolean isSameDayDelivery) {
        return Order.builder()
                .id(id)
                .customer(customer)
                .orderItems(orderItems)
                .isSameDayDelivery(isSameDayDelivery)
                .completedItemCount(0)
                .originalItemCount(orderItems.size())
                .isCompleted(false)
                .build();
    }

    public void updateCompletedItemCount() {
        completedItemCount++;
        if (completedItemCount == originalItemCount) {
            isCompleted = true;
            log.info("Order completed! {}", id);
        }
        log.info("update order {} ", id);
    }

}
