package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountHolderIsNotNullOrEmptyException;
import com.example.kiantask.exceptionHandler.AccountNumberIsNotNullOrEmptyException;
import com.example.kiantask.exceptionHandler.InsufficientFundsException;
import com.example.kiantask.exceptionHandler.TransactionAmountMustBePositiveException;
import com.example.kiantask.util.validator.AccountValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AccountValidatorTest {

    @Test
    void testValidateAccountDetailValidInput() {

        String accountNumber = "12345";
        String accountHolderName = "Hamed";
        assertDoesNotThrow(() -> AccountValidator.validateAccountDetail(accountNumber, accountHolderName),
                "Valid account number and holder name should not throw an exception");
    }

    @Test
    void testValidateAccountDetailNullAccountNumber() {

        String accountHolderName = "Hamed";
        assertThrows(AccountNumberIsNotNullOrEmptyException.class,
                () -> AccountValidator.validateAccountDetail(null, accountHolderName),
                "Null account number should throw AccountNumberIsNotNullOrEmptyException");
    }

    @Test
    void testValidateAccountDetailEmptyAccountNumber() {

        String accountNumber = "";
        String accountHolderName = "Hamed";
        assertThrows(AccountNumberIsNotNullOrEmptyException.class,
                () -> AccountValidator.validateAccountDetail(accountNumber, accountHolderName),
                "Empty account number should throw AccountNumberIsNotNullOrEmptyException");
    }

    @Test
    void testValidateAccountDetailWhitespaceAccountNumber() {
        String accountNumber = "   ";
        String accountHolderName = "Hamed";

        assertThrows(AccountNumberIsNotNullOrEmptyException.class,
                () -> AccountValidator.validateAccountDetail(accountNumber, accountHolderName),
                "Whitespace-only account number should throw AccountNumberIsNotNullOrEmptyException");
    }

    @Test
    void testValidateAccountDetailNullAccountHolderName() {
        String accountNumber = "12345";
        assertThrows(AccountHolderIsNotNullOrEmptyException.class,
                () -> AccountValidator.validateAccountDetail(accountNumber, null),
                "Null account holder name should throw AccountHolderIsNotNullOrEmptyException");
    }

    @Test
    void testValidateAccountDetailEmptyAccountHolderName() {
        String accountNumber = "12345";
        String accountHolderName = "";
        assertThrows(AccountHolderIsNotNullOrEmptyException.class,
                () -> AccountValidator.validateAccountDetail(accountNumber, accountHolderName),
                "Empty account holder name should throw AccountHolderIsNotNullOrEmptyException");
    }

    @Test
    void testValidateAccountDetailWhitespaceAccountHolderName() {
        String accountNumber = "12345";
        String accountHolderName = "   ";
        assertThrows(AccountHolderIsNotNullOrEmptyException.class,
                () -> AccountValidator.validateAccountDetail(accountNumber, accountHolderName),
                "Whitespace-only account holder name should throw AccountHolderIsNotNullOrEmptyException");
    }

    @Test
    void testCheckAmountPositive() {
        double amount = 100.0;
        assertDoesNotThrow(() -> AccountValidator.checkAmount(amount),
                "Positive amount should not throw an exception");
    }

    @Test
    void testCheckAmountZero() {
        double amount = 0.0;
        assertThrows(TransactionAmountMustBePositiveException.class,
                () -> AccountValidator.checkAmount(amount),
                "Zero amount should throw TransactionAmountMustBePositiveException");
    }

    @Test
    void testCheckAmountNegative() {
        double amount = -50.0;
        assertThrows(TransactionAmountMustBePositiveException.class,
                () -> AccountValidator.checkAmount(amount),
                "Negative amount should throw TransactionAmountMustBePositiveException");
    }

    @Test
    void testCheckBalanceSufficientFunds() {
        double amount = 200.0;
        BankAccount account = Mockito.mock(BankAccount.class);
        when(account.getBalance()).thenReturn(1000.0);
        assertDoesNotThrow(() -> AccountValidator.checkBalance(amount, account),
                "Sufficient funds should not throw an exception");
    }

    @Test
    void testCheckBalanceExactFunds() {
        double amount = 500.0;
        BankAccount account = Mockito.mock(BankAccount.class);
        when(account.getBalance()).thenReturn(500.0);
        assertDoesNotThrow(() -> AccountValidator.checkBalance(amount, account),
                "Exact funds should not throw an exception");
    }

    @Test
    void testCheckBalanceInsufficientFunds() {
        double amount = 600.0;
        BankAccount account = Mockito.mock(BankAccount.class);
        when(account.getBalance()).thenReturn(500.0);
        assertThrows(InsufficientFundsException.class,
                () -> AccountValidator.checkBalance(amount, account),
                "Insufficient funds should throw InsufficientFundsException");
    }

    @Test
    void testCheckBalanceZeroAmount() {
        double amount = 0.0;
        BankAccount account = Mockito.mock(BankAccount.class);
        when(account.getBalance()).thenReturn(500.0);

        assertDoesNotThrow(() -> AccountValidator.checkBalance(amount, account), "Zero amount should not throw an exception in checkBalance (only checks funds)");
    }

    @Test
    void testCheckBalanceNegativeAmount() {
        double amount = -50.0;
        BankAccount account = Mockito.mock(BankAccount.class);
        when(account.getBalance()).thenReturn(500.0);

        assertDoesNotThrow(() -> AccountValidator.checkBalance(amount, account), "Negative amount should not throw an exception in checkBalance (only checks funds)");
    }
}