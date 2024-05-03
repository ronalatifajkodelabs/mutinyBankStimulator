package com.example.repositories;

import com.example.accounts.BankAccount;
import com.example.inMemoryDBs.DB;
import com.example.transactions.Transaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.UniEmitter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.function.Consumer;

@ApplicationScoped
public class TransactionRepository {

    public Uni<List<Transaction>> getTransactions() {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super List<Transaction>>>) em -> {
                    try {
                        em.complete(DB.transactions);
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

}
