package com.demo.services.admin;

import java.util.List;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.dao.UserDto;

/**
 * Service interface for administrative operations.
 * Handles user management, task assignment, and comment tracking.
 */
public interface AdminService {

    /**
     * Retrieves a list of all users in the system.
     *
     * @return list of UserDto objects representing users
     */
    List<UserDto> getUsers();

    /**
     * Creates a new task and assigns it to an employee.
     *
     * @param taskDao the task data to be created
     * @return the created TaskDao object
     */
    TaskDao createTask(TaskDao taskDao);

    /**
     * Retrieves a list of all tasks in the system.
     *
     * @return list of TaskDao objects
     */
    List<TaskDao> getAllTasks();

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     */
    void deleteTask(Long id);

    /**
     * Fetches a task's details using its ID.
     *
     * @param id the ID of the task
     * @return TaskDao object with task details, or null if not found
     */
    TaskDao getTaskById(Long id);

    /**
     * Updates the task with the specified ID.
     *
     * @param id the ID of the task to update
     * @param taskDao the updated task data
     * @return updated TaskDao object
     */
    TaskDao updateTask(Long id, TaskDao taskDao);

    /**
     * Searches for tasks with a title containing the given keyword.
     *
     * @param title the title keyword to search for
     * @return list of TaskDao objects that match the search criteria
     */
    List<TaskDao> searchTaskByTitle(String title);

    /**
     * Adds a comment to the specified task.
     *
     * @param taskId the ID of the task
     * @param content the comment content
     * @return the created CommentDao object
     */
    CommentDao createComment(Long taskId, String content);

    /**
     * Retrieves all comments associated with the given task ID.
     *
     * @param taskId the ID of the task
     * @return list of CommentDao objects
     */
    List<CommentDao> getCommentsByTaskId(Long taskId);

}
