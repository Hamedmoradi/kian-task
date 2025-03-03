package com.example.kiantask;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.InsufficientFundsInSourceAccountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientFundsInSourceAccountExceptionTest {

    @Test
    void testDefaultConstructor() {

        InsufficientFundsInSourceAccountException exception = new InsufficientFundsInSourceAccountException();

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    public void testConstructorWithCause() {

        Throwable cause = new IllegalArgumentException("Test cause");

        InsufficientFundsInSourceAccountException exception = new InsufficientFundsInSourceAccountException(cause);

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
     void testExceptionThrownAndCaughtWithoutCause() {

        try {
            throw new InsufficientFundsInSourceAccountException();
        } catch (InsufficientFundsInSourceAccountException e) {
            assertEquals("Insufficient funds in source account", e.getMessage(),
                    "Caught exception message should match enum value");
            assertEquals(100010, e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
     void testExceptionThrownAndCaughtWithCause() {

        Throwable cause = new NullPointerException("Null pointer test");

        try {
            throw new InsufficientFundsInSourceAccountException(cause);
        } catch (InsufficientFundsInSourceAccountException e) {
            assertEquals("Insufficient funds in source account", e.getMessage(), "Caught exception message should match enum value");
            assertEquals(100010, e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}