package com.demo.controller.admin;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.services.admin.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AdminService adminService; // Mocked service dependency

    @InjectMocks
    private AdminController adminController; // Controller under test

    private TaskDao task1, task2;
    private CommentDao comment;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

        // Dummy task data
        task1 = new TaskDao();
        task1.setId(1L);
        task1.setTitle("Task One");
        task1.setDueDate(java.sql.Date.valueOf(LocalDate.now().plusDays(3)));

        task2 = new TaskDao();
        task2.setId(2L);
        task2.setTitle("Task Two");
        task2.setDueDate(java.sql.Date.valueOf(LocalDate.now().plusDays(5)));

        // Dummy comment
        comment = new CommentDao();
        comment.setId(1L);
        comment.setContent("This is a test comment");
    }

    // ✅ Test creating a task
    @Test
    void testCreateTask() throws Exception {
        when(adminService.createTask(any(TaskDao.class))).thenReturn(task1);

        mockMvc.perform(post("/api/admin/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task One"));

        verify(adminService, times(1)).createTask(any(TaskDao.class));
    }

    // ✅ Test fetching all tasks
    @Test
    void testGetAllTasks() throws Exception {
        List<TaskDao> taskList = Arrays.asList(task1, task2);
        when(adminService.getAllTasks()).thenReturn(taskList);

        mockMvc.perform(get("/api/admin/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(adminService, times(1)).getAllTasks();
    }

    // ✅ Test deleting a task by ID
    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(adminService).deleteTask(1L);

        mockMvc.perform(delete("/api/admin/task/1"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).deleteTask(1L);
    }

    // ✅ Test updating a task
    @Test
    void testUpdateTask() throws Exception {
        when(adminService.updateTask(eq(1L), any(TaskDao.class))).thenReturn(task1);

        mockMvc.perform(put("/api/admin/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task One"));

        verify(adminService, times(1)).updateTask(eq(1L), any(TaskDao.class));
    }

    // ✅ Test searching tasks by title
    @Test
    void testSearchTaskByTitle() throws Exception {
        List<TaskDao> results = List.of(task1);
        when(adminService.searchTaskByTitle("Task")).thenReturn(results);

        mockMvc.perform(get("/api/admin/tasks/search/Task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(adminService, times(1)).searchTaskByTitle("Task");
    }

    // ✅ Test fetching task by ID
    @Test
    void testGetTaskById() throws Exception {
        when(adminService.getTaskById(1L)).thenReturn(task1);

        mockMvc.perform(get("/api/admin/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task One"));

        verify(adminService, times(1)).getTaskById(1L);
    }

    // ✅ Test creating a comment for a task
    @Test
    void testCreateComment() throws Exception {
        when(adminService.createComment(1L, "Nice Work")).thenReturn(comment);

        mockMvc.perform(post("/api/admin/task/comment/1")
                        .param("content", "Nice Work"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("This is a test comment"));

        verify(adminService, times(1)).createComment(1L, "Nice Work");
    }

    // ✅ Test fetching comments by task ID
    @Test
    void testGetCommentsByTaskId() throws Exception {
        when(adminService.getCommentsByTaskId(1L)).thenReturn(List.of(comment));

        mockMvc.perform(get("/api/admin/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(adminService, times(1)).getCommentsByTaskId(1L);
    }
}
