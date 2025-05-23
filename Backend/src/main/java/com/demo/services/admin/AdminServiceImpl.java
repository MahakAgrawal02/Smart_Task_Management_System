package com.demo.services.admin;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final UserRepository userRepository;
	private final TaskRepository taskRepository;
	private final JwtUtil jwtUtil;
	private final CommentRepository commentRepository;

	/**
	 * Retrieves all users with EMPLOYEE role.
	 */
	@Override
	public List<UserDto> getUsers() {
	    return userRepository.findAll()
	        .stream() 
	        .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
	        .map(User::getUserDto)
	        .collect(Collectors.toList());
	}

	/**
	 * Creates a new task and assigns it to an employee.
	 */
	@Override
	public TaskDao createTask(TaskDao taskDao) {
		Optional<User> optionalUser = userRepository.findById(taskDao.getEmployeeId());
		if (optionalUser.isPresent()) {
		    Task task = new Task();		    
		    task.setTitle(taskDao.getTitle());
		    task.setDescription(taskDao.getDescription());
		    task.setPriority(taskDao.getPriority());
		    task.setDueDate(taskDao.getDueDate());
		    task.setTaskStatus(TaskStatus.INPROGRESS); // Default status
		    task.setUser(optionalUser.get());
		    return taskRepository.save(task).getTaskDao();
		}
		return null;
	}

	/**
	 * Returns a list of all tasks, sorted by due date (descending).
	 */
	@Override
	public List<TaskDao> getAllTasks() {
		return taskRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(Task::getDueDate).reversed())
				.map(Task::getTaskDao)
				.collect(Collectors.toList());
	}

	/**
	 * Deletes a task by its ID.
	 */
	@Override
	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
	}

	/**
	 * Retrieves a task by its ID.
	 */
	@Override
	public TaskDao getTaskById(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		return optionalTask.map(Task::getTaskDao).orElse(null);
	}

	/**
	 * Updates the specified task.
	 */
	@Override
	public TaskDao updateTask(Long id, TaskDao taskDao) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		Optional<User> optionalUser = userRepository.findById(taskDao.getEmployeeId());

		if (optionalTask.isPresent() && optionalUser.isPresent()) {
		    Task existingTask = optionalTask.get();
		    existingTask.setTitle(taskDao.getTitle());
		    existingTask.setDescription(taskDao.getDescription());
		    existingTask.setDueDate(taskDao.getDueDate());
		    existingTask.setPriority(taskDao.getPriority());
		    existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDao.getTaskStatus())));
		    existingTask.setUser(optionalUser.get());
		    return taskRepository.save(existingTask).getTaskDao();
		}

		return null;
	}

	/**
	 * Maps string representation of task status to TaskStatus enum.
	 */
	private TaskStatus mapStringToTaskStatus(String status) {
	    return switch (status) {
	        case "PENDING"     -> TaskStatus.PENDING;
	        case "INPROGRESS"  -> TaskStatus.INPROGRESS;
	        case "COMPLETED"   -> TaskStatus.COMPLETED;
	        case "DEFERRED"    -> TaskStatus.DEFERRED;
	        default            -> TaskStatus.CANCELLED;
	    };
	}

	/**
	 * Searches tasks by title, sorted by due date (descending).
	 */
	@Override
	public List<TaskDao> searchTaskByTitle(String title) {
		return taskRepository.findAllByTitleContaining(title)
				.stream()
				.sorted(Comparator.comparing(Task::getDueDate).reversed())
				.map(Task::getTaskDao)
				.collect(Collectors.toList());
	}

	/**
	 * Creates a comment on a specific task by the logged-in user.
	 */
	@Override
	public CommentDao createComment(Long taskId, String content) {
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		User user = jwtUtil.getLoggedInUser();

		if (optionalTask.isPresent() && user != null) {
		    Comment comment = new Comment();
		    comment.setCreatedAt(new Date());
		    comment.setContent(content);
		    comment.setTask(optionalTask.get());
		    comment.setUser(user);
		    return commentRepository.save(comment).getCommentDao();
		}

		throw new EntityNotFoundException("User or Task not found");
	}

	/**
	 * Retrieves all comments associated with a specific task.
	 */
	@Override
	public List<CommentDao> getCommentsByTaskId(Long taskId) {
		return commentRepository.findAllByTaskId(taskId)
				.stream()
				.map(Comment::getCommentDao)
				.collect(Collectors.toList());
	}
}
