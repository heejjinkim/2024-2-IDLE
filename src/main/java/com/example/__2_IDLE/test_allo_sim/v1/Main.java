package com.example.__2_IDLE.test_allo_sim.v1;

import com.example.__2_IDLE.test_allo_sim.v1.manager.Mediator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 시뮬레이션 시간의 0.001초를 실제 시간의 1초로 가정
 * 거리1 당 이동시간 1초라고 가정
 * 피킹스테이션에서 작업을 처리하는 시간은 동일하다고 가정
 */
@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Warehouse warehouse = new Warehouse(10, 10, 5, 5, 5);
        warehouse.makeWareHouse();
        Mediator mediator = warehouse.getMediator();
        LinkedList<Task> taskQueue = mediator.generateTask(warehouse.getItemCount(), 100);
        log.info("generate random tasks");

//        printComponents(warehouse);


        AtomicInteger cnt = new AtomicInteger();
        for (Task task : taskQueue) {
            Thread taskThread = new Thread(() -> {
                try {
                    int count = mediator.allocateTask(task);
                    cnt.addAndGet(count);// Task 할당
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            // 스레드 시작
            taskThread.start();
            Thread.sleep(1);
        }

        System.out.println(cnt.get());

        warehouse.getRobotManager().printTotalTaskCount();
    }

    private static void printComponents(Warehouse warehouse) {
        ArrayList<Item> itemList = warehouse.getItemManager().getItemList();
        ArrayList<Robot> robotList = warehouse.getRobotManager().getRobotList();
        ArrayList<Station> stationList = warehouse.getStationManager().getStationList();

        System.out.println("Item");
        for (Item item : itemList) {
            System.out.println(item.getPose().toString());
        }
        System.out.println("Robot");
        for (Robot robot : robotList) {
            System.out.println(robot.getPose().toString());
        }
        System.out.println("Station");
        for (Station station : stationList) {
            System.out.println(station.getPose().toString());
        }
    }
}