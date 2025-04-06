package com.demo.services.employee;

import java.util.List;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;

public interface EmployeeService {
	
	List<TaskDao> getTasksByUserId();
	
	TaskDao updateTask(Long id, String status);
	
	TaskDao getTaskById(Long id);
	
	CommentDao createComment(Long taskId, String content);
	
	List<CommentDao> getCommentsByTaskId(Long taskId);

}
