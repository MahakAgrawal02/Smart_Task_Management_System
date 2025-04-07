package com.demo.services.employee;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.entities.Comment;
import com.demo.entities.Task;
import com.demo.entities.User;
import com.demo.enums.TaskStatus;
import com.demo.repositories.CommentRepository;
import com.demo.repositories.TaskRepository;
import com.demo.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the EmployeeServiceImpl class using JUnit and Mockito.
 */
class EmployeeServiceImplTest {

    // Mock dependencies
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CommentRepository commentRepository;

    // Class under test
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    // Test user instance
    private User user;

    /**
     * Setup common data before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
    }

    /**
     * Test: Get all tasks assigned to a user.
     */
    @Test
    void testGetTasksByUserId_ReturnsList() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDueDate(new Date());
        task1.setUser(user);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDueDate(new Date(System.currentTimeMillis() + 100000));
        task2.setUser(user);

        when(jwtUtil.getLoggedInUser()).thenReturn(user);
        when(taskRepository.findAllByUserId(user.getId())).thenReturn(Arrays.asList(task1, task2));

        List<TaskDao> result = employeeService.getTasksByUserId();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository).findAllByUserId(user.getId());
    }

    /**
     * Test: Update task status with a valid task ID.
     */
    @Test
    void testUpdateTask_ValidId_UpdatesStatus() {
        Task task = new Task();
        task.setId(1L);
        task.setTaskStatus(TaskStatus.PENDING);
        task.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDao updated = employeeService.updateTask(1L, "COMPLETED");

        assertNotNull(updated);
        assertEquals(TaskStatus.COMPLETED, task.getTaskStatus());
        verify(taskRepository).save(task);
    }

    /**
     * Test: Update task status with an invalid task ID throws exception.
     */
    @Test
    void testUpdateTask_InvalidId_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> employeeService.updateTask(1L, "PENDING"));
    }

    /**
     * Test: Get task by ID when task exists.
     */
    @Test
    void testGetTaskById_Found_ReturnsTask() {
        Task task = new Task();
        task.setId(1L);
        task.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDao result = employeeService.getTaskById(1L);
        assertNotNull(result);
    }

    /**
     * Test: Get task by ID when task does not exist returns null.
     */
    @Test
    void testGetTaskById_NotFound_ReturnsNull() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        TaskDao result = employeeService.getTaskById(1L);
        assertNull(result);
    }

    /**
     * Test: Create a comment on a valid task.
     */
    @Test
    void testCreateComment_ValidTask_CreatesComment() {
        Task task = new Task();
        task.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setTask(task);
        comment.setUser(user);
        comment.setCreatedAt(new Date());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(jwtUtil.getLoggedInUser()).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDao result = employeeService.createComment(1L, "Test comment");

        assertNotNull(result);
        assertEquals("Test comment", result.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    /**
     * Test: Creating a comment on a non-existent task throws exception.
     */
    @Test
    void testCreateComment_InvalidTask_ThrowsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        when(jwtUtil.getLoggedInUser()).thenReturn(user);

        assertThrows(EntityNotFoundException.class, () -> employeeService.createComment(1L, "Test"));
    }

    /**
     * Test: Get all comments for a given task ID.
     */
    @Test
    void testGetCommentsByTaskId_ReturnsList() {
        Task task = new Task();
        task.setId(1L);

        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Comment 1");
        comment1.setTask(task);
        comment1.setUser(user);
        comment1.setCreatedAt(new Date());

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Comment 2");
        comment2.setTask(task);
        comment2.setUser(user);
        comment2.setCreatedAt(new Date());

        when(commentRepository.findAllByTaskId(1L)).thenReturn(Arrays.asList(comment1, comment2));

        List<CommentDao> result = employeeService.getCommentsByTaskId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Comment 1", result.get(0).getContent());
    }
}
