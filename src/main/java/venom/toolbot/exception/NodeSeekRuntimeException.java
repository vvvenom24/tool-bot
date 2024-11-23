package venom.toolbot.exception;

import venom.toolbot.enums.TaskStatusEnum;

public class NodeSeekRuntimeException extends TaskRuntimeException {
    private final TaskStatusEnum taskStatus;

    public NodeSeekRuntimeException(TaskStatusEnum taskStatus) {
        super(taskStatus.getMessage());
        this.taskStatus = taskStatus;
    }
}
