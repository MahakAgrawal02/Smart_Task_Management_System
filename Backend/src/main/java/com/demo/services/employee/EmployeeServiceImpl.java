package com.demo.services.employee;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	
	private final TaskRepository taskRepository;
	private final JwtUtil jwtUtil;
	private final CommentRepository commentRepository;

	@Override
	public List<TaskDao> getTasksByUserId() {
		User user = jwtUtil.getLoggedInUser();
		if(user!=null) {
			return taskRepository.findAllByUserId(user.getId())
					.stream()
					.sorted(Comparator.comparing(Task::getDueDate).reversed())
					.map(Task::getTaskDao)
					.collect(Collectors.toList());
		}
		throw new EntityNotFoundException("User not found");
	}

	@Override
	public TaskDao updateTask(Long id, String status) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isPresent()) {
		    Task existingTask = optionalTask.get();
		    existingTask.setTaskStatus(mapStringToTaskStatus(status));
		    return taskRepository.save(existingTask).getTaskDao();
		}

		throw new EntityNotFoundException("Task not found");
	}
	
	private TaskStatus mapStringToTaskStatus(String status) {
	    return switch (status) {
	        case "PENDING"     -> TaskStatus.PENDING;
	        case "INPROGRESS"  -> TaskStatus.INPROGRESS;
	        case "COMPLETED"   -> TaskStatus.COMPLETED;
	        case "DEFERRED"    -> TaskStatus.DEFERRED;
	        default            -> TaskStatus.CANCELLED;
	    };
	}

	@Override
	public TaskDao getTaskById(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		return optionalTask.map(Task::getTaskDao).orElse(null);
	}

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

	@Override
	public List<CommentDao> getCommentsByTaskId(Long taskId) {
		return commentRepository.findAllByTaskId(taskId)
				.stream()
				.map(Comment::getCommentDao)
				.collect(Collectors.toList());
	}

}
