package com.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entities.Comment;

/**
 * Repository interface for Comment entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves all comments associated with a specific task ID.
     *
     * @param taskId the ID of the task
     * @return list of comments linked to the task
     */
    List<Comment> findAllByTaskId(Long taskId);

}
