package com.example.repositories;

import com.example.inMemoryDBs.DB;
import com.example.users.BankAccountHolder;
import com.example.users.BankEmployee;
import com.example.users.BankOwner;
import com.example.users.BankUser;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserRepository {

    Uni<List<BankUser>> bankUserUni = Uni.createFrom().item(DB.bankUsers);

    public Uni<List<BankUser>> getUsers() {
        return bankUserUni;
    }

    public Uni<BankUser> getUser(Long id) {
        return bankUserUni.onItem().transform(users -> users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null));
    }

    public Uni<List<BankUser>> getAccountHolders() {
        return bankUserUni.onItem().transform(users -> users.stream().filter(user -> user instanceof BankAccountHolder).toList());
    }

    public Uni<List<BankUser>> getBankEmployees() {
        return bankUserUni.onItem().transform(users -> users.stream().filter(user -> user instanceof BankEmployee).toList());
    }

    public Uni<List<BankUser>> getBankOwners() {
        return bankUserUni.onItem().transform(users -> users.stream().filter(user -> user instanceof BankOwner).toList());
    }

}
