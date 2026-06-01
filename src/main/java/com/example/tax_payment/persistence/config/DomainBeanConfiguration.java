package com.example.tax_payment.persistence.config;

import com.example.tax_payment.domain.service.PaymentAllocationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanConfiguration {

    @Bean
    public PaymentAllocationService paymentAllocationService() {
        return new PaymentAllocationService();
    }
}