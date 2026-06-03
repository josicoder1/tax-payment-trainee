package com.example.tax_payment.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application configuration class for the tax payment system.
 * Enables scheduling for background tasks like the outbox poller.
 * 
 * Scheduling is disabled in the test profile to prevent background
 * interference during deterministic testing.
 */
@Configuration
@EnableScheduling
@Profile("!test")
public class AppConfig {
}
