package com.example.accounts;


import com.example.enums.AccountType;
import com.example.enums.TransactionStatus;
import com.example.inMemoryDBs.DB;
import com.example.transactions.BankBillingTransaction;
import com.example.transactions.Transaction;
import com.example.users.BankAccountHolder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CheckingBankAccount extends BankAccount {

    public static final double MONTHLY_FEE = 13.5;

    public CheckingBankAccount() {
        super();
    }

    @Builder
    public CheckingBankAccount(String accountNumber, BankAccountHolder accountHolder) {
        super(accountNumber, accountHolder, AccountType.CHECKING);
    }

    @Override
    public void runMonthlyUpdate() {
        if (this.getPausedMonthlyBillingsUntil() != null && this.getPausedMonthlyBillingsUntil().isAfter(LocalDateTime.now())) {
            return;
        }
        if (this.getBalanceAmount() < CheckingBankAccount.MONTHLY_FEE) {
            System.out.println("Monthly fee of " + CheckingBankAccount.MONTHLY_FEE + " for checking account " + this.getAccountNumber() + "has not been deducted due to insufficient funds");
            return;
        }
        this.setBalanceAmount(this.getBalanceAmount() - (CheckingBankAccount.MONTHLY_FEE));
        Transaction bankServicesBillingTransaction = BankBillingTransaction.builder()
                .amount(CheckingBankAccount.MONTHLY_FEE)
                .sourceAccount(this)
                .transactionTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();
        DB.transactions.add(bankServicesBillingTransaction);
        System.out.println("Monthly fee of " + CheckingBankAccount.MONTHLY_FEE + " for checking account " + this.getAccountNumber() + "has been deducted");
    }

}
