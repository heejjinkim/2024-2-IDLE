package com.example.__2_IDLE.robot_manager.state;

import com.example.__2_IDLE.global.model.Pose;
import com.example.__2_IDLE.robot_manager.robot.Robot;
import com.example.__2_IDLE.task.model.RobotTask;
import java.util.LinkedList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CarryState extends RobotState {
    public CarryState() {
        stateName = "carry";
    }

    @Override
    public void startTask(Robot robot) {
        Pose startPose = robot.getPose();
        RobotTask robotTask = robot.getRobotTask();
        LinkedList<Pose> destinations = robotTask.getDestinations();
        for (Pose viaPose : destinations) {
            goTo(startPose, viaPose);
            robot.setPose(viaPose);
            startPose = viaPose;
        }
        robot.setState(RobotStateContainer.getWaitStateInstance()); // 작업 완료 후 대기 상태로 전환
        robot.resetRobotTask();
        // TODO 작업 완료 후 대기 장소로 이동
    }

    private void goTo (Pose startPose, Pose viaPose) {
        double time = Math.abs(startPose.getX() - viaPose.getX()) + Math.abs(startPose.getY() - viaPose.getY()); // 1초에 거리1만큼 이동한다고 가정
        try {
            Thread.sleep((long) (time * 1000)); // 이동 시간 동안 대기
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
