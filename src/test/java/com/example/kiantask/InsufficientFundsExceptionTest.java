package com.example.kiantask;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InsufficientFundsExceptionTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        InsufficientFundsException exception = new InsufficientFundsException();

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    public void testConstructorWithCause() {
        // Arrange
        Throwable cause = new IllegalArgumentException("Test cause");

        // Act
        InsufficientFundsException exception = new InsufficientFundsException(cause);

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    public void testExceptionThrownAndCaughtWithoutCause() {
        // Act & Assert
        try {
            throw new InsufficientFundsException();
        } catch (InsufficientFundsException e) {
            assertEquals("Insufficient funds", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100007, e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    public void testExceptionThrownAndCaughtWithCause() {
        // Arrange
        Throwable cause = new NullPointerException("Null pointer test");

        // Act & Assert
        try {
            throw new InsufficientFundsException(cause);
        } catch (InsufficientFundsException e) {
            assertEquals("Insufficient funds", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100007, e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}