package com.demo.controller.admin;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        log.info("Admin requested user list.");
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDao> createTask(@RequestBody TaskDao taskDao) {
        log.info("Admin creating new task: {}", taskDao.getTitle());
        TaskDao createTaskDao = adminService.createTask(taskDao);
        if (createTaskDao == null) {
            log.warn("Failed to create task: {}", taskDao);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Task created successfully with ID: {}", createTaskDao.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createTaskDao);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks() {
        log.info("Admin fetching all tasks.");
        return ResponseEntity.ok(adminService.getAllTasks());
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Admin deleting task with ID: {}", id);
        adminService.deleteTask(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<TaskDao> updateTask(@PathVariable Long id, @RequestBody TaskDao taskDao) {
        log.info("Admin updating task with ID: {}", id);
        TaskDao updatedTask = adminService.updateTask(id, taskDao);
        if (updatedTask == null) {
            log.warn("Task not found for update. ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Task updated successfully. ID: {}", updatedTask.getId());
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks/search/{title}")
    public ResponseEntity<List<TaskDao>> searchTask(@PathVariable String title) {
        log.info("Admin searching tasks with title containing: {}", title);
        return ResponseEntity.ok(adminService.searchTaskByTitle(title));
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDao> getTaskById(@PathVariable Long id) {
        log.info("Admin fetching task with ID: {}", id);
        return ResponseEntity.ok(adminService.getTaskById(id));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDao> createComment(@PathVariable Long taskId, @RequestParam String content) {
        log.info("Admin adding comment to Task ID: {}", taskId);
        CommentDao createdCommentDao = adminService.createComment(taskId, content);
        if (createdCommentDao == null) {
            log.warn("Failed to create comment on task ID: {}", taskId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Comment created on Task ID: {}", taskId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDao);
    }

    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDao>> getCommentsByTaskId(@PathVariable Long taskId) {
        log.info("Admin fetching comments for Task ID: {}", taskId);
        return ResponseEntity.ok(adminService.getCommentsByTaskId(taskId));
    }
}
