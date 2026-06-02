package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.InvoiceRepositoryPort;
import com.example.tax_payment.domain.model.Invoice;
import com.example.tax_payment.domain.valueobject.InvoiceStatus;
import com.example.tax_payment.persistence.mapper.InvoicePersistenceMapper;
import com.example.tax_payment.persistence.repository.SpringDataInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InvoiceRepositoryAdapter
        implements InvoiceRepositoryPort {

    private final SpringDataInvoiceRepository repository;
    private final InvoicePersistenceMapper mapper;

    @Override
    public Invoice save(Invoice invoice) {

        return mapper.toDomain(
                repository.save(
                        mapper.toEntity(invoice)
                )
        );
    }

    @Override
    public Optional<Invoice> findById(UUID id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Invoice> findByTaxpayerTin(
            String taxpayerTin
    ) {

        return repository.findByTaxpayerTin(
                        taxpayerTin
                )
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
