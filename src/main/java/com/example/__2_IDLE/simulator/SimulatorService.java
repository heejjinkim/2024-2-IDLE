package com.example.__2_IDLE.simulator;


import com.example.__2_IDLE.global.model.Customer;
import com.example.__2_IDLE.global.model.Order;
import com.example.__2_IDLE.global.model.enums.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.swing.text.html.parser.DTDConstants.NAMES;

public class SimulatorService {
    private void printOrderList(List<Order> orders){
        System.out.println("생성된 Order 출력 : ");
        for(int i = 0 ; i < orders.size() ; i++){
            System.out.println(orders.get(i).toString());
        }
    }

    public void run(){
        List<Order> orders = generateRandomOrders(10);
        printOrderList(orders);
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
}
