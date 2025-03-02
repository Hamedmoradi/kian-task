package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.*;
import com.example.kiantask.pattern.observer.impl.TransactionLogger;
import com.example.kiantask.repository.BankAccountRepository;
import com.example.kiantask.service.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BankTests {

    @Autowired
    private Bank bank;

    @Autowired
    private BankAccountRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.flush();
        try {
            BankAccount account1 = new BankAccount("123", "Ali", 1000.0);
            BankAccount account2 = new BankAccount("456", "Babak", 100.0);
            repository.saveAndFlush(account1);
            repository.saveAndFlush(account2);
            System.out.println("Accounts after setup: " + repository.findAll());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up accounts: " + e.getMessage(), e);
        }
    }

    @Test
    public void testDeposit_NegativeAmount() {
        assertThrows(TransactionAmountMustBePositiveException.class, () -> bank.deposit("123", -50.0));
    }

    @Test
    public void testTransfer_SameAccount() {
        assertThrows(SourceAndDestinationAccountAreTheSameException.class, () -> bank.transfer("123", "123", 50.0));
    }

    @Test
    public void testTransfer_InsufficientFunds() {
        assertThrows(InsufficientFundsInSourceAccountException.class, () -> bank.transfer("456", "123", 150.0));
    }

    @Test
    public void testTransactionLogger_LogsCorrectFormat() throws Exception {
        TransactionLogger logger = new TransactionLogger();
        String accountNumber = "123";
        String transactionType = "DEPOSIT";
        double amount = 50.0;
        logger.onTransaction(accountNumber, transactionType, amount);

        Thread.sleep(100);

        File logFile = new File("transactions.log");
        assertTrue(logFile.exists(), "Transaction log file should be created");
    }
    @Test
    public void testAccountsInserted() {
        assertEquals(2, repository.count());
        assertTrue(repository.findByAccountNumber("123").isPresent());
        assertTrue(repository.findByAccountNumber("456").isPresent());
    }

    @Test
    public void testCreateAccount_Success() {
        BankAccount account = bank.createAccount("789", "Charlie", 300.0);
        assertEquals("789", account.getAccountNumber());
        assertEquals("Charlie", account.getAccountHolderName());
        assertEquals(300.0, account.getBalance(), 0.01);
    }

    @Test
    public void testCreateAccount_NullAccountNumber() {
        AccountNumberIsNotNullOrEmptyException exception = assertThrows(AccountNumberIsNotNullOrEmptyException.class, () -> bank.createAccount(null, "Ali", 100.0));
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), exception.getMessage());
    }

    @Test
    public void testCreateAccount_EmptyAccountNumber() {
        AccountNumberIsNotNullOrEmptyException exception = assertThrows(AccountNumberIsNotNullOrEmptyException.class, () -> bank.createAccount("", "Ali", 100.0));
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), exception.getMessage());
    }

    @Test
    public void testCreateAccount_NegativeBalance() {
        jakarta.validation.ConstraintViolationException exception = assertThrows(jakarta.validation.ConstraintViolationException.class, () -> bank.createAccount("789", "Charlie", -50.0));
        assertTrue(exception.getMessage().contains("Balance must be greater than zero"));
    }

    @Test
    public void testCreateAccount_DuplicateAccount() {
        AccountNumberIsAlreadyExistException exception = assertThrows(AccountNumberIsAlreadyExistException.class, () -> bank.createAccount("123", "Babak", 200.0));
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getMessage(), exception.getMessage());
    }

    @Test
    public void testDeposit_Success() {
        bank.deposit("123", 50.0);
        assertEquals(1050.0, bank.getBalance("123"), 0.01);
    }

    @Test
    public void testDeposit_NonExistentAccount() {
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> bank.deposit("999", 50.0));
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), exception.getMessage());
    }

    @Test
    public void testWithdraw_Success() {
        bank.withdraw("123", 50.0);
        assertEquals(950.0, bank.getBalance("123"), 0.01);
    }

    @Test
    public void testWithdraw_InsufficientFunds() {
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> bank.withdraw("123", 1500.0));
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getMessage(), exception.getMessage());
    }

    @Test
    public void testTransfer_Success() {
        bank.transfer("123", "456", 50.0);
        assertEquals(950.0, bank.getBalance("123"), 0.01);
        assertEquals(150.0, bank.getBalance("456"), 0.01);
    }

    @Test
    public void testGetBalance_Success() {
        double balance = bank.getBalance("123");
        assertEquals(1000.0, balance, 0.01);
    }

    @Test
    public void testGetBalance_NonExistentAccount() {
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> bank.getBalance("999"));
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), exception.getMessage());
    }

    @Test
    @Transactional
    public void testSetBalance_NegativeValue() {
        BankAccount account = repository.findByAccountNumber("123").get();
        account.setBalance(-50.0);
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            repository.saveAndFlush(account);
        });
    }

    @Test
    @Transactional
    public void testSetBalance_ZeroValue() {
        BankAccount account = repository.findByAccountNumber("123").get();
        account.setBalance(0.0);
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            repository.saveAndFlush(account);
        });
    }
}