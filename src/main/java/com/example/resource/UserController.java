package com.example.resource;

import com.example.users.BankUser;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;
import com.example.services.UserService;
import com.example.users.BankAccountHolder;
import com.example.users.BankEmployee;
import com.example.users.BankOwner;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @GET
    public Uni<List<BankUser>> getUsers() {
        return userService.getUsers();
    }

    @GET
    @Path("/{id}")
    public Uni<BankUser> getUser(@PathParam("id") Long id) {
        return userService.getUser(id);
    }

    @POST
    public Uni<List<BankUser>> addBankOwner(BankUser bankUser) {
        return userService.addBankUser(bankUser);
    }

    @DELETE
    @Path("/{id}")
    public Uni<String> deleteUser(@PathParam("id") Long id) {
        return userService.deleteUser(id);
    }

    @PUT
    @Path("/{id}")
    public Uni<BankUser> updateUser(@PathParam("id") Long id, BankAccountHolder bankUserCloud) {
        return userService.updateUser(id, bankUserCloud);
    }

}
