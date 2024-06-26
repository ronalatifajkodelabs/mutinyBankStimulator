package com.example.accounts;

import com.example.adapters.JsonSubtype;
import com.example.adapters.JsonType;
import com.example.enums.AccountType;
import com.example.users.BankAccountHolder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.example.inMemoryDBs.DB.bankAccounts;

@Getter
@Setter
@JsonType(
        property = "accountType",
        subtypes = {
                @JsonSubtype(clazz = CheckingBankAccount.class, name = "CHECKING"),
                @JsonSubtype(clazz = SavingsBankAccount.class, name = "SAVINGS")
        }
)
public abstract class BankAccount {

    private String accountNumber;
    private BankAccountHolder accountHolder;
    private double balanceAmount;
    private AccountType accountType;
    private LocalDateTime pausedMonthlyBillingsUntil;

    public BankAccount() {
    }

    public BankAccount(String accountNumber, BankAccountHolder accountHolder, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balanceAmount = 0.0;
        this.accountType = accountType;
    }

    public BankAccount(String accountNumber, BankAccountHolder accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
    }

    public static BankAccount getBankAccount(String accountNumber) {
        for (BankAccount bankAccount : bankAccounts){
            if (bankAccount.getAccountNumber().equals(accountNumber)) {
                return bankAccount;
            }
        }
        return null;
    }

    public String toString() {
        return "\nAccount Number: " + accountNumber +
                "\nAccount Holder: " + accountHolder.getFirstName() + " " + accountHolder.getLastName() +
                "\nAccount Balance: " + balanceAmount;
    }

    public boolean equals(Object obj) {
        if (obj instanceof BankAccount bankAccount) {
            return this.accountNumber.equals(bankAccount.accountNumber);
        }
        return false;
    }

    public abstract void runMonthlyUpdate();
}
