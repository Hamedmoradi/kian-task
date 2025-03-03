package com.example.kiantask;

import com.example.kiantask.exceptionHandler.GeneralException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneralExceptionTest {

    @Test
    void testConstructorWithMessageAndCode() {
        String message = "Test Exception";
        int code = 400;
        GeneralException exception = new GeneralException(message, code);

        assertEquals(message, exception.getMessage());
        assertEquals(code, exception.getCode());
    }

    @Test
    void testConstructorWithMessageCodeAndCause() {
        String message = "Test Exception";
        int code = 500;
        Throwable cause = new RuntimeException("Cause Exception");
        GeneralException exception = new GeneralException(message, code, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(code, exception.getCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testAllArgsConstructor() {
        int code = 404;
        GeneralException exception = new GeneralException(code);

        assertEquals(code, exception.getCode());
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralException exception1 = new GeneralException("Error", 400);
        GeneralException exception2 = new GeneralException("Error", 400);
        GeneralException exception3 = new GeneralException("Different Error", 500);

        assertEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);
        assertEquals(exception1.hashCode(), exception2.hashCode());
        assertNotEquals(exception1.hashCode(), exception3.hashCode());
    }

    @Test
    void testToString() {
        GeneralException exception = new GeneralException("Error Message", 400);
        String expected = "GeneralException(code=400)";
        assertTrue(exception.toString().contains(expected));
    }
}
