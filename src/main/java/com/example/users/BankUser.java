package com.example.users;

import com.example.enums.UserType;
import io.quarkus.arc.impl.Identified;
import lombok.Data;

@Data
public class BankUser {

    Long id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    UserType userType;

    public BankUser() {
    }

    public BankUser(Long id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
