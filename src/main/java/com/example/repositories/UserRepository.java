package com.example.repositories;

import com.example.inMemoryDBs.DB;
import com.example.users.BankUser;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.UniEmitter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.function.Consumer;

@ApplicationScoped
public class UserRepository {

    public Uni<List<BankUser>> getUsers() {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super List<BankUser>>>) em -> {
                    try {
                        em.complete(DB.bankUsers);
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    public Uni<BankUser> getUser(Long id) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super BankUser>>) em -> {
                    try {
                        BankUser user = DB.bankUsers.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
                        if (user != null) {
                            em.complete(user);
                        } else em.fail(new Exception("User not found"));
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor());
    }

    public Uni<BankUser> addBankUser(BankUser bankUser) {

        return Uni.createFrom().emitter((Consumer<UniEmitter<? super List<BankUser>>>) em -> {
                    try {
                        bankUser.setId((long) (DB.bankUsers.size() + 1));
                        DB.bankUsers.add(bankUser);
                        em.complete(DB.bankUsers);
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor())
                .onItem().transformToUni(list -> getUser(bankUser.getId()));
    }

    public Uni<String> deleteBankUser(Long id) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super List<BankUser>>>) em -> {
                    try {
                        BankUser user = DB.bankUsers.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
                        if (user != null) {
                            DB.bankUsers.remove(user);
                            em.complete(DB.bankUsers);
                        } else em.fail(new Exception("User not found"));
                    } catch (Exception e) {
                        em.fail(e);
                    }
                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor())
                .onItem().transformToUni(list -> Uni.createFrom().item("User deleted successfully"));
    }

    public Uni<BankUser> updateBankUser(Long id, BankUser bankUserCloud) {
        return Uni.createFrom().emitter((Consumer<UniEmitter<? super List<BankUser>>>) em -> {
                    try {
                        BankUser user = DB.bankUsers.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
                        if (user != null) {
                            user.setFirstName(bankUserCloud.getFirstName());
                            user.setLastName(bankUserCloud.getLastName());
                            user.setEmail(bankUserCloud.getEmail());
                            user.setPhoneNumber(bankUserCloud.getPhoneNumber());
                            em.complete(DB.bankUsers);
                        } else em.fail(new Exception("User not found"));
                    } catch (Exception e) {
                        em.fail(e);
                    }

                }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .emitOn(Infrastructure.getDefaultExecutor())
                .onItem().transformToUni(list -> getUser(id));
    }
}
