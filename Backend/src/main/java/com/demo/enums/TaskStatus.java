package com.demo.enums;

/**
 * Enum representing the possible statuses of a task.
 */
public enum TaskStatus {

    /**
     * Task has been created but not yet started.
     */
    PENDING,

    /**
     * Task is currently in progress.
     */
    INPROGRESS,

    /**
     * Task has been completed successfully.
     */
    COMPLETED,

    /**
     * Task has been deferred for future consideration or action.
     */
    DEFERRED,

    /**
     * Task has been cancelled and will not be completed.
     */
    CANCELLED

}
