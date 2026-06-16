package com.stefanini.taskmanager.application;

import com.stefanini.taskmanager.application.dto.CreateTaskRequest;
import com.stefanini.taskmanager.application.dto.TaskResponse;
import com.stefanini.taskmanager.application.dto.UpdateTaskRequest;
import com.stefanini.taskmanager.application.exception.TaskNotFoundException;
import com.stefanini.taskmanager.domain.Task;
import com.stefanini.taskmanager.domain.TaskStatus;
import com.stefanini.taskmanager.domain.event.TaskCreatedEvent;
import com.stefanini.taskmanager.domain.event.TaskUpdatedEvent;
import com.stefanini.taskmanager.infrastructure.messaging.TaskEventProducer;
import com.stefanini.taskmanager.infrastructure.persistence.TaskRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso de gerenciamento de tarefas: CRUD completo e publicação de
 * eventos de domínio ao criar e atualizar tarefas.
 */
@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventProducer eventProducer;

    public TaskService(TaskRepository taskRepository, TaskEventProducer eventProducer) {
        this.taskRepository = taskRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public TaskResponse create(CreateTaskRequest request) {
        Task task = Task.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .status(request.status() != null ? request.status() : TaskStatus.PENDENTE)
                .build();

        Task saved = taskRepository.save(task);
        log.info("Tarefa criada id={}", saved.getId());

        eventProducer.publish(TaskCreatedEvent.from(saved));
        return TaskResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(Long id) {
        return TaskResponse.from(getOrThrow(id));
    }

    @Transactional
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        Task task = getOrThrow(id);
        task.setTitulo(request.titulo());
        task.setDescricao(request.descricao());
        task.setStatus(request.status());

        Task saved = taskRepository.save(task);
        log.info("Tarefa atualizada id={}", saved.getId());

        eventProducer.publish(TaskUpdatedEvent.from(saved));
        return TaskResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        Task task = getOrThrow(id);
        taskRepository.delete(task);
        log.info("Tarefa removida id={}", id);
    }

    private Task getOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
