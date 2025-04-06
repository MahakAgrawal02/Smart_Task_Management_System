package com.demo.dao;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDao {
	
	private Long id; 
	private String content;
	private Date createdAt;
	private Long taskId;
	private Long userId;
	private String postedBy;

}
