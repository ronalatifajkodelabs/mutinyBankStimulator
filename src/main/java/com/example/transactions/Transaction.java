package com.example.transactions;


import lombok.Getter;
import lombok.Setter;
import com.example.accounts.BankAccount;
import com.example.adapters.JsonSubtype;
import com.example.adapters.JsonType;
import com.example.enums.TransactionStatus;
import com.example.enums.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonType(
        property = "transactionType",
        subtypes = {
                @JsonSubtype(clazz = BankBillingTransaction.class, name = "BANK_BILLING"),
                @JsonSubtype(clazz = DepositTransaction.class, name = "DEPOSIT"),
                @JsonSubtype(clazz = TransferTransaction.class, name = "TRANSFER"),
                @JsonSubtype(clazz = WithdrawTransaction.class, name = "WITHDRAWAL")
        }
)
public abstract class Transaction {

    private BankAccount sourceAccount;
    private double amount; //amountBilled
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
    private TransactionStatus transactionStatus;

    Transaction(BankAccount sourceAccount, double amount, TransactionType transactionType, LocalDateTime transactionTime, TransactionStatus transactionStatus) {
        this.sourceAccount = sourceAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
    }

    public abstract void performTransaction();

    public abstract String toString();

    public void perform(double amount, BankAccount... accounts) {
        if (accounts.length == 1) {

        } else if (accounts.length == 2) {

        } else {
            throw new IllegalArgumentException("Invalid number of accounts");
        }
    }

}
