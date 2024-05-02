package com.example.transactions;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.example.accounts.BankAccount;
import com.example.enums.TransactionStatus;
import com.example.enums.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
public class BankBillingTransaction extends Transaction {

    @Builder
    BankBillingTransaction(BankAccount sourceAccount, double amount, LocalDateTime transactionTime, TransactionStatus transactionStatus) {
        super(sourceAccount, amount, TransactionType.BANK_BILLING, transactionTime, transactionStatus);
    }

    @Override
    public void performTransaction() {

    }

    @Override
    public String toString() {
        return "Billing Transaction for account of: " + this.getSourceAccount().getAccountHolder().getFirstName() + " " + this.getSourceAccount().getAccountHolder().getLastName() +
                "\n     Transaction Type: " + this.getTransactionType() +
                "\n     Amount: " + this.getAmount() +
                "\n     Transaction Time: " + this.getTransactionTime() +
                "\n     Transaction Status: " + this.getTransactionStatus();
    }

}
