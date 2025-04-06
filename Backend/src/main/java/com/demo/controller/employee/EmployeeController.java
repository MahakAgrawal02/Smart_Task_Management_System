package com.demo.controller.employee;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.services.employee.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@CrossOrigin("*") // Allow CORS requests from any origin
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Retrieves tasks assigned to the currently logged-in employee.
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDao>> getTasksByUserId() {
        log.info("Fetching tasks for logged-in employee.");
        List<TaskDao> tasks = employeeService.getTasksByUserId();
        log.info("Found {} tasks for employee.", tasks.size());
        return ResponseEntity.ok(tasks);
    }

    /**
     * Updates the status of a task by its ID.
     *
     * @param id     Task ID
     * @param status New status to apply
     */
    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDao> updateTask(@PathVariable Long id, @PathVariable String status) {
        log.info("Updating task with ID {} to status {}", id, status);
        TaskDao updatedTaskDao = employeeService.updateTask(id, status);
        if (updatedTaskDao == null) {
            log.warn("Failed to update task with ID {}. Task not found or invalid status.", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Task updated successfully: ID {}", updatedTaskDao.getId());
        return ResponseEntity.ok(updatedTaskDao);
    }

    /**
     * Fetches a specific task by its ID.
     *
     * @param id Task ID
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDao> getTaskById(@PathVariable Long id) {
        log.info("Fetching task details for ID {}", id);
        TaskDao task = employeeService.getTaskById(id);
        if (task == null) {
            log.warn("Task not found for ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Task found: ID {}", task.getId());
        return ResponseEntity.ok(task);
    }

    /**
     * Adds a comment to a specific task.
     *
     * @param taskId  ID of the task to comment on
     * @param content Comment content
     */
    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDao> createComment(@PathVariable Long taskId, @RequestParam String content) {
        log.info("Creating comment for task ID {} with content: {}", taskId, content);
        CommentDao createdCommentDao = employeeService.createComment(taskId, content);
        if (createdCommentDao == null) {
            log.warn("Failed to create comment for task ID {}", taskId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Comment created successfully for task ID {}", taskId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDao);
    }

    /**
     * Retrieves all comments associated with a given task ID.
     *
     * @param taskId ID of the task
     */
    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDao>> getCommentsByTaskId(@PathVariable Long taskId) {
        log.info("Fetching comments for task ID {}", taskId);
        List<CommentDao> comments = employeeService.getCommentsByTaskId(taskId);
        log.info("Found {} comments for task ID {}", comments.size(), taskId);
        return ResponseEntity.ok(comments);
    }
}
