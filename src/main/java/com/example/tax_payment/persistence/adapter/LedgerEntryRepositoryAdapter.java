package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.LedgerEntryRepositoryPort;
import com.example.tax_payment.domain.model.LedgerEntry;
import com.example.tax_payment.domain.valueobject.EntrySide;
import com.example.tax_payment.domain.valueobject.LedgerAccount;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.persistence.entity.LedgerEntryJpaEntity;
import com.example.tax_payment.persistence.repository.SpringDataLedgerEntryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class LedgerEntryRepositoryAdapter
        implements LedgerEntryRepositoryPort {

    private final SpringDataLedgerEntryRepository repository;

    public LedgerEntryRepositoryAdapter(
            SpringDataLedgerEntryRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<LedgerEntry> entries) {
        repository.saveAll(
                entries.stream()
                        .map(this::toEntity)
                        .toList()
        );
    }

    @Override
    public List<LedgerEntry> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<LedgerEntry> findByInvoiceId(UUID invoiceId) {
        return repository.findByInvoiceIdOrderByCreatedAtDesc(invoiceId).stream()
                .map(this::toDomain)
                .toList();
    }

    private LedgerEntryJpaEntity toEntity(LedgerEntry entry) {
        LedgerEntryJpaEntity entity = new LedgerEntryJpaEntity();
        entity.setId(entry.getId());
        entity.setInvoiceId(entry.getInvoiceId());
        entity.setPaymentId(entry.getPaymentId());
        entity.setTransactionId(entry.getTransactionId());
        entity.setAccount(entry.getAccount().name());
        entity.setEntrySide(entry.getEntrySide().name());
        entity.setAmount(entry.getMoney().getAmount());
        entity.setCurrency(entry.getMoney().getCurrency());
        entity.setDescription(entry.getDescription());
        entity.setCreatedAt(entry.getCreatedAt());
        return entity;
    }

    private LedgerEntry toDomain(LedgerEntryJpaEntity entity) {
        return new LedgerEntry(
                entity.getId(),
                entity.getInvoiceId(),
                entity.getPaymentId(),
                entity.getTransactionId(),
                LedgerAccount.valueOf(entity.getAccount()),
                EntrySide.valueOf(entity.getEntrySide()),
                entity.getDescription(),
                new Money(entity.getAmount(), entity.getCurrency()),
                entity.getCreatedAt()
        );
    }
}
