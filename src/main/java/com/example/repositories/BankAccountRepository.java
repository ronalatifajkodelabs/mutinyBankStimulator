package com.example.repositories;

import com.example.DTOs.BankAccountInformationDTO;
import com.example.accounts.BankAccount;
import com.example.accounts.CheckingBankAccount;
import com.example.accounts.SavingsBankAccount;
import com.example.enums.AccountType;
import com.example.enums.OrderType;
import com.example.inMemoryDBs.DB;
import com.example.transactions.Transaction;
import com.example.users.BankAccountHolder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.UniEmitter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.function.Consumer;

@ApplicationScoped
public class BankAccountRepository {

    public Uni<List<BankAccount>> getBankAccounts() {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super List<BankAccount>>>) em -> {
                    try {
                        em.complete(DB.bankAccounts);
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    public Uni<BankAccount> getBankAccount(String id) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super BankAccount>>) em -> {
                    try {
                        BankAccount bankAccount = DB.bankAccounts.stream().filter(u -> u.getAccountNumber().equals(id)).findFirst().orElse(null);
                        if (bankAccount != null) {
                            em.complete(bankAccount);
                        } else em.fail(new Exception("Bank Account not found"));
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    public Uni<BankAccount> addBankAccount(BankAccountInformationDTO bankAccountRegistrationDTO) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super BankAccount>>) em -> {
                    try {
                        UUID uuid = UUID.randomUUID();
                        if (bankAccountRegistrationDTO.getAccountType().equals(AccountType.CHECKING)) {
                            CheckingBankAccount checkingBankAccount = CheckingBankAccount.builder().accountHolder(BankAccountHolder.builder().firstName(bankAccountRegistrationDTO.getFirstName()).lastName(bankAccountRegistrationDTO.getLastName()).email(bankAccountRegistrationDTO.getEmail()).phoneNumber(bankAccountRegistrationDTO.getPhoneNumber()).build()).accountNumber(uuid.toString()).build();
                            DB.bankAccounts.add(checkingBankAccount);
                            em.complete(checkingBankAccount);
                        } else if (bankAccountRegistrationDTO.getAccountType().equals(AccountType.SAVINGS)) {
                            SavingsBankAccount savingsBankAccount = SavingsBankAccount.builder().accountHolder(BankAccountHolder.builder().firstName(bankAccountRegistrationDTO.getFirstName()).lastName(bankAccountRegistrationDTO.getLastName()).email(bankAccountRegistrationDTO.getEmail()).phoneNumber(bankAccountRegistrationDTO.getPhoneNumber()).build()).accountNumber(uuid.toString()).minimumBalance(bankAccountRegistrationDTO.getMinimumBalance()).build();
                            DB.bankAccounts.add(savingsBankAccount);
                            em.complete(savingsBankAccount);
                        } else {
                            em.fail(new Exception("Invalid account type"));
                        }
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    public Uni<String> deleteBankAccount(String accountNumber) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super String>>) em -> {
                    try {
                        BankAccount bankAccount = DB.bankAccounts.stream().filter(u -> u.getAccountNumber().equals(accountNumber)).findFirst().orElse(null);
                        if (bankAccount != null) {
                            DB.bankAccounts.remove(bankAccount);
                            em.complete("Bank account deleted successfully");
                        } else em.fail(new Exception("Bank Account not found"));
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    public Uni<BankAccount> updateBankAccount(String accountNumber, BankAccountInformationDTO bankAccountRegistrationDTO) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super BankAccount>>) em -> {
                    try {
                        BankAccount realBankAccount = DB.bankAccounts.stream().filter(u -> u.getAccountNumber().equals(accountNumber)).findFirst().orElse(null);
                        if (realBankAccount != null) {
                            realBankAccount.getAccountHolder().setFirstName(bankAccountRegistrationDTO.getFirstName() == null ? realBankAccount.getAccountHolder().getFirstName() : bankAccountRegistrationDTO.getFirstName());
                            realBankAccount.getAccountHolder().setLastName(bankAccountRegistrationDTO.getLastName() == null ? realBankAccount.getAccountHolder().getLastName() : bankAccountRegistrationDTO.getLastName());
                            realBankAccount.getAccountHolder().setEmail(bankAccountRegistrationDTO.getEmail() == null ? realBankAccount.getAccountHolder().getEmail() : bankAccountRegistrationDTO.getEmail());
                            realBankAccount.getAccountHolder().setPhoneNumber(bankAccountRegistrationDTO.getPhoneNumber() == null ? realBankAccount.getAccountHolder().getPhoneNumber() : bankAccountRegistrationDTO.getPhoneNumber());
                            realBankAccount.setAccountType(bankAccountRegistrationDTO.getAccountType() == null ? realBankAccount.getAccountType() : bankAccountRegistrationDTO.getAccountType());
                            realBankAccount.setPausedMonthlyBillingsUntil(bankAccountRegistrationDTO.getPausedMonthlyBillingsUntil() == null ? realBankAccount.getPausedMonthlyBillingsUntil() : bankAccountRegistrationDTO.getPausedMonthlyBillingsUntil());
                            em.complete(realBankAccount);
                        } else em.fail(new Exception("Bank Account not found"));
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    public Uni<List<BankAccount>> getBankAccountsWithFilters(String orderBy, String orderType, int limit, AccountType accountType) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super List<BankAccount>>>) em -> {
                    try {
                        if (orderBy.equals("income")) {
                            em.complete(getTopLimitPayingAccountsByType(accountType, limit, orderType.equals("ASC") ? OrderType.ASC : OrderType.DESC));
                        }
                         em.complete(DB.bankAccounts.stream().filter(bankAccount -> bankAccount.getAccountType().equals(accountType)).sorted((bankAccount1, bankAccount2) -> switch (orderBy) {
                            case "balanceAmount" ->
                                    orderType.equals("ASC") ? Double.compare(bankAccount1.getBalanceAmount(), bankAccount2.getBalanceAmount()) : Double.compare(bankAccount2.getBalanceAmount(), bankAccount1.getBalanceAmount());
                            case "accountNumber" ->
                                    orderType.equals("ASC") ? bankAccount1.getAccountNumber().compareTo(bankAccount2.getAccountNumber()) : bankAccount2.getAccountNumber().compareTo(bankAccount1.getAccountNumber());
                            default ->
                                    orderType.equals("ASC") ? bankAccount1.getAccountHolder().getFirstName().compareTo(bankAccount2.getAccountHolder().getFirstName()) : bankAccount2.getAccountHolder().getFirstName().compareTo(bankAccount1.getAccountHolder().getFirstName());
                        }).limit(limit).toList());
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    //TODO this is not reactive
    public List<BankAccount> getTopLimitPayingAccountsByType(AccountType accountType, int limit, OrderType orderType) {
        Map<BankAccount, Double> bankAccountToTotalAmountBilled = new HashMap<>();
        for (BankAccount bankAccount : DB.bankAccounts) {
            if (bankAccount.getAccountType().equals(accountType)) {
                List<Transaction> transactions = DB.transactions.stream()
                        .filter(transaction -> transaction.getSourceAccount().equals(bankAccount))
                        .toList();
                double totalAmount = transactions.stream()
                        .mapToDouble(Transaction::getAmount)
                        .sum();
                bankAccountToTotalAmountBilled.put(bankAccount, totalAmount);
            }
        }
        List<Map.Entry<BankAccount, Double>> listToSortMap = new LinkedList<>(bankAccountToTotalAmountBilled.entrySet());
        listToSortMap.sort(Map.Entry.comparingByValue());
        if (orderType.equals(OrderType.ASC)) {
            Collections.reverse(listToSortMap);
        }
        return listToSortMap.stream()
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    //TODO fix this
    public Uni<List<BankAccount>> getUniTopLimitPayingAccountsByType(AccountType accountType, int limit, OrderType orderType) {
        Map<BankAccount, Double> bankAccountToTotalAmountBilled = new HashMap<>();
        Multi<BankAccount> bankAccountMulti = Multi.createFrom().iterable(DB.bankAccounts);
        return bankAccountMulti.filter(bankAccount -> bankAccount.getAccountType().equals(accountType)).onItem().transformToUniAndMerge(bankAccount -> {
            List<Transaction> transactions = DB.transactions.stream().filter(transaction -> transaction.getSourceAccount().equals(bankAccount)).toList();
            double totalAmount = transactions.stream().mapToDouble(Transaction::getAmount).sum();
            bankAccountToTotalAmountBilled.put(bankAccount, totalAmount);
            return Uni.createFrom().item(bankAccount);
        }).collect().asMap(item -> item, bankAccountToTotalAmountBilled::get).onItem().transformToUni(map -> {
            List<Map.Entry<BankAccount, Double>> listToSortMap = new LinkedList<>(map.entrySet());
            listToSortMap.sort(Map.Entry.comparingByValue());
            if (orderType.equals(OrderType.ASC)) {
                Collections.reverse(listToSortMap);
            }
            return Uni.createFrom().item(listToSortMap.stream().limit(limit).map(Map.Entry::getKey).toList());
        });
    }

}
