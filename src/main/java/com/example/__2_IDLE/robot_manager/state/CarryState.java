package com.example.__2_IDLE.robot_manager.state;

import com.example.__2_IDLE.robot_manager.pos.Pos;
import com.example.__2_IDLE.robot_manager.robot.Robot;
import com.example.__2_IDLE.task.model.RobotTask;
import java.util.LinkedList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@Getter
public class CarryState extends RobotState {
    public CarryState() {
        stateName = "carry";
    }

    @Override
    public void startTask(Robot robot) {
        Pos startPos = robot.getPos();
        RobotTask robotTask = robot.getRobotTask();
        LinkedList<Pos> destinations = robotTask.getDestinations();
        for (Pos viaPos : destinations) {
            goTo(startPos, viaPos);
            robot.setPos(viaPos);
            startPos = viaPos;
        }
        robot.setState(RobotStateContainer.getWaitStateInstance()); // 작업 완료 후 대기 상태로 전환
        robot.resetRobotTask();
        // TODO 작업 완료 후 대기 장소로 이동
    }

    private void goTo (Pos startPos, Pos viaPos) {
        double time = Math.abs(startPos.getX() - viaPos.getX()) + Math.abs(startPos.getY() - viaPos.getY()); // 1초에 거리1만큼 이동한다고 가정
        try {
            Thread.sleep((long) (time * 1000)); // 이동 시간 동안 대기
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
