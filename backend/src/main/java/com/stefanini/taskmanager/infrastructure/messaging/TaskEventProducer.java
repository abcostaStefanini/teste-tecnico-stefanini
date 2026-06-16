package com.stefanini.taskmanager.infrastructure.messaging;

import com.stefanini.taskmanager.domain.event.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publica eventos de domínio de tarefa no tópico Kafka configurado.
 */
@Slf4j
@Component
public class TaskEventProducer {

    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;
    private final String topic;

    public TaskEventProducer(KafkaTemplate<String, TaskEvent> kafkaTemplate,
                             @Value("${app.kafka.topic.task-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Publica um evento usando o id da tarefa como chave da mensagem,
     * garantindo ordenação por tarefa dentro da partição.
     */
    public void publish(TaskEvent event) {
        String key = String.valueOf(event.taskId());
        log.info("Publicando evento {} para tarefa {} no topico {}",
                event.type(), event.taskId(), topic);
        kafkaTemplate.send(topic, key, event);
    }
}
