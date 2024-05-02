package com.example.utils;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import com.example.accounts.BankAccount;
import com.example.transactions.Transaction;
import com.example.users.BankAccountHolder;
import com.example.users.BankEmployee;
import com.example.users.BankOwner;
import com.example.users.BankUser;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class DBInitializer {

    void onStart(@Observes StartupEvent ev) {
        System.out.println("The application is starting...");
        List<BankAccount> bankAccountsFromJSON = com.example.inMemoryDBs.DB.fillData("src/main/java/com/example/jsonFiles/bankAccounts.json", BankAccount.class);
        List<BankUser> bankUsersFromJSON = com.example.inMemoryDBs.DB.fillData("src/main/java/com/example/jsonFiles/bankUsers.json", BankUser.class);
//        List<? extends BankUser> bankUsersFromJson = com.example.inMemoryDBs.DB.fillData("src/main/java/com/example/jsonFiles/bankAccountHolders.json", BankAccountHolder.class);
//        List<? extends BankUser> bankEmployeesFromJson = com.example.inMemoryDBs.DB.fillData("src/main/java/com/example/jsonFiles/bankEmployees.json", BankEmployee.class);
//        List<? extends BankUser> bankOwnersFromJson = com.example.inMemoryDBs.DB.fillData("src/main/java/com/example/jsonFiles/bankOwners.json", BankOwner.class);
//        List<BankUser> bankUsersFromJSON = new LinkedList<>();
//        bankUsersFromJSON.addAll(bankUsersFromJson);
//        bankUsersFromJSON.addAll(bankEmployeesFromJson);
//        bankUsersFromJSON.addAll(bankOwnersFromJson);
        List<Transaction> transactionsFromJSON = com.example.inMemoryDBs.DB.fillData("src/main/java/com/example/jsonFiles/transactions.json", Transaction.class);

        com.example.inMemoryDBs.DB.bankAccounts.addAll(bankAccountsFromJSON);
        com.example.inMemoryDBs.DB.bankUsers.addAll(bankUsersFromJSON);
        com.example.inMemoryDBs.DB.transactions.addAll(transactionsFromJSON);
    }
}
