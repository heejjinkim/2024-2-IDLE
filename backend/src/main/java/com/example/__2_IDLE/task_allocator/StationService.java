package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.order.OrderService;
import com.example.__2_IDLE.order.model.Order;
import com.example.__2_IDLE.simulator.response.OrderStatus;
import com.example.__2_IDLE.simulator.response.StationStatusResponse;
import com.example.__2_IDLE.task_allocator.model.PickingTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class StationService {

    private final OrderService orderService;

    public List<PickingTask> getNotAllocatedTaskSameItemOf(PickingTask task, Station station) {
        List<PickingTask> result = new ArrayList<>();
        Item item = task.getItem();

        List<PickingTask> tasks = station.getTasks();
        for (PickingTask stationTask : tasks) {
            if (stationTask.getItem() == item && !stationTask.isAllocated()) {
                result.add(stationTask);
            }
        }

        return result;
    }

    // 다른 Station 정보 가져오기
    public List<Station> getAllStationsExcept(Station station) {
        return Arrays.stream(Station.values()).filter(s -> !s.equals(station))
                .toList();
    }

    public Optional<Station> getStationHasMaxTimeCost() {
        return Arrays.stream(Station.values())
                .max(Comparator.comparing(Station::countUnallocatedTask));
    }

    public Optional<Station> getStationHasTask(PickingTask task) {
        return Arrays.stream(Station.values())
                .filter(station -> station.hasTask(task))
                .findFirst();
    }

    public List<StationStatusResponse> getAllStationStatus() {
        List<StationStatusResponse> stationStatuses = new ArrayList<>();

        for (Station station : Station.values()) {
            Map<Long, OrderStatus> orderStatusMap = buildOrderStatusMap(station);
            stationStatuses.add(createStationStatusResponse(station, orderStatusMap));
        }
        return stationStatuses;
    }

    private Map<Long, OrderStatus> buildOrderStatusMap(Station station) {
        Map<Long, OrderStatus> orderStatusMap = new HashMap<>();  // 주문 ID, 주문 상태 Map

        for (PickingTask task : station.getTasks()) {
            Long orderId = task.getOrderId();
            Order order = orderService.findOrderById(orderId);

            // 주문이 이미 맵에 있다면 기존 상태 업데이트, 없다면 새로운 상태 생성
            orderStatusMap.computeIfAbsent(order.getId(), id ->
                    OrderStatus.builder()
                            .id(order.getId())
                            .originalItemCount(order.getOriginalItemCount())
                            .completedItemCount(order.getCompletedItemCount())
                            .build()
            );
        }
        return orderStatusMap;
    }

    private StationStatusResponse createStationStatusResponse(Station station, Map<Long, OrderStatus> orderStatusMap) {
        return StationStatusResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .orders(new ArrayList<>(orderStatusMap.values()))
                .build();
    }
}
