package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.exceptionHandler.DestinationAccountNotFoundException;
import com.example.kiantask.exceptionHandler.InsufficientFundsException;
import com.example.kiantask.pattern.strategy.impl.TransferStrategy;
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

class TransferStrategyTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private TransferStrategy transferStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteSuccessfulTransfer() {
        String fromAccountNumber = "12345";
        String toAccountNumber = "67890";
        double amount = 300.0;
        BankAccount sourceAccount = new BankAccount(fromAccountNumber, "Ali", 1000.0);
        BankAccount destinationAccount = new BankAccount(toAccountNumber, "Babak", 500.0);

        when(repository.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.of(sourceAccount));
        when(repository.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(destinationAccount));
        when(repository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transferStrategy.execute(repository, fromAccountNumber, amount, toAccountNumber);

        assertEquals(700.0, sourceAccount.getBalance(), 0.01, "Source balance should decrease");
        assertEquals(800.0, destinationAccount.getBalance(), 0.01, "Destination balance should increase");
        verify(repository, times(1)).findByAccountNumber(fromAccountNumber);
        verify(repository, times(1)).findByAccountNumber(toAccountNumber);
        verify(repository, times(1)).save(sourceAccount);
        verify(repository, times(1)).save(destinationAccount);
    }

    @Test
    void testExecuteSourceAccountNotFound() {
        String fromAccountNumber = "99999";
        String toAccountNumber = "67890";
        double amount = 200.0;

        when(repository.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> transferStrategy.execute(repository, fromAccountNumber, amount, toAccountNumber),
                "Should throw AccountNotFoundException for non-existent source");

        verify(repository, times(1)).findByAccountNumber(fromAccountNumber);
        verify(repository, never()).findByAccountNumber(toAccountNumber);
        verify(repository, never()).save(any(BankAccount.class));
    }

    @Test
    void testExecuteDestinationAccountNotFound() {
        String fromAccountNumber = "12345";
        String toAccountNumber = "99999";
        double amount = 200.0;
        BankAccount sourceAccount = new BankAccount(fromAccountNumber, "Ali", 1000.0);

        when(repository.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.of(sourceAccount));
        when(repository.findByAccountNumber(toAccountNumber)).thenReturn(Optional.empty());

        assertThrows(DestinationAccountNotFoundException.class,
                () -> transferStrategy.execute(repository, fromAccountNumber, amount, toAccountNumber),
                "Should throw DestinationAccountNotFoundException for non-existent destination");

        verify(repository, times(1)).findByAccountNumber(fromAccountNumber);
        verify(repository, times(1)).findByAccountNumber(toAccountNumber);
        verify(repository, never()).save(any(BankAccount.class));
    }

    @Test
    void testExecuteInsufficientFunds() {
        String fromAccountNumber = "12345";
        String toAccountNumber = "67890";
        double amount = 1500.0;
        BankAccount sourceAccount = new BankAccount(fromAccountNumber, "Ali", 1000.0);
        BankAccount destinationAccount = new BankAccount(toAccountNumber, "Babak", 500.0);

        when(repository.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.of(sourceAccount));
        when(repository.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(destinationAccount));

        assertThrows(InsufficientFundsException.class,
                () -> transferStrategy.execute(repository, fromAccountNumber, amount, toAccountNumber),
                "Should throw InsufficientFundsInSourceAccountException for insufficient funds");

        verify(repository, times(1)).findByAccountNumber(fromAccountNumber);
        verify(repository, times(1)).findByAccountNumber(toAccountNumber);
        verify(repository, never()).save(any(BankAccount.class));
    }
}