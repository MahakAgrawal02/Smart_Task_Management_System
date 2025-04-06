package com.demo.entities;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.demo.dao.TaskDao;
import com.demo.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Entity class representing a task assigned to a user (employee).
 */
@Entity
@Data
public class Task {

    /**
     * Primary key - unique identifier for the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the task.
     */
    private String title;

    /**
     * Detailed description of the task.
     */
    private String description;

    /**
     * Due date for the task.
     */
    private Date dueDate;

    /**
     * Priority level of the task (e.g., High, Medium, Low).
     */
    private String priority;

    /**
     * Current status of the task (e.g., PENDING, IN_PROGRESS, COMPLETED).
     */
    private TaskStatus taskStatus;

    /**
     * The user (employee) to whom the task is assigned.
     * On user deletion, related tasks are also deleted (CASCADE).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    /**
     * Converts the entity to a data transfer object (DAO) for use in responses.
     *
     * @return TaskDao containing essential, non-sensitive task details.
     */
    public TaskDao getTaskDao() {
        TaskDao taskDao = new TaskDao();
        taskDao.setId(id);
        taskDao.setTitle(title);
        taskDao.setDescription(description);
        taskDao.setEmployeeName(user.getName());
        taskDao.setEmployeeId(user.getId());
        taskDao.setTaskStatus(taskStatus);
        taskDao.setDueDate(dueDate);
        taskDao.setPriority(priority);
        return taskDao;
    }
}
