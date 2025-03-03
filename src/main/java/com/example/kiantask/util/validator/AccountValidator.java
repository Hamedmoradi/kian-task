package com.example.kiantask.util.validator;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.*;

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

    public static void isSourceAndDestinationAreTheSame(String sourceAccount, String destinationAccount) {
        if (sourceAccount.equals(destinationAccount)) {
            throw new SourceAndDestinationAccountAreTheSameException();
        }
    }
}
