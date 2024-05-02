package com.example.services;

import com.example.enums.OrderType;
import com.example.users.BankUser;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import com.example.DTOs.BankAccountRegistrationDTO;
import com.example.DTOs.FilterParameters;
import com.example.accounts.BankAccount;
import com.example.accounts.CheckingBankAccount;
import com.example.accounts.SavingsBankAccount;
import com.example.enums.AccountType;
import com.example.inMemoryDBs.DB;
import com.example.transactions.Transaction;
import com.example.users.BankAccountHolder;
import com.example.utils.CustomResponse;

import java.util.*;
import java.util.logging.Filter;

import static com.example.utils.Util.validate;

@ApplicationScoped
public class BankAccountService {

    Multi<BankAccount> bankAccountMulti = Multi.createFrom().iterable(DB.bankAccounts);
    Uni<List<BankAccount>> bankAccountUni = Uni.createFrom().item(DB.bankAccounts);

    public Uni<List<BankAccount>> getBankAccounts() {
        return bankAccountUni;
    }

    public Uni<BankAccount> addBankAccount(BankAccountRegistrationDTO bankAccountRegistrationDTO) {
        UUID uuid = UUID.randomUUID();
        if (bankAccountRegistrationDTO.getAccountType().equals(AccountType.CHECKING)) {
            CheckingBankAccount checkingBankAccount = CheckingBankAccount.builder().accountHolder(BankAccountHolder.builder().firstName(bankAccountRegistrationDTO.getFirstName()).lastName(bankAccountRegistrationDTO.getLastName()).email(bankAccountRegistrationDTO.getEmail()).phoneNumber(bankAccountRegistrationDTO.getPhoneNumber()).build()).accountNumber(uuid.toString()).build();
            return bankAccountUni.onItem().invoke(bankAccounts -> {
                bankAccounts.add(checkingBankAccount);
            }).onItem().transformToUni(bankAccounts -> Uni.createFrom().item(bankAccounts.get(bankAccounts.size() - 1)));
        } else if (bankAccountRegistrationDTO.getAccountType().equals(AccountType.SAVINGS)) {
            SavingsBankAccount savingsBankAccount = SavingsBankAccount.builder().accountHolder(BankAccountHolder.builder().firstName(bankAccountRegistrationDTO.getFirstName()).lastName(bankAccountRegistrationDTO.getLastName()).email(bankAccountRegistrationDTO.getEmail()).phoneNumber(bankAccountRegistrationDTO.getPhoneNumber()).build()).accountNumber(uuid.toString()).minimumBalance(bankAccountRegistrationDTO.getMinimumBalance()).build();
            return bankAccountUni.onItem().invoke(bankAccounts -> {
                bankAccounts.add(savingsBankAccount);
            }).onItem().transformToUni(bankAccounts -> Uni.createFrom().item(bankAccounts.get(bankAccounts.size() - 1)));
        }
        return null;
    }

    public Uni<BankAccount> getBankAccountByAccountNumber(String accountNumber) {
        return bankAccountUni.onItem().transform(bankAccounts -> bankAccounts.stream().filter(bankAccount -> bankAccount.getAccountNumber().equals(accountNumber)).findFirst().orElse(null));
    }

    public Uni<String> deleteBankAccountByAccountNumber(String accountNumber) {
        return bankAccountUni.onItem().transform(bankAccounts -> removeBankAccount(bankAccounts, accountNumber));
    }

    private String removeBankAccount(List<BankAccount> bankAccounts, String accountNumber) {
        return bankAccounts.removeIf(bankAccount -> bankAccount.getAccountNumber().equals(accountNumber)) ? "Bank account deleted successfully" : "Bank account not found";
    }

    public Uni<SavingsBankAccount> updateSavingsBankAccount(String accountNumber, SavingsBankAccount bankAccountCloud) {

        return bankAccountMulti.filter(bankAccount -> bankAccount.getAccountType().equals(AccountType.SAVINGS)).onItem().transform(bankAccount -> (SavingsBankAccount) bankAccount).filter(bankAccount -> bankAccount.getAccountNumber().equals(accountNumber)).onItem().transform(bankAccount -> {
            if (bankAccount == null) {
                return null;
            }
            bankAccount.setAccountHolder(bankAccountCloud.getAccountHolder() == null ? bankAccount.getAccountHolder() : bankAccountCloud.getAccountHolder());
            bankAccount.setAccountNumber(bankAccountCloud.getAccountNumber() == null ? bankAccount.getAccountNumber() : bankAccountCloud.getAccountNumber());
            bankAccount.setBalanceAmount(bankAccountCloud.getBalanceAmount() == 0.0 ? bankAccount.getBalanceAmount() : bankAccountCloud.getBalanceAmount());
            bankAccount.setPausedMonthlyBillingsUntil(bankAccountCloud.getPausedMonthlyBillingsUntil() == null ? bankAccount.getPausedMonthlyBillingsUntil() : bankAccountCloud.getPausedMonthlyBillingsUntil());
            bankAccount.setMinimumBalance(bankAccountCloud.getMinimumBalance() == 0.0 ? bankAccount.getMinimumBalance() : bankAccountCloud.getMinimumBalance());
            return bankAccount;
        }).toUni();

    }

    public Uni<List<BankAccount>> getUniTopLimitPayingAccountsByType(AccountType accountType, int limit, OrderType orderType) {
        Map<BankAccount, Double> bankAccountToTotalAmountBilled = new HashMap<>();
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

    public Uni<List<BankAccount>> getBankAccounts(FilterParameters filterParameters) {
        FilterParameters validatedFilterParameters = validate(filterParameters);
        String orderBy = validatedFilterParameters.orderBy;
        String orderType = validatedFilterParameters.orderType;
        int limit = Integer.parseInt(validatedFilterParameters.limit);
        AccountType accountType = AccountType.valueOf(validatedFilterParameters.accountType);
        if (orderBy.equals("income")) {
            return getUniTopLimitPayingAccountsByType(accountType, limit, orderType.equals("ASC") ? OrderType.ASC : OrderType.DESC);
        }
        return bankAccountMulti.filter(bankAccount -> bankAccount.getAccountType().equals(accountType)).onItem().transformToUniAndMerge(bankAccount -> Uni.createFrom().item(bankAccount)).collect().asList().onItem().transformToUni(list -> Uni.createFrom().item(list.stream().sorted((bankAccount1, bankAccount2) -> switch (orderBy) {
            case "balanceAmount" ->
                    orderType.equals("ASC") ? Double.compare(bankAccount1.getBalanceAmount(), bankAccount2.getBalanceAmount()) : Double.compare(bankAccount2.getBalanceAmount(), bankAccount1.getBalanceAmount());
            case "accountNumber" ->
                    orderType.equals("ASC") ? bankAccount1.getAccountNumber().compareTo(bankAccount2.getAccountNumber()) : bankAccount2.getAccountNumber().compareTo(bankAccount1.getAccountNumber());
            default ->
                    orderType.equals("ASC") ? bankAccount1.getAccountHolder().getFirstName().compareTo(bankAccount2.getAccountHolder().getFirstName()) : bankAccount2.getAccountHolder().getFirstName().compareTo(bankAccount1.getAccountHolder().getFirstName());
        }).limit(limit).toList()));
    }

}
