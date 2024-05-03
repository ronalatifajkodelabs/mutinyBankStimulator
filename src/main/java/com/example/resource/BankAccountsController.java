package com.example.resource;

import com.example.DTOs.BankAccountInformationDTO;
import com.example.DTOs.FilterParameters;
import com.example.accounts.BankAccount;
import com.example.services.BankAccountService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/bank-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BankAccountsController {

    @Inject
    BankAccountService bankAccountsService;

    //    GET /bank-accounts?orderBy=...&orderType=...&limit=...&accountType=...
    @GET
    public Uni<List<BankAccount>> getBankAccounts(@BeanParam FilterParameters filterParameters) {
        return bankAccountsService.getBankAccounts(filterParameters);
    }

    @GET
    @Path("/{accountNumber}")
    public Uni<BankAccount> getBankAccounts(@PathParam("accountNumber") String accountNumber) {
        return bankAccountsService.getBankAccount(accountNumber);
    }

    @POST
    public Uni<BankAccount> addBankAccount(BankAccountInformationDTO bankAccountRegistrationDTO) {
        return bankAccountsService.addBankAccount(bankAccountRegistrationDTO);
    }

    @DELETE
    @Path("/{accountNumber}")
    public Uni<String> deleteBankAccount(@PathParam("accountNumber") String accountNumber) {
        return bankAccountsService.deleteBankAccount(accountNumber);
    }

    @PUT
    @Path("/{accountNumber}")
    public Uni<BankAccount> updateBankAccount(@PathParam("accountNumber") String accountNumber, BankAccountInformationDTO bankAccountRegistrationDTO) {
        return bankAccountsService.updateBankAccount(accountNumber, bankAccountRegistrationDTO);
    }

}
