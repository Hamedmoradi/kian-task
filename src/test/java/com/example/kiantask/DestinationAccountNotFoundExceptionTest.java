package com.example.kiantask;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.DestinationAccountNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DestinationAccountNotFoundExceptionTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        DestinationAccountNotFoundException exception = new DestinationAccountNotFoundException();

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    public void testConstructorWithCause() {
        // Arrange
        Throwable cause = new IllegalArgumentException("Test cause");

        // Act
        DestinationAccountNotFoundException exception = new DestinationAccountNotFoundException(cause);

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    public void testExceptionThrownAndCaughtWithoutCause() {
        // Act & Assert
        try {
            throw new DestinationAccountNotFoundException();
        } catch (DestinationAccountNotFoundException e) {
            assertEquals("destination account not found", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100009, e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    public void testExceptionThrownAndCaughtWithCause() {
        // Arrange
        Throwable cause = new NullPointerException("Null pointer test");

        // Act & Assert
        try {
            throw new DestinationAccountNotFoundException(cause);
        } catch (DestinationAccountNotFoundException e) {
            assertEquals("destination account not found", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100009, e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}