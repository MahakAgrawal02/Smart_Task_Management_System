package com.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entities.Task;

/**
 * Repository interface for Task entity.
 * Provides basic CRUD operations and custom query methods for tasks.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Retrieves all tasks where the title contains the given keyword (case-sensitive).
     *
     * @param title the keyword to search within task titles
     * @return list of matching tasks
     */
    List<Task> findAllByTitleContaining(String title);

    /**
     * Retrieves all tasks assigned to a specific user by user ID.
     *
     * @param id the ID of the user
     * @return list of tasks assigned to the user
     */
    List<Task> findAllByUserId(Long id);

}
