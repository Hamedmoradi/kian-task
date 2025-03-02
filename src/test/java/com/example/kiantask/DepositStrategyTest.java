package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.pattern.strategy.impl.DepositStrategy;
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

class DepositStrategyTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private DepositStrategy depositStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteSuccessfulDeposit() {
        String accountNumber = "12345";
        double initialBalance = 1000.0;
        double depositAmount = 500.0;
        BankAccount account = new BankAccount(accountNumber, "Ali", initialBalance);

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(repository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        depositStrategy.execute(repository, accountNumber, depositAmount, null);

        assertEquals(1500.0, account.getBalance(), 0.01, "Balance should increase by deposit amount");
        verify(repository, times(1)).findByAccountNumber(accountNumber);
        verify(repository, times(1)).save(account);
    }

    @Test
    void testExecuteAccountNotFound() {
        String accountNumber = "99999";
        double depositAmount = 200.0;

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> depositStrategy.execute(repository, accountNumber, depositAmount, null),
                "Should throw AccountNotFoundException for non-existent account");

        verify(repository, times(1)).findByAccountNumber(accountNumber);
        verify(repository, never()).save(any(BankAccount.class));
    }

    @Test
    void testExecuteTargetAccountNumberIgnored() {
        String accountNumber = "67890";
        String targetAccountNumber = "11111";
        double initialBalance = 300.0;
        double depositAmount = 100.0;
        BankAccount account = new BankAccount(accountNumber, "Babak", initialBalance);

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(repository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        depositStrategy.execute(repository, accountNumber, depositAmount, targetAccountNumber);

        assertEquals(400.0, account.getBalance(), 0.01, "Balance should increase by deposit amount");
        verify(repository, times(1)).findByAccountNumber(accountNumber);
        verify(repository, times(1)).save(account);
        verifyNoMoreInteractions(repository);
    }
}