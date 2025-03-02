package com.example.kiantask;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        AccountNotFoundException exception = new AccountNotFoundException();

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(),
                exception.getMessageCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    void testConstructorWithCause() {
        // Arrange
        Throwable cause = new IllegalStateException("Test cause");

        // Act
        AccountNotFoundException exception = new AccountNotFoundException(cause);

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(),
                exception.getMessageCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    void testExceptionThrownAndCaught() {
        // Act & Assert
        try {
            throw new AccountNotFoundException();
        } catch (AccountNotFoundException e) {
            assertEquals("account not found", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100001, e.getMessageCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    void testExceptionWithCauseThrownAndCaught() {

        Throwable cause = new NullPointerException("Null pointer test");

        try {
            throw new AccountNotFoundException(cause);
        } catch (AccountNotFoundException e) {
            assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100001, e.getMessageCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}