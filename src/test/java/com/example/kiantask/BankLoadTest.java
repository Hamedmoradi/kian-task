package com.example.kiantask;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.repository.BankAccountRepository;
import com.example.kiantask.service.Bank;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @BeforeAll
    public static void setupAccounts(@Autowired Bank bank, @Autowired BankAccountRepository repository) {
        repository.deleteAll();
        bank.createAccount("123", "Alice", 1000.0);
        bank.createAccount("456", "Bob", 100.0);
    }

    @BeforeEach
    public void resetBalances() {
        BankAccount account123 = repository.findByAccountNumber("123").get();
        account123.setBalance(1000.0);
        repository.save(account123);

        BankAccount account456 = repository.findByAccountNumber("456").get();
        account456.setBalance(100.0);
        repository.save(account456);
    }

    @Test
    @Transactional
    public void testConcurrentTransactions() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < 5; i++) {
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

        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        assertEquals(1250.0, bank.getBalance("123"), 0.01, "Balance should reflect concurrent transactions");
    }

    @Test
    @Transactional
    public void testConcurrentTransfers() throws InterruptedException {
        int numThreads = 10;
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

        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        assertEquals(500.0, bank.getBalance("123"), 0.01, "From account balance incorrect");
        assertEquals(600.0, bank.getBalance("456"), 0.01, "To account balance incorrect");
    }
}