package com.example.services;

import com.example.transactions.Transaction;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import com.example.inMemoryDBs.DB;
import com.example.utils.CustomResponse;

import java.util.List;

@ApplicationScoped
public class TransactionService {

    Multi<Transaction> transactionMulti = Multi.createFrom().iterable(DB.transactions);
    Uni<List<Transaction>> transactionUni = Uni.createFrom().item(DB.transactions);

    public Uni<List<Transaction>> getTransactions() {
        return transactionUni;
    }

}
