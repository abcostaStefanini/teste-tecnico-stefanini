package com.stefanini.taskmanager.domain.event;

import com.stefanini.taskmanager.domain.Task;
import com.stefanini.taskmanager.domain.TaskStatus;
import java.time.OffsetDateTime;

/**
 * Evento de domínio disparado quando uma tarefa é atualizada.
 */
public record TaskUpdatedEvent(
        Long taskId,
        String titulo,
        TaskStatus status,
        OffsetDateTime occurredAt
) implements TaskEvent {

    public static TaskUpdatedEvent from(Task task) {
        return new TaskUpdatedEvent(
                task.getId(),
                task.getTitulo(),
                task.getStatus(),
                OffsetDateTime.now()
        );
    }

    @Override
    public TaskEventType type() {
        return TaskEventType.UPDATED;
    }
}
