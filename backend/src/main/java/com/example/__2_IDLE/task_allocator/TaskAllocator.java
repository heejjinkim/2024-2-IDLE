package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.robot.model.Robot;
import com.example.__2_IDLE.schedule.ScheduleService;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskAllocator {

    private final TaskAllocateAlgorithm algorithm;

    public boolean start() {
        try {
            return run();
        } catch (ArrayIndexOutOfBoundsException e) {
            log.info("scheduling queue is empty");
            return true;
        }
    }

    public void initAlgorithm(TaskWave taskWave, Map<String, Robot> allRobots) {
        algorithm.init(taskWave, allRobots);
        algorithm.step1();
    }

    private boolean run() {
        if (algorithm.isDone()) {
            return true;
        }

        boolean isDone = false;
        while (!isDone) {
            isDone = runAlgorithm();
        }
        return isDone;
    }

    private boolean runAlgorithm() {
        algorithm.step2();
        if (algorithm.isDone()) {
            return true;
        }
        algorithm.step3();
        if (algorithm.isDone()) {
            return true;
        }
        algorithm.step4();
        return algorithm.isDone();
    }
}
