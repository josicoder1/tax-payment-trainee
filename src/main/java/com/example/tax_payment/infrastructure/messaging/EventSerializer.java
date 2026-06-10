package com.example.tax_payment.infrastructure.messaging;

import com.example.tax_payment.domain.event.DomainEvent;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

/**
 * Serializes and deserializes domain events to/from JSON format.
 * Supports polymorphic event types and Java 8 time types (Instant, LocalDateTime).
 */
@Component
public class EventSerializer {

    private final ObjectMapper objectMapper;

    public EventSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        
        // Register JSR310 module for Java 8 time type support
        if (!this.objectMapper.getRegisteredModuleIds().contains(new JavaTimeModule().getTypeId())) {
            this.objectMapper.registerModule(new JavaTimeModule());
        }
        
        // Configure polymorphic type handling for DomainEvent hierarchy
        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(DomainEvent.class)
                .build();
        
        this.objectMapper.activateDefaultTyping(
                typeValidator,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
    }

    /**
     * Serializes a DomainEvent to a compact JSON string.
     * The JSON includes a @type field for polymorphic deserialization.
     *
     * @param event the domain event to serialize
     * @return compact JSON string representation
     * @throws IllegalStateException if serialization fails
     */
    public String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Failed to serialize event of type " + event.getClass().getName() 
                    + ": " + e.getMessage(), 
                    e
            );
        }
    }

    /**
     * Deserializes a JSON string back to a DomainEvent instance.
     * Uses the @type field in JSON for polymorphic deserialization.
     *
     * @param json the JSON string to deserialize
     * @return the deserialized domain event
     * @throws IllegalStateException if deserialization fails
     */
    public DomainEvent deserialize(String json) {
        try {
            return objectMapper.readValue(json, DomainEvent.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Failed to deserialize event from JSON: " + e.getMessage(),
                    e
            );
        }
    }
}
