package com.example.__2_IDLE.global;

import com.example.__2_IDLE.task.entity.Item;
import com.example.__2_IDLE.task.entity.Order;
import com.example.__2_IDLE.task.entity.OrderItem;
import com.example.__2_IDLE.task.entity.Station;
import com.example.__2_IDLE.task.repository.ProductRepository;
import com.example.__2_IDLE.task.repository.StationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final ProductRepository productRepository;
  private final StationRepository stationRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    initializeItems();
    initializeStations();
    createTestOrder();
  }

  private void initializeItems() {
    if (productRepository.count() == 0) {
      List<Item> items = List.of(
          Item.of(1L, "Product A", 100, 200),
          Item.of(2L, "Product B", 150, 250),
          Item.of(3L, "Product C", 200, 300)
      );
      productRepository.saveAll(items);
    }
  }

  private void initializeStations() {
    if (stationRepository.count() == 0) {
      List<Station> stations = List.of(
          Station.of(1L, "Station A", 100, 200),
          Station.of(2L, "Station B", 150, 250),
          Station.of(3L, "Station C", 200, 300)
      );
      stationRepository.saveAll(stations);
    }
  }

  private void createTestOrder() {
    List<Long> itemIds = List.of(1L, 2L);
    List<OrderItem> orderItems = itemIds.stream()
        .map(id -> productRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Test item not found for id: " + id)))
        .map(OrderItem::of)
        .toList();

    Order testOrder = Order.of("고객1", orderItems, false);
  }
}