package com.example.tax_payment.persistence.mapper;

import com.example.tax_payment.domain.model.LedgerEntry;
import com.example.tax_payment.persistence.entity.LedgerEntryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class LedgerPersistenceMapper {

    public LedgerEntryJpaEntity toEntity(LedgerEntry entry) {

        LedgerEntryJpaEntity e = new LedgerEntryJpaEntity();

        e.setId(entry.getId());
        e.setTransactionId(entry.getTransactionId());
        e.setPaymentId(entry.getPaymentId());
        e.setInvoiceId(entry.getInvoiceId());

        e.setAccount(entry.getAccount());
        e.setType(entry.getType());

        if (entry.getMoney() != null) {
            e.setAmount(entry.getMoney().getAmount());
            e.setCurrency(entry.getMoney().getCurrency());
        }

        e.setCreatedAt(entry.getCreatedAt());

        return e;
    }
}