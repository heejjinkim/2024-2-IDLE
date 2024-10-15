package com.example.__2_IDLE.test_allo_sim.v2.manager;

import com.example.__2_IDLE.test_allo_sim.v2.model.Item;
import com.example.__2_IDLE.test_allo_sim.v2.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
public class OrderManager {

    private List<Order> orderList;

    public OrderManager(int maxItemsPerOrder, int orderCount) {
        orderList = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }
}
