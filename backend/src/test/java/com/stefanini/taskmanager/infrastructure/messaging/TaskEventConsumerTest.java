package com.stefanini.taskmanager.infrastructure.messaging;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.stefanini.taskmanager.domain.TaskStatus;
import com.stefanini.taskmanager.domain.event.TaskUpdatedEvent;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

class TaskEventConsumerTest {

    @Test
    void onTaskEvent_deveProcessarEventoSemLancarExcecao() {
        TaskEventConsumer consumer = new TaskEventConsumer();
        TaskUpdatedEvent event = new TaskUpdatedEvent(7L, "Tarefa", TaskStatus.EM_ANDAMENTO, OffsetDateTime.now());

        assertThatCode(() -> consumer.onTaskEvent(event)).doesNotThrowAnyException();
    }
}
