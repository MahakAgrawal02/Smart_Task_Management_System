package com.demo.entities;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.demo.dao.CommentDao;
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
 * Entity class representing a comment made by a user on a task.
 */
@Data
@Entity
public class Comment {

    /**
     * Primary key - unique identifier for the comment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Content/text of the comment.
     */
    private String content;

    /**
     * Timestamp of when the comment was created.
     */
    private Date createdAt;

    /**
     * The user who posted the comment.
     * Many comments can be posted by one user.
     * On user deletion, related comments are also deleted (CASCADE).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    /**
     * The task to which this comment is associated.
     * Many comments can belong to one task.
     * On task deletion, related comments are also deleted (CASCADE).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task;

    /**
     * Converts the entity to a data access object (DAO) for API responses or transfer.
     * 
     * @return CommentDao containing non-sensitive and essential comment details.
     */
    public CommentDao getCommentDao() {
        CommentDao commentDao = new CommentDao();
        commentDao.setId(id);
        commentDao.setContent(content);
        commentDao.setCreatedAt(createdAt);
        commentDao.setTaskId(task.getId());
        commentDao.setPostedBy(user.getName());
        return commentDao;
    }
}
