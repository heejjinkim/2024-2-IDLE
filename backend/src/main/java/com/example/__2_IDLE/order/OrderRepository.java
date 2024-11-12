package com.example.__2_IDLE.order;

import com.example.__2_IDLE.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    private List<Order> orders = new ArrayList<>();

    public void saveAll(List<Order> orders) {
        this.orders.addAll(orders);
    }

    public Optional<Order> findById(Long orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }
}
