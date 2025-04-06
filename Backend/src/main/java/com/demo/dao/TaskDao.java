package com.demo.dao;

import java.util.Date;

import com.demo.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskDao {
	
	private Long id;
	private String title;
	private String description;
	private Date dueDate;
	private String priority;
	private TaskStatus taskStatus;
	private Long employeeId;
	private String employeeName;

}
