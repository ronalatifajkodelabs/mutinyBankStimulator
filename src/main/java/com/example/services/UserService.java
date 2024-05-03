package com.example.services;

import com.example.repositories.UserRepository;
import com.example.users.BankUser;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public Uni<List<BankUser>> getUsers() {
        return userRepository.getUsers();
    }

    public Uni<BankUser> getUser(Long id) {
        return userRepository.getUser(id);
    }

    public Uni<BankUser> updateUser(Long id, BankUser bankUserCloud) {
        return userRepository.updateBankUser(id, bankUserCloud);
    }

    public Uni<BankUser> addBankUser(BankUser bankUser) {
        return userRepository.addBankUser(bankUser);
    }

    public Uni<String> deleteBankUser(Long id) {
        return userRepository.deleteBankUser(id);
    }

}
