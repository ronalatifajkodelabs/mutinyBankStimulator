package com.example.services;

import com.example.inMemoryDBs.DB;
import com.example.repositories.UserRepository;
import com.example.users.BankEmployee;
import com.example.users.BankUser;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.UniEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.function.Consumer;

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

    public Uni<String> deleteUser(Long id) {
        return userRepository.getUsers().onItem().transform(users -> {
            if (removeUser(users, id)) {
                return "User deleted successfully";
            } else return "User not found";
        });
    }

    private boolean removeUser(List<BankUser> users, Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }

    public Uni<BankUser> updateUser(Long id, BankUser bankUserCloud) {

        return userRepository.getUser(id).onItem().transform(u -> {
            if (u != null) {
                u.setFirstName(bankUserCloud.getFirstName() == null ? u.getFirstName() : bankUserCloud.getFirstName());
                u.setLastName(bankUserCloud.getLastName() == null ? u.getLastName() : bankUserCloud.getLastName());
                u.setEmail(bankUserCloud.getEmail() == null ? u.getEmail() : bankUserCloud.getEmail());
                u.setPhoneNumber(bankUserCloud.getPhoneNumber() == null ? u.getPhoneNumber() : bankUserCloud.getPhoneNumber());
                return u;
            } else return null;
        });
    }

    public Uni<List<BankUser>> addBankUser(BankUser bankUser) {
        bankUser.setId((long) (DB.bankUsers.size() + 1));
        return userRepository.getUsers().onItem().transform(users -> {
            users.add(bankUser);
            return users;
        });
    }

//      return Uni.createFrom().emitter(new Consumer<UniEmitter<? super Object>>() {
//            @Override
//            public void accept(UniEmitter<? super Object> uniEmitter) {
//                try {
//                    userRepository.getUsers().onItem().transform(users -> {
//                        users.add(bankUser);
//                        return users;
//                    });
//                    uniEmitter.complete();
//                }
//                catch ( Exception e){
//                }
//
//            }
//        }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
//              .emitOn(Infrastructure.getDefaultExecutor());
//        return userRepository.getUsers().onItem().transform(users -> {
//            users.add(bankUser);
//            return users;
//        });
//    }

//    public Uni<BankUser> addBankUser(BankUser bankUser) {
//        return userRepository.getUsers().onItem().invoke(users -> {
//            users.add(bankUser);
//        }).onItem().transformToUni(users -> Uni.createFrom().item(users.get(users.size() - 1)));
//    }

}
