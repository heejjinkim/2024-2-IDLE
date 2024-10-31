package com.example.__2_IDLE.simulator;


import com.example.__2_IDLE.global.model.Customer;
import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.global.model.ScheduleTask;
import com.example.__2_IDLE.global.model.enums.Item;
import com.example.__2_IDLE.global.model.enums.Station;
import com.example.__2_IDLE.global.model.robot.Robot;
import com.example.__2_IDLE.global.model.robot.RobotRepository;
import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.task.TaskModule;
import com.example.__2_IDLE.task_allocator.TaskAllocator;
import org.springframework.scheduling.config.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatorService {

    private final ScheduleModule scheduleModule = new ScheduleModule();

    public void run(){
        List<Order> orders = generateRandomOrders(10);

        List<ScheduleTask> tasks = createScheduleTasks(orders);
        scheduleModule.addAllTask(tasks);
        scheduleModule.run();
    }

    public static List<ScheduleTask> createScheduleTasks(List<Order> orders) {
        List<ScheduleTask> tasks = new ArrayList<>();
        for(int i = 0; i < orders.size() ; i++){
            ScheduleTask task = new ScheduleTask();
            task.setId(i);
            if(orders.get(i).isSameDayDelivery()){
                task.setUrgency(1);
            }
            task.setCreateTime(LocalDateTime.now());
            task.setOrder(orders.get(i));
            tasks.add(task);
        }
        return tasks;
    }

    public static List<Order> generateRandomOrders(int numberOfOrders) {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();

        for (long i = 0; i < numberOfOrders; i++) {
            Customer customer = new Customer();

            List<Item> productList = new ArrayList<>();
            List<Item> items = List.of(Item.values());

            // 5개 랜덤하게 생성
            int numberOfProducts = random.nextInt(5) + 1;
            for (int j = 0; j < numberOfProducts; j++) {
                int randomIndex = random.nextInt(items.size());
                productList.add(items.get(randomIndex));
            }

            boolean oneDayShipping = random.nextBoolean();
            orders.add(Order.of(i+1, customer, productList, oneDayShipping));
        }

        return orders;
    }

    private void printOrderList(List<Order> orders){
        System.out.println("생성된 Order 출력 : ");
        System.out.println("-------------------------------------------");
        for(int i = 0 ; i < orders.size() ; i++){
            System.out.println(orders.get(i).toString());
        }
    }
}
