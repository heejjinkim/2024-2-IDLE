package com.example.__2_IDLE.order;

import com.example.__2_IDLE.global.exception.RestApiException;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.order.model.Customer;
import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.simulator.response.DeliveryStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.__2_IDLE.global.exception.errorcode.TaskErrorCode.ORDER_NOT_FOUND;
import static com.example.__2_IDLE.schedule.ScheduleService.WAVE_SIZE;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> generateRandomOrders() {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();

        for (long i = 0; i < WAVE_SIZE; i++) {
            Customer customer = new Customer();
            List<Item> productList = new ArrayList<>();
            List<Item> items = List.of(Item.values());

            int numberOfProducts = random.nextInt(5) + 1;
            for (int j = 0; j < numberOfProducts; j++) {
                int randomIndex = random.nextInt(items.size());
                productList.add(items.get(randomIndex));
            }

            boolean oneDayShipping = random.nextBoolean();
            orders.add(Order.of(i + 1, customer, productList, oneDayShipping));
        }
        orderRepository.saveAll(orders);
        return orders;
    }

    public void markItemAsCompleted(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RestApiException(ORDER_NOT_FOUND));

        order.updateCompletedItemCount();
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RestApiException(ORDER_NOT_FOUND));
    }

    public DeliveryStatusResponse getProcessedDeliveryCount() {
        List<Order> allOrders = orderRepository.findAll();

        int sameDayDeliveryCount = 0;
        int standardDeliveryCount = 0;

        for (Order order : allOrders) {
            if (order.isCompleted()) {
                if (order.isSameDayDelivery()) {
                    sameDayDeliveryCount++;
                } else {
                    standardDeliveryCount++;
                }
            }
        }

        return DeliveryStatusResponse.builder()
                .SameDayDeliveryCount(sameDayDeliveryCount)
                .StandardDeliveryCount(standardDeliveryCount)
                .build();
    }
}
