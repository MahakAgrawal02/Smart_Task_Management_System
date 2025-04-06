package com.demo.services.employee;

import java.util.List;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;

/**
 * Service interface for employee-specific operations
 * such as viewing and updating assigned tasks, and commenting on tasks.
 */
public interface EmployeeService {
    
    /**
     * Retrieves all tasks assigned to the currently logged-in employee.
     *
     * @return List of TaskDao objects assigned to the user
     */
    List<TaskDao> getTasksByUserId();

    /**
     * Updates the status of a specific task.
     *
     * @param id     the task ID
     * @param status the new status of the task (e.g., "COMPLETED", "INPROGRESS")
     * @return the updated TaskDao object
     */
    TaskDao updateTask(Long id, String status);

    /**
     * Retrieves a task by its ID.
     *
     * @param id the task ID
     * @return the corresponding TaskDao object
     */
    TaskDao getTaskById(Long id);

    /**
     * Creates a comment on a specific task by the current employee.
     *
     * @param taskId  the task ID
     * @param content the comment content
     * @return the created CommentDao object
     */
    CommentDao createComment(Long taskId, String content);

    /**
     * Retrieves all comments associated with a specific task.
     *
     * @param taskId the task ID
     * @return List of CommentDao objects for the task
     */
    List<CommentDao> getCommentsByTaskId(Long taskId);

}
