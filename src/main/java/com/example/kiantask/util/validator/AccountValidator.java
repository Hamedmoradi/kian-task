package com.example.kiantask.util.validator;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountHolderIsNotNullOrEmptyException;
import com.example.kiantask.exceptionHandler.AccountNumberIsNotNullOrEmptyException;
import com.example.kiantask.exceptionHandler.InsufficientFundsException;
import com.example.kiantask.exceptionHandler.TransactionAmountMustBePositiveException;

public class AccountValidator {
    public static void validateAccountDetail(String accountNumber, String accountHolderName) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new AccountNumberIsNotNullOrEmptyException();
        }
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new AccountHolderIsNotNullOrEmptyException();
        }
    }

    public static void checkAmount(double amount) {
        if (amount <= 0) {
            throw new TransactionAmountMustBePositiveException();
        }
    }

    public static void checkBalance(double amount, BankAccount account) {
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException();
        }
    }
}
