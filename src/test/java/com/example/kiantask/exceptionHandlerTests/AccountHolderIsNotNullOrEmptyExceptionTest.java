package com.example.kiantask.exceptionHandlerTests;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.AccountHolderIsNotNullOrEmptyException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountHolderIsNotNullOrEmptyExceptionTest {

    @Test
    void testDefaultConstructor() {
        AccountHolderIsNotNullOrEmptyException exception = new AccountHolderIsNotNullOrEmptyException();
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    void testConstructorWithCause() {

        Throwable cause = new IllegalArgumentException("Test cause");
        AccountHolderIsNotNullOrEmptyException exception = new AccountHolderIsNotNullOrEmptyException(cause);

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(),
                exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode(),
                exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match the provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    void testExceptionThrownAndCaught() {

        try {
            throw new AccountHolderIsNotNullOrEmptyException();
        } catch (AccountHolderIsNotNullOrEmptyException e) {
            assertEquals("Account holder name can not be null or empty", e.getMessage(),
                    "Caught exception message should match");
            assertEquals(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), e.getMessage(), "Caught exception code should match");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    void testExceptionWithCauseThrownAndCaught() {

        Throwable cause = new NullPointerException("Null pointer test");

        try {
            throw new AccountHolderIsNotNullOrEmptyException(cause);
        } catch (AccountHolderIsNotNullOrEmptyException e) {
            assertEquals("Account holder name can not be null or empty", e.getMessage(), "Caught exception message should match");
            assertEquals(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), e.getMessage(), "Caught exception code should match");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}