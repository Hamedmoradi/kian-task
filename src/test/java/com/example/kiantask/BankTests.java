package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.pattern.observer.impl.TransactionLogger;
import com.example.kiantask.repository.BankAccountRepository;
import com.example.kiantask.service.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BankTests {

    @Autowired
    private Bank bank;

    @Autowired
    private BankAccountRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        bank.createAccount("123", "Alice", 1000.0);
        bank.createAccount("456", "Bob", 100.0);
    }

    @Test
    public void testCreateAccount_Success() throws Exception {
        BankAccount account = bank.createAccount("789", "Charlie", 300.0);
        assertEquals("789", account.getAccountNumber());
        assertEquals("Charlie", account.getAccountHolderName());
        assertEquals(300.0, account.getBalance(), 0.01);
    }

    @Test
    public void testCreateAccount_NullAccountNumber() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.createAccount(null, "Alice", 100.0);
        });
        assertEquals("Account number cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testCreateAccount_EmptyAccountNumber() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.createAccount("", "Alice", 100.0);
        });
        assertEquals("Account number cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testCreateAccount_NegativeBalance() {
        jakarta.validation.ConstraintViolationException exception = assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            bank.createAccount("789", "Charlie", -50.0);
        });
        assertTrue(exception.getMessage().contains("Balance must be greater than zero"));
    }

    @Test
    public void testCreateAccount_DuplicateAccount() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bank.createAccount("123", "Bob", 200.0);
        });
        assertEquals("Account number already exists", exception.getMessage());
    }

    @Test
    public void testDeposit_Success() throws Exception {
        bank.deposit("123", 50.0);
        assertEquals(1050.0, bank.getBalance("123"), 0.01);
    }

    @Test
    public void testDeposit_NonExistentAccount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.deposit("999", 50.0);
        });
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testDeposit_NegativeAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.deposit("123", -50.0);
        });
        assertEquals("Transaction amount must be positive", exception.getMessage());
    }

    @Test
    public void testWithdraw_Success() throws Exception {
        bank.withdraw("123", 50.0);
        assertEquals(950.0, bank.getBalance("123"), 0.01);
    }

    @Test
    public void testWithdraw_InsufficientFunds() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bank.withdraw("123", 1500.0);
        });
        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    public void testTransfer_Success() throws Exception {
        bank.transfer("123", "456", 50.0);
        assertEquals(950.0, bank.getBalance("123"), 0.01);
        assertEquals(150.0, bank.getBalance("456"), 0.01);
    }

    @Test
    public void testTransfer_SameAccount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.transfer("123", "123", 50.0);
        });
        assertEquals("Cannot transfer to the same account", exception.getMessage());
    }

    @Test
    public void testTransfer_InsufficientFunds() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bank.transfer("123", "456", 1500.0);
        });
        assertEquals("Insufficient funds in from account", exception.getMessage());
    }

    @Test
    public void testGetBalance_Success() {
        double balance = bank.getBalance("123");
        assertEquals(1000.0, balance, 0.01);
    }

    @Test
    public void testGetBalance_NonExistentAccount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.getBalance("999");
        });
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    public void testTransactionLogger_LogsCorrectFormat() throws Exception {
        TransactionLogger logger = new TransactionLogger();
        String accountNumber = "123";
        String transactionType = "DEPOSIT";
        double amount = 50.0;
        logger.onTransaction(accountNumber, transactionType, amount);
        File logFile = new File("transactions.log");
        assertTrue(logFile.exists(), "Transaction log file should be created");
        try (java.util.Scanner scanner = new java.util.Scanner(logFile)) {
            String logEntry = scanner.nextLine();
            assertTrue(logEntry.contains(accountNumber), "Log should contain account number");
            assertTrue(logEntry.contains(transactionType), "Log should contain transaction type");
            assertTrue(logEntry.contains(String.format("%.2f", amount)), "Log should contain amount");
        }
    }

    @Test
//    @Transactional(rollbackOn = Exception.class) // Explicit rollback
    @Transactional // Explicit rollback
    public void testSetBalance_NegativeValue() throws Exception {
        BankAccount account = repository.findByAccountNumber("123").get();
        account.setBalance(-50.0);
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            repository.saveAndFlush(account); // Force validation
        });
    }

    @Test
//    @Transactional(rollbackOn = Exception.class) // Explicit rollback
    @Transactional
    public void testSetBalance_ZeroValue() throws Exception {
        BankAccount account = repository.findByAccountNumber("123").get();
        account.setBalance(0.0);
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            repository.saveAndFlush(account); // Force validation
        });
    }
}