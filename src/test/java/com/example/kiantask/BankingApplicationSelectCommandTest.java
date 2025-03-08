package com.example.kiantask;

import com.example.kiantask.service.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BankingApplicationSelectCommandTest {

    private Bank bank;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        bank = Mockito.mock(Bank.class);
        MockitoAnnotations.openMocks(this);

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testSelectCommandCreateAccount() {
        String input = "12345\nAli\n1000.0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankingApplication.selectCommand(bank, 1, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Account created!"), "Should display account creation success");
        verify(bank, times(1)).createAccount("12345", "Ali", 1000.0);
    }

    @Test
    void testSelectCommandDeposit() {
        String input = "12345\n500.0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankingApplication.selectCommand(bank, 2, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Deposit successful!"), "Should display deposit success");
        verify(bank, times(1)).deposit("12345", 500.0);
    }

    @Test
    void testSelectCommandWithdraw() {
        String input = "12345\n200.0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankingApplication.selectCommand(bank, 3, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Withdrawal successful!"), "Should display withdrawal success");
        verify(bank, times(1)).withdraw("12345", 200.0);
    }

    @Test
    void testSelectCommandTransfer() {
        String input = "12345\n67890\n300.0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankingApplication.selectCommand(bank, 4, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Transfer successful!"), "Should display transfer success");
        verify(bank, times(1)).transfer("12345", "67890", 300.0);
    }

    @Test
    void testSelectCommandCheckBalance() {
        String input = "12345\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        when(bank.getBalance("12345")).thenReturn(1000.0);

        BankingApplication.selectCommand(bank, 5, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Balance: 1000.0"), "Should display balance");
        verify(bank, times(1)).getBalance("12345");
    }

    @Test
    void testSelectCommandInvalidOption() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("".getBytes()));

        BankingApplication.selectCommand(bank, 7, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid option! Please choose between 1 and 6."), "Should display invalid option message");
        verifyNoInteractions(bank);
    }

    @Test
    void testSelectCommandCreateAccountInputMismatch() {
        String input = "12345\nAli\nabc\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        assertThrows(InputMismatchException.class, () -> BankingApplication.selectCommand(bank, 1, scanner),
                "Invalid balance input should throw InputMismatchException");

        String output = outputStream.toString();
        assertFalse(output.contains("Account created!"), "Should not display success message on invalid input");
        verifyNoInteractions(bank);
    }

}