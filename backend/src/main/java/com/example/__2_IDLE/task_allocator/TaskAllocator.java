package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TaskAllocator {

    private final TaskAllocateAlgorithm algorithm;
    private final ScheduleModule scheduleModule;

    public void start() {
        try {
            run();
        } catch (ArrayIndexOutOfBoundsException e) {
            log.info("scheduling queue is empty");
        }
    }

    private void run() {
        initAlgorithm();

        if (algorithm.isDone()) {
            return;
        }

        boolean isDone = false;
        while (!isDone) {
            isDone = runAlgorithm();
        }
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
        return scheduleModule.getTaskWave();
    }
}
