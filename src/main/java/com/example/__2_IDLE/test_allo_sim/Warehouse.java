package com.example.__2_IDLE.test_allo_sim;

import com.example.__2_IDLE.test_allo_sim.manager.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class Warehouse {
    private double width;
    private double height;
    private int robotCount;
    private int itemCount;
    private int stationCount;
    private ItemManager itemManager;
    private RobotManager robotManager;
    private StationManager stationManager;
    private TaskManager taskManager;
    private Mediator mediator;

    private static final double STATION_Y = 0;

    /**
     *
     * @param width
     * @param height
     * @param robotCount
     * @param itemCount
     * @param stationCount
     */
    public Warehouse(int width, int height, int robotCount, int itemCount, int stationCount) {
        if (width < robotCount || width < itemCount || width < stationCount) {
            throw new IllegalArgumentException("물류센터의 가로 길이 설정이 잘못되었습니다.");
        }
        if (height < 2) {
            throw new IllegalArgumentException("물류센터의 세로는 2보다 크거나 같아야 합니다.");
        }
        this.width = width;
        this.height = height;
        this.robotCount = robotCount;
        this.itemCount = itemCount;
        this.stationCount = stationCount;
        this.itemManager = new ItemManager();
        this.robotManager = new RobotManager();
        this.stationManager = new StationManager();
        this.taskManager = new TaskManager();
        this.mediator = new Mediator(itemManager, robotManager, stationManager, taskManager);
    }

    public void makeWareHouse() {
        setRobot();
        setItem();
        setStation();
    }

    private void setRobot() {
        double robotInterval = width / robotCount;
        double x = 0;
        for (int i = 0; i < robotCount; i++) {
            robotManager.createRobot(new Pose(x, height), "robot" + i, mediator);
            x += robotInterval;
        }
        log.info("complete setRobot");
    }

    private void setItem() {
        double itemInterval = width / itemCount; // 10가지 item 가정
        double x = 0;
        for (int i = 0; i < itemCount; i++) {
            itemManager.createItem(new Pose(x, height / 2), "item" + i);
            x += itemInterval;
        }
        log.info("complete setItem");
    }

    private void setStation() {
        double stationInterval = width / stationCount;
        double x = 0;
        for (int i = 0; i < stationCount; i++) {
            stationManager.createStation(new Pose(x, 0), "station" + i);
            x += stationInterval;
        }
        log.info("complete setStation");
    }
}
