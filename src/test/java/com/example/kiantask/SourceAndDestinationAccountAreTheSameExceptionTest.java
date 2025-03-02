package com.example.kiantask;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.SourceAndDestinationAccountAreTheSameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SourceAndDestinationAccountAreTheSameExceptionTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        SourceAndDestinationAccountAreTheSameException exception = new SourceAndDestinationAccountAreTheSameException();

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    public void testConstructorWithCause() {
        // Arrange
        Throwable cause = new IllegalArgumentException("Test cause");

        // Act
        SourceAndDestinationAccountAreTheSameException exception = new SourceAndDestinationAccountAreTheSameException(cause);

        // Assert
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    public void testExceptionThrownAndCaughtWithoutCause() {
        // Act & Assert
        try {
            throw new SourceAndDestinationAccountAreTheSameException();
        } catch (SourceAndDestinationAccountAreTheSameException e) {
            assertEquals("Source and destination account are the same", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100006, e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    public void testExceptionThrownAndCaughtWithCause() {
        // Arrange
        Throwable cause = new NullPointerException("Null pointer test");

        // Act & Assert
        try {
            throw new SourceAndDestinationAccountAreTheSameException(cause);
        } catch (SourceAndDestinationAccountAreTheSameException e) {
            assertEquals("Source and destination account are the same", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100006, e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}