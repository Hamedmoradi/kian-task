package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.repository.BankAccountRepository;
import com.example.kiantask.util.annotation.Numeric;
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
class BankAccountTest {

    @Autowired
    private BankAccountRepository repository;

    private Validator validator;

    private BankAccount account1;

    private BankAccount account2;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.flush();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        account1 = new BankAccount("88888", "Mehdi", 1000.0);
        account2 = new BankAccount("88888", "Mehdi", 1000.0);
    }

    static class TestEntity {
        @Numeric
        private String value;

        public TestEntity(String value) {
            this.value = value;
        }
    }

    @Test
    void testBankAccountCreationAndPersistence() {
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
    void testConstructorWithParameters() {
        BankAccount account = new BankAccount("67890", "Ali", 500.0);

        assertEquals("67890", account.getAccountNumber(), "Account number should be set by constructor");
        assertEquals("Ali", account.getAccountHolderName(), "Account holder name should be set by constructor");
        assertEquals(500.0, account.getBalance(), 0.01, "Balance should be set by constructor");
        assertEquals(0, account.getId(), "ID should be 0 (not set) before persistence");
    }

    @Test
    void testDefaultConstructorAndSetters() {
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
    void testUniqueAccountNumberConstraint() {
        BankAccount account1 = new BankAccount("22222", "Sara", 300.0);
        BankAccount account2 = new BankAccount("22222", "Eli", 400.0);
        repository.save(account1);

        assertThrows(Exception.class, () -> repository.saveAndFlush(account2), "Saving account with duplicate account number should throw an exception");
    }

    @Test
    void testUniqueAccountHolderNameConstraint() {
        BankAccount account1 = new BankAccount("33333", "Franak", 500.0);
        BankAccount account2 = new BankAccount("44444", "Franak", 600.0);
        repository.save(account1);

        assertThrows(Exception.class, () -> repository.saveAndFlush(account2), "Saving account with duplicate account holder name should throw an exception");
    }

    @Test
    void testBalancePositiveConstraint() {
        BankAccount account = new BankAccount("55555", "Mori", -100.0);

        Set<ConstraintViolation<BankAccount>> violations = validator.validate(account);

        assertFalse(violations.isEmpty(), "Negative balance should trigger a validation error");
        assertEquals(1, violations.size(), "Exactly one violation expected");
        assertEquals("Balance must be greater than zero", violations.iterator().next().getMessage(), "Violation message should match @Positive constraint");
    }

    @Test
    void testToString() {
        BankAccount account = new BankAccount("66666", "Hani", 700.0);
        account.setId(42);

        String result = account.toString();

        assertEquals("BankAccount(id=42, accountNumber=66666, accountHolderName=Hani, balance=700.0)", result, "toString should return formatted string");
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(account1.equals(account1), "An object should be equal to itself");
    }

    @Test
    void testEqualsNull() {
        assertFalse(account1.equals(null), "An object should not be equal to null");
    }

    @Test
    void testEqualsDifferentClass() {
        assertFalse(account1.equals("not a BankAccount"), "An object should not be equal to a different class");
    }

    @Test
    void testEqualsSameValues() {
        assertTrue(account1.equals(account2), "Objects with same field values should be equal");
    }

    @Test
    void testEqualsDifferentId() {
        account1.setId(1);
        account2.setId(2);
        assertFalse(account1.equals(account2), "Objects with different IDs should not be equal");
    }

    @Test
    void testEqualsDifferentAccountNumber() {
        account2 = new BankAccount("67890", "Ali", 1000.0);
        assertFalse(account1.equals(account2), "Objects with different account numbers should not be equal");
    }

    @Test
    void testEqualsDifferentAccountHolderName() {
        account2 = new BankAccount("12345", "BabaK", 1000.0);
        assertFalse(account1.equals(account2), "Objects with different account holder names should not be equal");
    }

    @Test
    void testEqualsDifferentBalance() {
        account2 = new BankAccount("12345", "Ali", 2000.0);
        assertFalse(account1.equals(account2), "Objects with different balances should not be equal");
    }

    @Test
    void testHashCodeSameObject() {
        assertEquals(account1.hashCode(), account1.hashCode(), "HashCode should be consistent for the same object");
    }

    @Test
    void testHashCodeEqualObjects() {
        assertEquals(account1.hashCode(), account2.hashCode(), "Equal objects should have the same hashCode");
    }

    @Test
    void testHashCodeDifferentId() {
        account1.setId(1);
        account2.setId(2);
        assertNotEquals(account1.hashCode(), account2.hashCode(), "Objects with different IDs should have different hashCodes");
    }

    @Test
    void testHashCodeDifferentAccountNumber() {
        account2 = new BankAccount("67890", "Ali", 1000.0);
        assertNotEquals(account1.hashCode(), account2.hashCode(), "Objects with different account numbers should have different hashCodes");
    }

    @Test
    void testHashCodeDifferentAccountHolderName() {
        account2 = new BankAccount("12345", "BabaK", 1000.0);
        assertNotEquals(account1.hashCode(), account2.hashCode(), "Objects with different account holder names should have different hashCodes");
    }

    @Test
    void testHashCodeDifferentBalance() {
        account2 = new BankAccount("12345", "Ali", 2000.0);
        assertNotEquals(account1.hashCode(), account2.hashCode(), "Objects with different balances should have different hashCodes");
    }

    @Test
    void testConstructorInitializesFields() {
        BankAccount account = new BankAccount("12345", "Ali", 1000.0);

        assertEquals("12345", account.getAccountNumber(), "Account number should be set by constructor");
        assertEquals("Ali", account.getAccountHolderName(), "Account holder name should be set by constructor");
        assertEquals(1000.0, account.getBalance(), 0.01, "Balance should be set by constructor");
        assertEquals(0, account.getId(), "ID should be 0 (unset) by constructor");
    }

    @Test
    void testConstructorNullAccountNumber() {
        BankAccount account = new BankAccount(null, "Ali", 1000.0);

        assertNull(account.getAccountNumber(), "Account number can be null (JPA validation occurs later)");
        assertEquals("Ali", account.getAccountHolderName(), "Account holder name should be set");
        assertEquals(1000.0, account.getBalance(), 0.01, "Balance should be set");
    }

    @Test
    void testConstructorNullAccountHolderName() {
        BankAccount account = new BankAccount("12345", null, 1000.0);

        assertEquals("12345", account.getAccountNumber(), "Account number should be set");
        assertNull(account.getAccountHolderName(), "Account holder name can be null (JPA validation occurs later)");
        assertEquals(1000.0, account.getBalance(), 0.01, "Balance should be set");
    }

    @Test
    void testConstructorNegativeBalance() {
        BankAccount account = new BankAccount("12345", "Ali", -100.0);

        assertEquals("12345", account.getAccountNumber(), "Account number should be set");
        assertEquals("Ali", account.getAccountHolderName(), "Account holder name should be set");
        assertEquals(-100.0, account.getBalance(), 0.01, "Balance can be negative (JPA validation occurs later)");
    }

    @Test
    void testValidNumericString() {
        TestEntity entity = new TestEntity("12345");
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);
        assertEquals(0, violations.size(), "Valid numeric string should pass validation");
    }

    @Test
    void testInvalidNumericString() {
        TestEntity entity = new TestEntity("123abc");
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);
        assertEquals(1, violations.size(), "Non-numeric string should fail validation");
    }

    @Test
    void testEmptyString() {
        TestEntity entity = new TestEntity("");
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);
        assertEquals(1, violations.size(), "Empty string should fail validation");
    }

    @Test
    void testNullValue() {
        TestEntity entity = new TestEntity(null);
        Set<ConstraintViolation<TestEntity>> violations = validator.validate(entity);
        assertEquals(1, violations.size(), "Null value should fail validation");
    }
}