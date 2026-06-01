package com.example.tax_payment.persistence.config;

import com.example.tax_payment.persistence.repository.SpringDataPaymentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = SpringDataPaymentRepository.class)
public class PersistenceConfig {
}
