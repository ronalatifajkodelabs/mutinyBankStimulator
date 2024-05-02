package com.example.transactions;


import com.example.accounts.BankAccount;
import com.example.enums.TransactionStatus;
import com.example.inMemoryDBs.DB;

public interface TransactionForOneBankAccount {
    default void performTransaction(BankAccount bankAccount, double amount) {
        bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() + amount);
        DB.transactions.add(
                DepositTransaction.builder()
                        .sourceAccount(bankAccount)
                        .amount(amount)
                        .transactionTime(java.time.LocalDateTime.now())
                        .transactionStatus(TransactionStatus.COMPLETED)
                        .build());
        System.out.println("Successful");
    }
}
