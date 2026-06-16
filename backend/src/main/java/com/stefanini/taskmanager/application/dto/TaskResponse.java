package com.stefanini.taskmanager.application.dto;

import com.stefanini.taskmanager.domain.Task;
import com.stefanini.taskmanager.domain.TaskStatus;
import java.time.OffsetDateTime;

public record TaskResponse(
        Long id,
        String titulo,
        String descricao,
        TaskStatus status,
        OffsetDateTime dataCriacao
) {

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitulo(),
                task.getDescricao(),
                task.getStatus(),
                task.getDataCriacao()
        );
    }
}
