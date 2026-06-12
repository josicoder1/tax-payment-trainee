package com.example.tax_payment.persistence.adapter;

import com.example.tax_payment.application.port.outbound.TransactionRepositoryPort;
import com.example.tax_payment.domain.model.Transaction;
import com.example.tax_payment.domain.valueobject.Money;
import com.example.tax_payment.persistence.entity.TransactionJpaEntity;
import com.example.tax_payment.persistence.repository.SpringDataTransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepositoryAdapter
        implements TransactionRepositoryPort {

    private final SpringDataTransactionRepository repository;

    public TransactionRepositoryAdapter(
            SpringDataTransactionRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(Transaction transaction) {
        repository.save(toEntity(transaction));
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByInvoiceId(UUID invoiceId) {
        return repository.findByInvoiceIdOrderByCreatedAtDesc(invoiceId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByPaymentId(UUID paymentId) {
        return repository.findByPaymentIdOrderByCreatedAtDesc(paymentId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private TransactionJpaEntity toEntity(Transaction transaction) {
        TransactionJpaEntity entity = new TransactionJpaEntity();
        entity.setId(transaction.getId());
        entity.setInvoiceId(transaction.getInvoiceId());
        entity.setPaymentId(transaction.getPaymentId());
        entity.setType(transaction.getType());
        entity.setDescription(transaction.getDescription());
        if (transaction.getMoney() != null) {
            entity.setAmount(transaction.getMoney().getAmount());
            entity.setCurrency(transaction.getMoney().getCurrency());
        }
        entity.setCreatedAt(transaction.getCreatedAt());
        return entity;
    }

    private Transaction toDomain(TransactionJpaEntity entity) {
        Money money = entity.getAmount() != null && entity.getCurrency() != null
                ? new Money(entity.getAmount(), entity.getCurrency())
                : null;
        return new Transaction(
                entity.getId(),
                entity.getInvoiceId(),
                entity.getPaymentId(),
                entity.getType(),
                entity.getDescription(),
                money,
                entity.getCreatedAt()
        );
    }
}
