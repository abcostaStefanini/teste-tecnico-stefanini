package com.stefanini.taskmanager.infrastructure.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.stefanini.taskmanager.domain.TaskStatus;
import com.stefanini.taskmanager.domain.event.TaskCreatedEvent;
import com.stefanini.taskmanager.domain.event.TaskEvent;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class TaskEventProducerTest {

    private static final String TOPIC = "task-events";

    @Mock
    private KafkaTemplate<String, TaskEvent> kafkaTemplate;

    @Test
    void publish_deveEnviarEventoComChaveSendoOIdDaTarefa() {
        TaskEventProducer producer = new TaskEventProducer(kafkaTemplate, TOPIC);
        TaskCreatedEvent event = new TaskCreatedEvent(42L, "Tarefa", TaskStatus.PENDENTE, OffsetDateTime.now());

        producer.publish(event);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TaskEvent> valueCaptor = ArgumentCaptor.forClass(TaskEvent.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), valueCaptor.capture());

        assertThat(topicCaptor.getValue()).isEqualTo(TOPIC);
        assertThat(keyCaptor.getValue()).isEqualTo("42");
        assertThat(valueCaptor.getValue()).isEqualTo(event);
    }
}
