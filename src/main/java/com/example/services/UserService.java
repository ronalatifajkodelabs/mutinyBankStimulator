package com.example.services;

import com.example.inMemoryDBs.DB;
import com.example.users.BankAccountHolder;
import com.example.users.BankEmployee;
import com.example.users.BankOwner;
import com.example.users.BankUser;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserService {

    //    map -> onItem().transform()
    //    .onItem().transform(i -> i + 1)
    //    .onItem().transformToUni(i -> Uni.createFrom().item(i + 1))
//    flatMap -> onItem().transformToUniAndMerge and onItem().transformToMultiAndMerge
//    concatMap -> onItem().transformToUniAndConcatenate and onItem().transformToMultiAndConcatenate
    // concatenate preserves the order of items while merge uses them as they come - concurrently

    Multi<BankUser> bankUserMulti = Multi.createFrom().iterable(DB.bankUsers); //vjen prej DB si multi
    Uni<List<BankUser>> bankUserUni = Uni.createFrom().item(DB.bankUsers);

    public Uni<List<BankUser>> getUsers() {
        return bankUserUni;
    }

    public Uni<BankUser> getUser(String firstName, String lastName) {
        return bankUserUni.onItem().transform(users -> users.stream().filter(user -> (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))).findFirst().orElse(null));
    }

    public Uni<String> deleteUser(String firstName, String lastName) {
        return bankUserUni.onItem().transform(users -> {
            if (removeUser(users, firstName, lastName)) {
                return "User deleted successfully";
            } else return "User not found";
        });
    }

    private boolean removeUser(List<BankUser> users, String firstName, String lastName) {
        return users.removeIf(user -> user.getFirstName().equals(firstName) && user.getLastName().equals(lastName));
    }

    public Uni<BankUser> updateUser(String firstName, String lastName, BankUser bankUserCloud) {

        return bankUserMulti.onItem().invoke(user -> System.out.println(user.getFirstName())).filter(u -> u.getFirstName().equals(firstName) && u.getLastName().equals(lastName)).onItem().transform(u -> {
            if (u != null) {
                u.setFirstName(bankUserCloud.getFirstName() == null ? u.getFirstName() : bankUserCloud.getFirstName());
                u.setLastName(bankUserCloud.getLastName() == null ? u.getLastName() : bankUserCloud.getLastName());
                u.setEmail(bankUserCloud.getEmail() == null ? u.getEmail() : bankUserCloud.getEmail());
                u.setPhoneNumber(bankUserCloud.getPhoneNumber() == null ? u.getPhoneNumber() : bankUserCloud.getPhoneNumber());
                return u;
            } else return null;
        }).toUni();
    }

    public Uni<BankUser> addBankAccountHolder(BankAccountHolder bankUser) {
        return bankUserUni.onItem().invoke(users -> {
            users.add(bankUser);
        }).onItem().transformToUni(users -> Uni.createFrom().item(users.get(users.size() - 1)));
    }

    public Uni<List<BankUser>> addBankEmployee(BankEmployee bankUser) {
        return bankUserUni.onItem().transform(users -> {
            users.add(bankUser);
            return users;
        });
    }

    public Uni<List<BankUser>> addBankOwner(BankOwner bankUser) {
        return bankUserUni.onItem().transform(users -> {
            users.add(bankUser);
            return users;
        });
    }
}
