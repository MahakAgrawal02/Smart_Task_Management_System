package com.demo.dao;

import java.util.Date;

import com.demo.enums.TaskStatus;
import lombok.Data;

/**
 * Data Transfer Object representing task details.
 */
@Data
public class TaskDao {

    /**
     * Unique identifier for the task.
     */
    private Long id;

    /**
     * Title or short name of the task.
     */
    private String title;

    /**
     * Detailed description of the task.
     */
    private String description;

    /**
     * Deadline or due date by which the task should be completed.
     */
    private Date dueDate;

    /**
     * Priority level of the task (e.g., High, Medium, Low).
     */
    private String priority;

    /**
     * Current status of the task (e.g., PENDING, COMPLETED).
     */
    private TaskStatus taskStatus;

    /**
     * ID of the employee assigned to this task.
     */
    private Long employeeId;

    /**
     * Name of the employee assigned to the task.
     */
    private String employeeName;
}
