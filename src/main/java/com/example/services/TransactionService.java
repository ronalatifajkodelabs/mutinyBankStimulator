package com.example.services;

import com.example.repositories.TransactionRepository;
import com.example.transactions.Transaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;

    public Uni<List<Transaction>> getTransactions() {
        return transactionRepository.getTransactions();
    }

}
