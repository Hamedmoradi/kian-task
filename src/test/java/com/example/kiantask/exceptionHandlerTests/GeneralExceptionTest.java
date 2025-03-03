package com.example.kiantask.exceptionHandlerTests;

import com.example.kiantask.exceptionHandler.GeneralException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GeneralExceptionTest {

    @Test
    void testConstructorWithMessageAndCode() {

        GeneralException exception = new GeneralException("Test message", 1001);

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals("Test message", exception.getMessage(), "Message should match provided value");
        assertEquals(1001, exception.getCode(), "Code should match provided value");
        assertNull(exception.getCause(), "Cause should be null");
    }

    @Test
    void testConstructorWithMessageCodeAndCause() {

        Throwable cause = new IllegalArgumentException("Test cause");

        GeneralException exception = new GeneralException("Test message", 1001, cause);

        assertNotNull(exception, "Exception should be instantiated");
        assertEquals("Test message", exception.getMessage(), "Message should match provided value");
        assertEquals(1001, exception.getCode(), "Code should match provided value");
        assertEquals(cause, exception.getCause(), "Cause should match provided throwable");
        assertEquals("Test cause", exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    void testAllArgsConstructor() {

        GeneralException exception = new GeneralException(1001);

        assertNotNull(exception, "Exception should be instantiated");
        assertNull(exception.getMessage(), "Message should be null when not provided");
        assertEquals(1001, exception.getCode(), "Code should match provided value");
        assertNull(exception.getCause(), "Cause should be null");
    }

    @Test
    void testEqualsSameObject() {
        GeneralException exception = new GeneralException("Test message", 1001);
        assertTrue(exception.equals(exception), "An object should be equal to itself");
    }

    @Test
    void testEqualsNull() {
        GeneralException exception = new GeneralException("Test message", 1001);
        assertFalse(exception.equals(null), "An object should not be equal to null");
    }

    @Test
    void testEqualsDifferentClass() {
        GeneralException exception = new GeneralException("Test message", 1001);
        assertFalse(exception.equals("not an exception"), "An object should not be equal to a different class");
    }

    @Test
    void testEqualsDifferentCode() {
        GeneralException exception1 = new GeneralException("Test message", 1001);
        GeneralException exception2 = new GeneralException("Test message", 1002);
        assertFalse(exception1.equals(exception2), "Objects with different codes should not be equal");
    }

    @Test
    void testEqualsDifferentMessage() {
        GeneralException exception1 = new GeneralException("Test message 1", 1001);
        GeneralException exception2 = new GeneralException("Test message 2", 1001);
        assertFalse(exception1.equals(exception2), "Objects with different messages should not be equal (due to callSuper=true)");
    }

    @Test
    void testEqualsWithCause() {
        Throwable cause1 = new IllegalArgumentException("Cause 1");
        Throwable cause2 = new IllegalArgumentException("Cause 2");
        GeneralException exception1 = new GeneralException("Test message", 1001, cause1);
        GeneralException exception2 = new GeneralException("Test message", 1001, cause2);
        assertFalse(exception1.equals(exception2), "Objects with different causes should not be equal (due to callSuper=true)");
    }

    @Test
    void testHashCodeSameObject() {
        GeneralException exception = new GeneralException("Test message", 1001);
        assertEquals(exception.hashCode(), exception.hashCode(), "HashCode should be consistent for the same object");
    }

    @Test
    void testHashCodeDifferentCode() {
        GeneralException exception1 = new GeneralException("Test message", 1001);
        GeneralException exception2 = new GeneralException("Test message", 1002);
        assertNotEquals(exception1.hashCode(), exception2.hashCode(), "Objects with different codes should have different hashCodes");
    }

    @Test
    void testHashCodeDifferentMessage() {
        GeneralException exception1 = new GeneralException("Test message 1", 1001);
        GeneralException exception2 = new GeneralException("Test message 2", 1001);
        assertNotEquals(exception1.hashCode(), exception2.hashCode(), "Objects with different messages should have different hashCodes (due to callSuper=true)");
    }

    @Test
    void testThrownAndCaughtWithoutCause() {
        try {
            throw new GeneralException("Test message", 1001);
        } catch (GeneralException e) {
            assertEquals("Test message", e.getMessage(), "Caught exception message should match");
            assertEquals(1001, e.getCode(), "Caught exception code should match");
            assertNull(e.getCause(), "Caught exception should have no cause");
        }
    }

    @Test
    void testThrownAndCaughtWithCause() {
        Throwable cause = new NullPointerException("Cause test");
        try {
            throw new GeneralException("Test message", 1001, cause);
        } catch (GeneralException e) {
            assertEquals("Test message", e.getMessage(), "Caught exception message should match");
            assertEquals(1001, e.getCode(), "Caught exception code should match");
            assertEquals(cause, e.getCause(), "Caught exception cause should match");
            assertEquals("Cause test", e.getCause().getMessage(), "Cause message should match");
        }
    }
}