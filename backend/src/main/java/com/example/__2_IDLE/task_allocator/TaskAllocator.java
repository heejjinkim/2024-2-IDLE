package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.schedule.ScheduleService;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskAllocator {

    private final TaskAllocateAlgorithm algorithm;
    private final ScheduleService scheduleService;

    public boolean start() {
        try {
            return run();
        } catch (ArrayIndexOutOfBoundsException e) {
            log.info("scheduling queue is empty");
            return true;
        }
    }

    private boolean run() {
        initAlgorithm();

        if (algorithm.isDone()) {
            return true;
        }

        boolean isDone = false;
        while (!isDone) {
            isDone = runAlgorithm();
        }
        return isDone;
    }

    private void initAlgorithm() {
        algorithm.init(fetchTaskWave());
        algorithm.step1();
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

    private TaskWave fetchTaskWave() {
        return scheduleService.getTaskWave();
    }
}
