package com.example.services;

import com.example.DTOs.BankAccountInformationDTO;
import com.example.DTOs.FilterParameters;
import com.example.accounts.BankAccount;
import com.example.enums.AccountType;
import com.example.repositories.BankAccountRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static com.example.utils.Util.validate;

@ApplicationScoped
public class BankAccountService {

    @Inject
    BankAccountRepository bankAccountRepository;

    public Uni<BankAccount> getBankAccount(String id) {
        return bankAccountRepository.getBankAccount(id);
    }

    public Uni<BankAccount> addBankAccount(BankAccountInformationDTO bankAccountRegistrationDTO) {
        return bankAccountRepository.addBankAccount(bankAccountRegistrationDTO);
    }

    public Uni<String> deleteBankAccount(String accountNumber) {
        return bankAccountRepository.deleteBankAccount(accountNumber);
    }

    public Uni<BankAccount> updateBankAccount(String accountNumber, BankAccountInformationDTO bankAccountRegistrationDTO) {
        return bankAccountRepository.updateBankAccount(accountNumber, bankAccountRegistrationDTO);
    }

    public Uni<List<BankAccount>> getBankAccounts(FilterParameters filterParameters) {
        if (filterParameters.accountType == null && filterParameters.limit == null && filterParameters.orderBy == null && filterParameters.orderType == null) {
            return bankAccountRepository.getBankAccounts();
        }
        FilterParameters validatedFilterParameters = validate(filterParameters);
        String orderBy = validatedFilterParameters.orderBy;
        String orderType = validatedFilterParameters.orderType;
        int limit = Integer.parseInt(validatedFilterParameters.limit);
        AccountType accountType = AccountType.valueOf(validatedFilterParameters.accountType);
        return bankAccountRepository.getBankAccountsWithFilters(orderBy, orderType, limit, accountType);
    }

}
