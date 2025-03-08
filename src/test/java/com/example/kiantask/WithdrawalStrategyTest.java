package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.exceptionHandler.InsufficientFundsException;
import com.example.kiantask.pattern.strategy.impl.WithdrawalStrategy;
import com.example.kiantask.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class WithdrawalStrategyTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private WithdrawalStrategy withdrawalStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteSuccessfulWithdrawal() {
        String accountNumber = "12345";
        double initialBalance = 1000.0;
        double withdrawalAmount = 300.0;
        BankAccount account = new BankAccount(accountNumber, "Ali", initialBalance);

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(repository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        withdrawalStrategy.execute(repository, accountNumber, withdrawalAmount, null);

        assertEquals(700.0, account.getBalance(), 0.01, "Balance should decrease by withdrawal amount");
        verify(repository, times(1)).findByAccountNumber(accountNumber);
        verify(repository, times(1)).save(account);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testExecuteAccountNotFound() {
        String accountNumber = "99999";
        double withdrawalAmount = 200.0;

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> withdrawalStrategy.execute(repository, accountNumber, withdrawalAmount, null),
                "Should throw AccountNotFoundException for non-existent account");

        verify(repository, times(1)).findByAccountNumber(accountNumber);
        verify(repository, never()).save(any(BankAccount.class));
    }

    @Test
    void testExecuteInsufficientFunds() {
        String accountNumber = "12345";
        double initialBalance = 100.0;
        double withdrawalAmount = 200.0;
        BankAccount account = new BankAccount(accountNumber, "Ali", initialBalance);

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class,
                () -> withdrawalStrategy.execute(repository, accountNumber, withdrawalAmount, null),
                "Should throw InsufficientFundsException for insufficient funds via checkBalance");

        verify(repository, times(1)).findByAccountNumber(accountNumber);
        verify(repository, never()).save(any(BankAccount.class));
        assertEquals(100.0, account.getBalance(), 0.01, "Balance should remain unchanged");
    }

    @Test
    void testExecuteTargetAccountNumberIgnored() {
        String accountNumber = "12345";
        String targetAccountNumber = "67890";
        double initialBalance = 1000.0;
        double withdrawalAmount = 400.0;
        BankAccount account = new BankAccount(accountNumber, "Ali", initialBalance);

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(repository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        withdrawalStrategy.execute(repository, accountNumber, withdrawalAmount, targetAccountNumber);

        assertEquals(600.0, account.getBalance(), 0.01, "Balance should decrease by withdrawal amount");
        verify(repository, times(1)).findByAccountNumber(accountNumber);
        verify(repository, times(1)).save(account);
        verify(repository, never()).findByAccountNumber(targetAccountNumber);
    }
}