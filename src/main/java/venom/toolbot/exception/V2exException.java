package venom.toolbot.exception;

import venom.toolbot.enums.TaskStatusEnum;

public class V2exException extends TaskRuntimeException {

    private final TaskStatusEnum taskStatus;

    public V2exException(TaskStatusEnum taskStatus) {
        super(taskStatus.getMessage());
        this.taskStatus = taskStatus;
    }
}
