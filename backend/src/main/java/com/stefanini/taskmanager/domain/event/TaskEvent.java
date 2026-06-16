package com.stefanini.taskmanager.domain.event;

import com.stefanini.taskmanager.domain.TaskStatus;
import java.time.OffsetDateTime;

/**
 * Contrato comum dos eventos de domínio de uma tarefa, usado para
 * serialização e consumo genérico no listener Kafka.
 */
public sealed interface TaskEvent permits TaskCreatedEvent, TaskUpdatedEvent {

    TaskEventType type();

    Long taskId();

    String titulo();

    TaskStatus status();

    OffsetDateTime occurredAt();
}
