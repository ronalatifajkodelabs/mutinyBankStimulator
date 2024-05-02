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
    @Path("/{firstName}/{lastName}")
    public Uni<BankUser> getUser(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        return userService.getUser(firstName, lastName);
    }

    @POST
    @Path("/bank-account-holder")
    public Uni<BankUser> addBankAccountHolder(BankAccountHolder bankUser) {
        return userService.addBankAccountHolder(bankUser);
    }

    @POST
    @Path("/bank-employee")
    public Uni<List<BankUser>> addBankEmployee(BankEmployee bankUser) {
        return userService.addBankEmployee(bankUser);
    }

    @POST
    @Path("/bank-owner")
    public Uni<List<BankUser>> addBankOwner(BankOwner bankUser) {
        return userService.addBankOwner(bankUser);
    }

    @DELETE
    @Path("/{firstName}/{lastName}")
    public Uni<String> deleteUser(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        return userService.deleteUser(firstName, lastName);
    }

    @PUT
    public Uni<BankUser> updateUser(@RestQuery String firstName, @RestQuery String lastName, BankAccountHolder bankUserCloud) {
        return userService.updateUser(firstName, lastName, bankUserCloud);
    }

    //    @GET
    //    public BankUser getUser(UserIdentificationDTO userIdentificationDTO) {
    //        return userService.getUser(userIdentificationDTO.getFirstName(), userIdentificationDTO.getFirstName());
    //    }

}
