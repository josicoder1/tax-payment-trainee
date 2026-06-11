package com.example.tax_payment.api.controller;

import com.example.tax_payment.application.port.outbound.LedgerRepositoryPort;
import com.example.tax_payment.domain.model.LedgerEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {

    private final LedgerRepositoryPort ledgerRepository;

    public LedgerController(LedgerRepositoryPort ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @GetMapping
    public List<LedgerEntry> getAll() {
        return ledgerRepository.findAll();
    }
}