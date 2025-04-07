package com.demo.controller.employee;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.services.employee.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private EmployeeService employeeService; // Mocked service layer

    @InjectMocks
    private EmployeeController employeeController; // Controller under test

    private TaskDao dummyTask;
    private CommentDao dummyComment;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        // Dummy task initialization
        dummyTask = new TaskDao();
        dummyTask.setId(1L);
        dummyTask.setTitle("Test Task");

        // Dummy comment initialization
        dummyComment = new CommentDao();
        dummyComment.setId(1L);
        dummyComment.setContent("Test Comment");
    }

    // ✅ Test fetching tasks assigned to the logged-in employee
    @Test
    void testGetTasksByUserId() throws Exception {
        when(employeeService.getTasksByUserId()).thenReturn(Arrays.asList(dummyTask));

        mockMvc.perform(get("/api/employee/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(employeeService).getTasksByUserId();
    }

    // ✅ Test fetching a specific task by ID
    @Test
    void testGetTaskById() throws Exception {
        when(employeeService.getTaskById(1L)).thenReturn(dummyTask);

        mockMvc.perform(get("/api/employee/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(employeeService).getTaskById(1L);
    }

    // ✅ Test successfully updating task status
    @Test
    void testUpdateTask_Success() throws Exception {
        when(employeeService.updateTask(1L, "COMPLETED")).thenReturn(dummyTask);

        mockMvc.perform(get("/api/employee/task/1/COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(employeeService).updateTask(1L, "COMPLETED");
    }

    // ❌ Test updating task with invalid status
    @Test
    void testUpdateTask_Failure() throws Exception {
        when(employeeService.updateTask(1L, "INVALID")).thenReturn(null);

        mockMvc.perform(get("/api/employee/task/1/INVALID"))
                .andExpect(status().isBadRequest());

        verify(employeeService).updateTask(1L, "INVALID");
    }

    // ✅ Test adding a comment to a task successfully
    @Test
    void testCreateComment_Success() throws Exception {
        when(employeeService.createComment(1L, "Test comment")).thenReturn(dummyComment);

        mockMvc.perform(post("/api/employee/task/comment/1")
                        .param("content", "Test comment"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(employeeService).createComment(1L, "Test comment");
    }

    // ❌ Test failure to add comment due to bad content
    @Test
    void testCreateComment_Failure() throws Exception {
        when(employeeService.createComment(1L, "Invalid")).thenReturn(null);

        mockMvc.perform(post("/api/employee/task/comment/1")
                        .param("content", "Invalid"))
                .andExpect(status().isBadRequest());

        verify(employeeService).createComment(1L, "Invalid");
    }

    // ✅ Test fetching comments for a specific task
    @Test
    void testGetCommentsByTaskId() throws Exception {
        List<CommentDao> comments = Arrays.asList(dummyComment);
        when(employeeService.getCommentsByTaskId(1L)).thenReturn(comments);

        mockMvc.perform(get("/api/employee/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test Comment"));

        verify(employeeService).getCommentsByTaskId(1L);
    }
}
