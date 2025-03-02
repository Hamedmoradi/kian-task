package com.example.kiantask;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.AccountNumberIsAlreadyExistException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountNumberIsAlreadyExistExceptionTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        AccountNumberIsAlreadyExistException exception = new AccountNumberIsAlreadyExistException();

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    public void testConstructorWithCause() {
        // Arrange
        Throwable cause = new IllegalArgumentException("Test cause");

        // Act
        AccountNumberIsAlreadyExistException exception = new AccountNumberIsAlreadyExistException(cause);

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    public void testExceptionThrownAndCaughtWithoutCause() {
        // Act & Assert
        try {
            throw new AccountNumberIsAlreadyExistException();
        } catch (AccountNumberIsAlreadyExistException e) {
            assertEquals("Account number already exists", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100004, e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    public void testExceptionThrownAndCaughtWithCause() {
        // Arrange
        Throwable cause = new NullPointerException("Null pointer test");

        // Act & Assert
        try {
            throw new AccountNumberIsAlreadyExistException(cause);
        } catch (AccountNumberIsAlreadyExistException e) {
            assertEquals("Account number already exists", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100004, e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}