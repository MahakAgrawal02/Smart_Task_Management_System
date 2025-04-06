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

@Entity
@Data
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private Date dueDate;
	private String priority;
	private TaskStatus taskStatus;
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name="user_id", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;
	
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
