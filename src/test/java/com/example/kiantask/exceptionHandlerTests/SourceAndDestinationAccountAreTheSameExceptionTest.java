package com.example.kiantask.exceptionHandlerTests;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.SourceAndDestinationAccountAreTheSameException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SourceAndDestinationAccountAreTheSameExceptionTest {

    @Test
    void testDefaultConstructor() {

        SourceAndDestinationAccountAreTheSameException exception = new SourceAndDestinationAccountAreTheSameException();

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    void testConstructorWithCause() {

        Throwable cause = new IllegalArgumentException("Test cause");

        SourceAndDestinationAccountAreTheSameException exception = new SourceAndDestinationAccountAreTheSameException(cause);

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    void testExceptionThrownAndCaughtWithoutCause() {

        try {
            throw new SourceAndDestinationAccountAreTheSameException();
        } catch (SourceAndDestinationAccountAreTheSameException e) {
            assertEquals("Source and destination account are the same", e.getMessage(), "Caught exception message should match enum value");
            assertEquals(100006, e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    void testExceptionThrownAndCaughtWithCause() {

        Throwable cause = new NullPointerException("Null pointer test");

        try {
            throw new SourceAndDestinationAccountAreTheSameException(cause);
        } catch (SourceAndDestinationAccountAreTheSameException e) {
            assertEquals("Source and destination account are the same", e.getMessage(), "Caught exception message should match enum value");
            assertEquals(100006, e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}