package com.stefanini.taskmanager.infrastructure.messaging;

import com.stefanini.taskmanager.domain.event.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consome eventos de tarefa do tópico Kafka e registra um log estruturado.
 * Representa o lado "event-driven" do fluxo: reage a mudanças de estado das
 * tarefas de forma desacoplada do caso de uso que as originou.
 */
@Slf4j
@Component
public class TaskEventConsumer {

    @KafkaListener(
            topics = "${app.kafka.topic.task-events}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void onTaskEvent(TaskEvent event) {
        log.info("Evento de tarefa recebido",
                StructuredArguments.keyValue("eventType", event.type()),
                StructuredArguments.keyValue("taskId", event.taskId()),
                StructuredArguments.keyValue("titulo", event.titulo()),
                StructuredArguments.keyValue("status", event.status()),
                StructuredArguments.keyValue("occurredAt", event.occurredAt()));
    }
}
