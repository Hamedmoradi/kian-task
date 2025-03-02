package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.repository.BankAccountRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BankAccountTest {

    @Autowired
    private BankAccountRepository repository;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.flush();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testBankAccountCreationAndPersistence() {
        BankAccount account = new BankAccount("12345", "Hamed", 1000.0);
        BankAccount savedAccount = repository.save(account);
        Optional<BankAccount> retrievedAccount = repository.findByAccountNumber("12345");

        assertTrue(retrievedAccount.isPresent(), "Account should be persisted and retrievable");
        BankAccount found = retrievedAccount.get();

        assertEquals(savedAccount.getId(), found.getId(), "ID should match");
        assertEquals("12345", found.getAccountNumber(), "Account number should match");
        assertEquals("Hamed", found.getAccountHolderName(), "Account holder name should match");
        assertEquals(1000.0, found.getBalance(), 0.01, "Balance should match");
    }

    @Test
    public void testConstructorWithParameters() {
        BankAccount account = new BankAccount("67890", "Ali", 500.0);

        assertEquals("67890", account.getAccountNumber(), "Account number should be set by constructor");
        assertEquals("Ali", account.getAccountHolderName(), "Account holder name should be set by constructor");
        assertEquals(500.0, account.getBalance(), 0.01, "Balance should be set by constructor");
        assertEquals(0, account.getId(), "ID should be 0 (not set) before persistence");
    }

    @Test
    public void testDefaultConstructorAndSetters() {
        BankAccount account = new BankAccount();

        account.setAccountNumber("11111");
        account.setAccountHolderName("Hasan");
        account.setBalance(200.0);
        account.setId(1);

        assertEquals("11111", account.getAccountNumber(), "Account number should be set");
        assertEquals("Hasan", account.getAccountHolderName(), "Account holder name should be set");
        assertEquals(200.0, account.getBalance(), 0.01, "Balance should be set");
        assertEquals(1, account.getId(), "ID should be set");
    }

    @Test
    public void testUniqueAccountNumberConstraint() {
        BankAccount account1 = new BankAccount("22222", "Sara", 300.0);
        BankAccount account2 = new BankAccount("22222", "Eli", 400.0);
        repository.save(account1);

        assertThrows(Exception.class, () -> repository.saveAndFlush(account2), "Saving account with duplicate account number should throw an exception");
    }

    @Test
    public void testUniqueAccountHolderNameConstraint() {
        BankAccount account1 = new BankAccount("33333", "Franak", 500.0);
        BankAccount account2 = new BankAccount("44444", "Franak", 600.0);
        repository.save(account1);

        assertThrows(Exception.class, () -> repository.saveAndFlush(account2), "Saving account with duplicate account holder name should throw an exception");
    }

    @Test
    public void testBalancePositiveConstraint() {
        BankAccount account = new BankAccount("55555", "Mori", -100.0);

        Set<ConstraintViolation<BankAccount>> violations = validator.validate(account);

        assertFalse(violations.isEmpty(), "Negative balance should trigger a validation error");
        assertEquals(1, violations.size(), "Exactly one violation expected");
        assertEquals("Balance must be greater than zero", violations.iterator().next().getMessage(), "Violation message should match @Positive constraint");
    }

    @Test
    public void testToString() {
        BankAccount account = new BankAccount("66666", "Hani", 700.0);
        account.setId(42);

        String result = account.toString();

        assertEquals("BankAccount(id=42, accountNumber=66666, accountHolderName=Hani, balance=700.0)", result, "toString should return formatted string");
    }
}