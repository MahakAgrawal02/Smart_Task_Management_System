package com.demo.services.admin;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.dao.UserDto;
import com.demo.entities.Comment;
import com.demo.entities.Task;
import com.demo.entities.User;
import com.demo.enums.TaskStatus;
import com.demo.enums.UserRole;
import com.demo.repositories.CommentRepository;
import com.demo.repositories.TaskRepository;
import com.demo.repositories.UserRepository;
import com.demo.utils.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AdminServiceImpl using JUnit5 and Mockito.
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    // Mocked dependencies
    @Mock private UserRepository userRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private JwtUtil jwtUtil;

    // Inject mocks into AdminServiceImpl
    @InjectMocks
    private AdminServiceImpl adminService;

    // Test objects
    private User employee;
    private Task task;

    /**
     * Initialize reusable test data before each test case.
     */
    @BeforeEach
    void setUp() {
        // Set up test user (EMPLOYEE role)
        employee = new User();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john@example.com");
        employee.setUserRole(UserRole.EMPLOYEE);

        // Set up test task assigned to the employee
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(new Date());
        task.setTaskStatus(TaskStatus.INPROGRESS);
        task.setUser(employee);
    }

    /**
     * Test: Fetch all users and return only EMPLOYEES as DTOs.
     */
    @Test
    void testGetUsers_ReturnsListOfEmployees() {
        User admin = new User();
        admin.setUserRole(UserRole.ADMIN);

        when(userRepository.findAll()).thenReturn(List.of(employee, admin));
        List<UserDto> result = adminService.getUsers();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    /**
     * Test: Successfully create a new task.
     */
    @Test
    void testCreateTask_Success() {
        TaskDao taskDao = task.getTaskDao();

        when(userRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(1L);
            return savedTask;
        });

        TaskDao result = adminService.createTask(taskDao);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
    }

    /**
     * Test: Creating a task should return null if user not found.
     */
    @Test
    void testCreateTask_UserNotFound() {
        TaskDao taskDao = task.getTaskDao();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskDao result = adminService.createTask(taskDao);

        assertNull(result);
    }

    /**
     * Test: Retrieve all tasks sorted by due date descending.
     */
    @Test
    void testGetAllTasks_ReturnsSortedList() {
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Another Task");
        task2.setDueDate(new Date(System.currentTimeMillis() + 86400000)); // +1 day
        task2.setUser(employee); // prevent NullPointerException

        task.setUser(employee);

        when(taskRepository.findAll()).thenReturn(List.of(task2, task));

        List<TaskDao> tasks = adminService.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("Another Task", tasks.get(0).getTitle()); // Sorted by dueDate DESC
    }

    /**
     * Test: Delete task by ID should invoke repository delete.
     */
    @Test
    void testDeleteTask_DeletesSuccessfully() {
        doNothing().when(taskRepository).deleteById(1L);

        adminService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    /**
     * Test: Fetch task by ID and return as TaskDao.
     */
    @Test
    void testGetTaskById_ReturnsTaskDao() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDao result = adminService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
    }

    /**
     * Test: Successfully update an existing task.
     */
    @Test
    void testUpdateTask_Success() {
        TaskDao updatedDao = task.getTaskDao();
        updatedDao.setTitle("Updated Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskDao result = adminService.updateTask(1L, updatedDao);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    /**
     * Test: Search task by title returns matching list.
     */
    @Test
    void testSearchTaskByTitle_ReturnsList() {
        when(taskRepository.findAllByTitleContaining("Task")).thenReturn(List.of(task));

        List<TaskDao> results = adminService.searchTaskByTitle("Task");

        assertEquals(1, results.size());
        assertEquals("Test Task", results.get(0).getTitle());
    }

    /**
     * Test: Successfully create a new comment on task.
     */
    @Test
    void testCreateComment_Success() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Great job!");
        comment.setTask(task);
        comment.setUser(employee);
        comment.setCreatedAt(new Date());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(jwtUtil.getLoggedInUser()).thenReturn(employee);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDao result = adminService.createComment(task.getId(), "Great job!");

        assertNotNull(result);
        assertEquals("Great job!", result.getContent());
    }

    /**
     * Test: Creating a comment should fail if task not found.
     */
    @Test
    void testCreateComment_UserOrTaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());
        when(jwtUtil.getLoggedInUser()).thenReturn(employee);

        assertThrows(Exception.class, () -> adminService.createComment(task.getId(), "Missing task"));
    }

    /**
     * Test: Get all comments by task ID.
     */
    @Test
    void testGetCommentsByTaskId_ReturnsList() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Nice!");
        comment.setTask(task);
        comment.setUser(employee);
        comment.setCreatedAt(new Date());

        when(commentRepository.findAllByTaskId(task.getId())).thenReturn(List.of(comment));

        List<CommentDao> results = adminService.getCommentsByTaskId(task.getId());

        assertEquals(1, results.size());
        assertEquals("Nice!", results.get(0).getContent());
    }
}
