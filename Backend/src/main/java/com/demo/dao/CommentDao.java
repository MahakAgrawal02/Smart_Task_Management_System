package com.demo.dao;

import java.util.Date;
import lombok.Data;

/**
 * Data Transfer Object representing a comment made on a task.
 */
@Data
public class CommentDao {
	
	/**
	 * Unique identifier for the comment.
	 */
	private Long id;

	/**
	 * The content/text of the comment.
	 */
	private String content;

	/**
	 * The date and time when the comment was created.
	 */
	private Date createdAt;

	/**
	 * ID of the task to which this comment belongs.
	 */
	private Long taskId;

	/**
	 * ID of the user who posted the comment.
	 */
	private Long userId;

	/**
	 * Name or identifier of the user who posted the comment.
	 */
	private String postedBy;
}
