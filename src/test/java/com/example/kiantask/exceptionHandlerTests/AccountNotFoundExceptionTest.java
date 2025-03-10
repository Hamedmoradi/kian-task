package com.example.kiantask.exceptionHandlerTests;

import com.example.kiantask.enums.GeneralExceptionEnums;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        AccountNotFoundException exception = new AccountNotFoundException();

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertNull(exception.getCause(), "Cause should be null in default constructor");
    }

    @Test
    void testConstructorWithCause() {

        Throwable cause = new IllegalStateException("Test cause");

        AccountNotFoundException exception = new AccountNotFoundException(cause);

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), exception.getMessage(), "Message should match enum value");
        assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(), exception.getCode(), "Code should match enum value");
        assertEquals(cause, exception.getCause(), "Cause should match provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    void testExceptionThrownAndCaught() {
        try {
            throw new AccountNotFoundException();
        } catch (AccountNotFoundException e) {
            assertEquals("Account not found", e.getMessage(), "Caught exception message should match enum value");
            assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(), e.getCode(), "Caught exception code should match enum value");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    void testExceptionWithCauseThrownAndCaught() {

        Throwable cause = new NullPointerException("Null pointer test");

        try {
            throw new AccountNotFoundException(cause);
        } catch (AccountNotFoundException e) {
            assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), e.getMessage(), "Caught exception message should match enum value");
            assertEquals(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(), e.getCode(), "Caught exception code should match enum value");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Null pointer test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}