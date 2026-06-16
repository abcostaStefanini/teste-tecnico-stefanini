package com.stefanini.taskmanager.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Declaração do tópico de eventos de tarefa. O Spring Kafka cria o tópico
 * automaticamente (via KafkaAdmin) caso ainda não exista no broker.
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic taskEventsTopic(
            @Value("${app.kafka.topic.task-events}") String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
