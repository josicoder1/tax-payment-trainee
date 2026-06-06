package com.example.tax_payment.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration for event publishing infrastructure.
 * Provides KafkaTemplate for reliable message publishing to Apache Kafka.
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    /**
     * Configure Kafka producer factory with String serializers for both key and value.
     * Uses acks=all and retries=3 for reliable message delivery.
     *
     * @return ProducerFactory configured for reliable message publishing
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Require all in-sync replicas to acknowledge (strongest durability guarantee)
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        // Retry failed sends up to 3 times
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Create KafkaTemplate for sending messages to Kafka topics.
     *
     * @param producerFactory the producer factory with serializer configuration
     * @return KafkaTemplate for message publishing
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Configure Jackson ObjectMapper with JSR310 (Java 8 Time) module support.
     * This enables serialization of Instant, LocalDateTime, and other java.time types.
     *
     * @return ObjectMapper configured for Java 8 time type serialization
     */
    // Intentionally no ObjectMapper @Bean here; rely on Spring Boot auto-configuration.
}
