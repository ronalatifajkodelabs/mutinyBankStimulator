package com.example.utils;

import com.example.DTOs.FilterParameters;
import com.example.enums.AccountType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    public static double formatToTwoDecimals(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    public static LocalDateTime createLocalDateTimeFromString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }

    public static FilterParameters validate(FilterParameters filterParameters) {
        String orderBy = filterParameters.orderBy;
        String orderType = filterParameters.orderType;
        try {
            int limit = Integer.parseInt(filterParameters.limit);
            filterParameters.limit = limit + "";
        } catch (NumberFormatException e) {
            filterParameters.limit = 10 + "";
        }
        try {
            AccountType accountType = AccountType.valueOf(filterParameters.accountType);
            filterParameters.accountType = accountType.toString();
        } catch (IllegalArgumentException e) {
            filterParameters.accountType = AccountType.SAVINGS.toString();
        }
        if (!(orderBy.equals("balanceAmount") || orderBy.equals("accountNumber") || orderBy.equals("income"))) {
            filterParameters.orderBy = "accountHolder";
        }
        if (!(orderType.equals("ASC") || orderType.equals("DESC"))) {
            filterParameters.orderType = "ASC";
        }
        return filterParameters;
    }

    //    map -> onItem().transform()
    //    .onItem().transform(i -> i + 1)
    //    .onItem().transformToUni(i -> Uni.createFrom().item(i + 1))
    //    flatMap -> onItem().transformToUniAndMerge and onItem().transformToMultiAndMerge
    //    concatMap -> onItem().transformToUniAndConcatenate and onItem().transformToMultiAndConcatenate
    // concatenate preserves the order of items while merge uses them as they come - concurrently

}
