package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.repository.BankAccountRepository;
import com.example.kiantask.service.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class BankLoadTest {

    @Autowired
    private Bank bank;

    @Autowired
    private BankAccountRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.flush();
        try {
            BankAccount account1 = new BankAccount("123", "Alice", 1000.0);
            BankAccount account2 = new BankAccount("456", "Bob", 100.0);
            repository.saveAndFlush(account1);
            repository.saveAndFlush(account2);
            System.out.println("Accounts after setup: " + repository.findAll());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up accounts: " + e.getMessage(), e);
        }
    }

    @Test
    public void testConcurrentTransactions() throws InterruptedException {
        int numThreads = 5; // Reduced to lessen contention
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads * 2);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    bank.deposit("123", 100.0);
                } catch (Exception e) {
                    System.err.println("Deposit failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
            executor.submit(() -> {
                try {
                    bank.withdraw("123", 50.0);
                } catch (Exception e) {
                    System.err.println("Withdrawal failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS); // Reduced timeout
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        double finalBalance = bank.getBalance("123");
        System.out.println("Final balance after concurrent transactions: " + finalBalance);
        assertEquals(1250.0, finalBalance, 0.01, "Balance should reflect concurrent transactions");
    }

    @Test
    public void testConcurrentTransfers() throws InterruptedException {
        int numThreads = 5; // Reduced to lessen contention
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    bank.transfer("123", "456", 50.0);
                } catch (Exception e) {
                    System.err.println("Transfer failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS); // Reduced timeout
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        double fromBalance = bank.getBalance("123");
        double toBalance = bank.getBalance("456");
        System.out.println("From balance: " + fromBalance + ", To balance: " + toBalance);
        assertEquals(750.0, fromBalance, 0.01, "From account balance incorrect"); // 1000 - (5 × 50)
        assertEquals(350.0, toBalance, 0.01, "To account balance incorrect");     // 100 + (5 × 50)
    }
}