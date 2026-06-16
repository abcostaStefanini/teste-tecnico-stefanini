package com.stefanini.taskmanager.infrastructure.persistence;

import com.stefanini.taskmanager.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade {@link Task}.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
