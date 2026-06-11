package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.LedgerRepositoryPort;
import com.example.tax_payment.domain.model.LedgerEntry;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.persistence.entity.LedgerEntryJpaEntity;
import com.example.tax_payment.persistence.mapper.LedgerPersistenceMapper;
import com.example.tax_payment.persistence.repository.SpringDataLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LedgerRepositoryAdapter implements LedgerRepositoryPort {

    private final SpringDataLedgerRepository repository;

    @Override
    public void saveAll(List<LedgerEntry> entries) {
        repository.saveAll(
                entries.stream().map(this::toEntity).toList()
        );
    }

    @Override
    public List<LedgerEntry> findAll() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private LedgerEntryJpaEntity toEntity(LedgerEntry entry) {
        LedgerEntryJpaEntity e = new LedgerEntryJpaEntity();
        e.setId(entry.getId());
        e.setTransactionId(entry.getTransactionId());
        e.setPaymentId(entry.getPaymentId());
        e.setInvoiceId(entry.getInvoiceId());
        e.setAccount(entry.getAccount());
        e.setType(entry.getType());
        e.setAmount(entry.getMoney().getAmount());
        e.setCurrency(entry.getMoney().getCurrency());
        e.setCreatedAt(entry.getCreatedAt());
        return e;
    }

    private LedgerEntry toDomain(LedgerEntryJpaEntity e) {
        return new LedgerEntry(
                e.getId(),
                e.getTransactionId(),
                e.getPaymentId(),
                e.getInvoiceId(),
                e.getAccount(),
                e.getType(),
                new Money(e.getAmount(), e.getCurrency()),
                e.getCreatedAt()
        );
    }
}