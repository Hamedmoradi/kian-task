package com.example.kiantask;

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
        bank.createAccount("123", "Ali", 1000.0);
        bank.createAccount("456", "Babak", 100.0);
    }

    @Test
    public void testConcurrentDeposits() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

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
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        double finalBalance = bank.getBalance("123");
        System.out.println("Final balance after concurrent deposits: " + finalBalance);
        assertEquals(2000.0, finalBalance, 0.01, "Balance should reflect 10 deposits of 100.0 each (1000 + 1000)");
    }

    @Test
    public void testConcurrentWithdrawals() throws InterruptedException {
        int numThreads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    bank.withdraw("123", 100.0);
                } catch (Exception e) {
                    System.err.println("Withdrawal failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        double finalBalance = bank.getBalance("123");
        System.out.println("Final balance after concurrent withdrawals: " + finalBalance);
        assertEquals(500.0, finalBalance, 0.01, "Balance should reflect 5 withdrawals of 100.0 each (1000 - 500)");
    }

    @Test
    public void testConcurrentTransfers() throws InterruptedException {
        int numThreads = 5;
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

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        double fromBalance = bank.getBalance("123");
        double toBalance = bank.getBalance("456");
        System.out.println("From balance: " + fromBalance + ", To balance: " + toBalance);
        assertEquals(750.0, fromBalance, 0.01, "From account should reflect 5 transfers of 50.0 (1000 - 250)");
        assertEquals(350.0, toBalance, 0.01, "To account should reflect 5 transfers of 50.0 (100 + 250)");
    }

    @Test
    public void testMixedConcurrentTransactions() throws InterruptedException {
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

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        double finalBalance = bank.getBalance("123");
        System.out.println("Final balance after mixed concurrent transactions: " + finalBalance);
        assertEquals(1250.0, finalBalance, 0.01, "Balance should reflect 5 deposits of 100.0 and 5 withdrawals of 50.0 (1000 + 500 - 250)");
    }

    @Test
    public void testRetryOnConcurrencyFailure() throws InterruptedException {
        int numThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    bank.deposit("123", 10.0);
                } catch (Exception e) {
                    System.err.println("Deposit failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(1100.0, bank.getBalance("123"), 0.01, "Balance should reflect 10 deposits of 10.0 under contention");
    }
}