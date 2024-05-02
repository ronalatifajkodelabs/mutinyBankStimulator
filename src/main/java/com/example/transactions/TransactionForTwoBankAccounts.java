package com.example.transactions;


import com.example.accounts.BankAccount;
import com.example.accounts.SavingsBankAccount;
import com.example.enums.AccountType;
import com.example.enums.TransactionStatus;
import com.example.inMemoryDBs.DB;

public interface TransactionForTwoBankAccounts {
    default void performTransaction(BankAccount sourceAccount, BankAccount targetAccount, double amount) {
        if (sourceAccount == targetAccount) {
            System.out.println("You can't transfer money to the same account");
            return;
        }
        if (sourceAccount == null || targetAccount == null) {
            System.out.println("At least one of the accounts was not found");
            return;
        }
        if (sourceAccount.getAccountType().equals(AccountType.SAVINGS)) {
            if (amount > ((SavingsBankAccount) sourceAccount).getMinimumBalance()) {
                System.out.println("You can't transfer more than the minimum balance of the account");
            }
        }
        sourceAccount.setBalanceAmount(sourceAccount.getBalanceAmount() - amount);
        targetAccount.setBalanceAmount(targetAccount.getBalanceAmount() + amount);
        DB.transactions.add(
                TransferTransaction.builder()
                        .sourceAccount(sourceAccount)
                        .amount(amount)
                        .transactionTime(java.time.LocalDateTime.now())
                        .transactionStatus(TransactionStatus.COMPLETED)
                        .destinationAccount(targetAccount)
                        .build());
        System.out.println("Successful");
    }
}
