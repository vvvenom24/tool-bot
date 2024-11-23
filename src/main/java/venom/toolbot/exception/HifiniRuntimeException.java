package venom.toolbot.exception;

import venom.toolbot.enums.TaskStatusEnum;

public class HifiniRuntimeException extends TaskRuntimeException {
    private final TaskStatusEnum taskStatus;

    public HifiniRuntimeException(TaskStatusEnum taskStatus) {
        super(taskStatus.getMessage());
        this.taskStatus = taskStatus;
    }
}
