package com.example.__2_IDLE.test_allo_sim.v2.manager;

import com.example.__2_IDLE.test_allo_sim.v2.model.Pose;
import com.example.__2_IDLE.test_allo_sim.v2.model.Robot;
import com.example.__2_IDLE.test_allo_sim.v2.model.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RobotManager {

    private List<Robot> robotList;

    public RobotManager(int robotCount, int robotInterval, int y, ItemManager itemManager) {
        this.robotList = new ArrayList<Robot>();
        makeRobots(robotCount, robotInterval, y, itemManager);
    }

    public List<Task> getLastTasks () {
        List<Task> lastTasks = new ArrayList<>();
        for (Robot robot : robotList) {
            lastTasks.add(robot.getLastTask());
        }
        return lastTasks;
    }

    public void printAllocatedTasks() {
        for (Robot robot : robotList) {
            robot.printAllocatedTasks();
        }
    }

    private void makeRobots(int robotCount, int robotInterval, int y, ItemManager itemManager) {
        for (int i = 0; i < robotCount; i++) {
            Pose initialPose = new Pose(i * robotInterval, y);
            Robot robot = new Robot("robot" + i, initialPose, initialPose, new ArrayList<>(), itemManager);
            robotList.add(robot);
        }
    }
}
