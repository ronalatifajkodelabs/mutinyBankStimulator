package com.example.accounts;


import lombok.*;
import com.example.enums.AccountType;
import com.example.enums.TransactionStatus;
import com.example.inMemoryDBs.DB;
import com.example.transactions.BankBillingTransaction;
import com.example.transactions.Transaction;
import com.example.users.BankAccountHolder;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SavingsBankAccount extends BankAccount {

    public static final double INTEREST_RATE = 0.0399;
    private double minimumBalance;

    public SavingsBankAccount() {
        super();
    }

    @Builder
    public SavingsBankAccount(String accountNumber, BankAccountHolder accountHolder, double minimumBalance) {
        super(accountNumber, accountHolder, AccountType.SAVINGS);
        this.minimumBalance = minimumBalance;
    }

    public void runMonthlyUpdate() {
        if (this.getBalanceAmount() < (this.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE)) {
            System.out.println("Monthly fee of " + (this.getBalanceAmount() - (this.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE)) + " for saving account " + this.getAccountNumber() + " has not been deducted due to insufficient funds");
            return;
        }
        System.out.println("Monthly fee of " + this.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE + " for saving account " + this.getAccountNumber() + " has been deducted");
        Transaction bankServicesBillingTransaction = BankBillingTransaction.builder()
                .amount(this.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE)
                .sourceAccount(this)
                .transactionTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();
        this.setBalanceAmount(this.getBalanceAmount() - (this.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE));
        DB.transactions.add(bankServicesBillingTransaction);
    }

    public SavingsBankAccount updateSavingsBankAccount(SavingsBankAccount bankAccount) {
        this.setAccountHolder((bankAccount.getAccountHolder().equals(this.getAccountHolder()) ? bankAccount.getAccountHolder() : this.getAccountHolder()));
        this.setBalanceAmount((bankAccount.getBalanceAmount() == this.getBalanceAmount() ? bankAccount.getBalanceAmount() : this.getBalanceAmount()));
        this.setAccountNumber((bankAccount.getAccountNumber().equals(this.getAccountNumber()) ? bankAccount.getAccountNumber() : this.getAccountNumber()));
        this.setBalanceAmount((bankAccount.getBalanceAmount() == this.getBalanceAmount() ? bankAccount.getBalanceAmount() : this.getBalanceAmount()));
        this.setPausedMonthlyBillingsUntil((bankAccount.getPausedMonthlyBillingsUntil().equals(this.getPausedMonthlyBillingsUntil()) ? bankAccount.getPausedMonthlyBillingsUntil() : this.getPausedMonthlyBillingsUntil()));
        this.minimumBalance = (bankAccount.getMinimumBalance() == this.getMinimumBalance() ? bankAccount.getMinimumBalance() : this.getMinimumBalance());
        return this;
    }
}

