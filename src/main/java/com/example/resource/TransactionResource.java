package com.example.resource;

import com.example.transactions.Transaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.example.services.TransactionService;
import com.example.utils.CustomResponse;

import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @Inject
    TransactionService transactionService;

    @GET
    public Uni<List<Transaction>> getTransactions() {
        return transactionService.getTransactions();
    }

}
