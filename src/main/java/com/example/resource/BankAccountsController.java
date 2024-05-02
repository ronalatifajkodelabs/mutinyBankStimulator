package com.example.resource;

import com.example.accounts.BankAccount;
import com.example.enums.AccountType;
import com.example.enums.OrderType;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import com.example.DTOs.BankAccountRegistrationDTO;
import com.example.DTOs.FilterParameters;
import com.example.accounts.SavingsBankAccount;
import com.example.services.BankAccountService;
import com.example.utils.CustomResponse;

import java.util.List;

@Path("/bank-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BankAccountsController {

    @Inject
    BankAccountService bankAccountsService;

    @GET
    public Uni<List<BankAccount>> getBankAccounts() {
        return bankAccountsService.getBankAccounts();
    }

    @GET
    @Path("/{accountNumber}")
    public Uni<BankAccount> getBankAccounts(@PathParam("accountNumber") String accountNumber) {
        return bankAccountsService.getBankAccountByAccountNumber(accountNumber);
    }

    @POST
    public Uni<BankAccount> addBankAccount(BankAccountRegistrationDTO bankAccountRegistrationDTO) {
        return bankAccountsService.addBankAccount(bankAccountRegistrationDTO);
    }

    @DELETE
    @Path("/{accountNumber}")
    public Uni<String> deleteBankAccount(@PathParam("accountNumber") String accountNumber) {
        return bankAccountsService.deleteBankAccountByAccountNumber(accountNumber);
    }

    @PUT
    @Path("/{accountNumber}")
    public Uni<SavingsBankAccount> updateSavingsBankAccount(@PathParam("accountNumber") String accountNumber, SavingsBankAccount bankAccountCloud) {
        return bankAccountsService.updateSavingsBankAccount(accountNumber, bankAccountCloud);
    }

    //    GET /bank-accounts?orderBy=...&orderType=...&limit=...&accountType=...
    @GET
    @Path("/filters")
    public Uni<List<BankAccount>> getBankAccounts(@BeanParam FilterParameters filterParameters) {
        return bankAccountsService.getBankAccounts(filterParameters);
    }

}
