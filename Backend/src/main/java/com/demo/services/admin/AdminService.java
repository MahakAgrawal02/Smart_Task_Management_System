package com.demo.services.admin;

import java.util.List;

import com.demo.dao.CommentDao;
import com.demo.dao.TaskDao;
import com.demo.dao.UserDto;

public interface AdminService {
	
	List<UserDto> getUsers();
	
	TaskDao createTask(TaskDao taskDao);
	
	List<TaskDao> getAllTasks();
	
	void deleteTask(Long id);
	
	TaskDao getTaskById(Long id);
	
	TaskDao updateTask(Long id, TaskDao taskDao);
	
	List<TaskDao> searchTaskByTitle(String title);
	
	CommentDao createComment(Long taskId, String content);
	
	List<CommentDao> getCommentsByTaskId(Long taskId);

}
