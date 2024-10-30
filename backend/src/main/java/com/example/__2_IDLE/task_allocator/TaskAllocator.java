package com.example.__2_IDLE.task_allocator;

import com.example.__2_IDLE.schedule_module.ScheduleModule;
import com.example.__2_IDLE.task_allocator.model.TaskWave;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TaskAllocator {

    private final TaskAllocateAlgorithm algorithm;
    private final ScheduleModule scheduleModule;

    private TaskWave fetchTaskWave() {
        return scheduleModule.getTaskWave();
    }

    public void start() {
        while (true) {
            try {
                algorithm.setTaskWave(fetchTaskWave());
                run();
            } catch (ArrayIndexOutOfBoundsException e) {
                log.info("scheduling queue is empty");
                return;
            }
        }
    }

    private void run() {
        initAlgorithm();

        if (algorithm.isAllTasksAllocated()) {
            return;
        }

        boolean flag = false;
        while (!flag) {
            flag = runAlgorithm();
        }
    }

    private void initAlgorithm() {
        algorithm.setUnallocatedList();
        allocateNearestTask(); // step1
    }

    private boolean runAlgorithm() {
        allocateStronglyCorrelatedTask(); // step2
        if (algorithm.isAllTasksAllocated()) {
            return true;
        }
        allocateWeaklyCorrelatedTask(); // step3
        if (algorithm.isAllTasksAllocated()) {
            return true;
        }
        algorithm.step4();
        if (algorithm.isAllTasksAllocated()) {
            return true;
        }
        return false;
    }

    private void allocateWeaklyCorrelatedTask() {
        algorithm.step3();
    }

    private void allocateStronglyCorrelatedTask() {
        algorithm.step2();
    }

    private void allocateNearestTask() {
        algorithm.step1();
    }
}
