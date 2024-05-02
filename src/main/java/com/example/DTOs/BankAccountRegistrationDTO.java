package com.example.DTOs;

import lombok.Data;
import com.example.enums.AccountType;

import java.time.LocalDateTime;

@Data
public class BankAccountRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AccountType accountType;
    private LocalDateTime pausedMonthlyBillingsUntil;
    private double minimumBalance;
}
