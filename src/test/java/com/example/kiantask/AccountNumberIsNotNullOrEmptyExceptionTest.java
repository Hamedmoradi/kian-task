package com.example.kiantask;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.AccountNumberIsNotNullOrEmptyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberIsNotNullOrEmptyExceptionTest {

    @Test
    void testDefaultConstructor() {
        AccountNumberIsNotNullOrEmptyException exception = new AccountNumberIsNotNullOrEmptyException();

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    void testConstructorWithCause() {

        Throwable cause = new IllegalArgumentException("Test cause");

        AccountNumberIsNotNullOrEmptyException exception = new AccountNumberIsNotNullOrEmptyException(cause);

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    void testExceptionThrownAndCaughtWithoutCause() {
        try {
            throw new AccountNumberIsNotNullOrEmptyException();
        } catch (AccountNumberIsNotNullOrEmptyException e) {
            assertEquals("Account number cannot be null or empty", e.getMessage(), "Caught exception message should match enum value");
            assertEquals(100002, e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    void testExceptionThrownAndCaughtWithCause() {

        Throwable cause = new NullPointerException("Null pointer test");

        try {
            throw new AccountNumberIsNotNullOrEmptyException(cause);
        } catch (AccountNumberIsNotNullOrEmptyException e) {
            assertEquals("Account number cannot be null or empty", e.getMessage(), "Caught exception message should match enum value");
            assertEquals(100002, e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}