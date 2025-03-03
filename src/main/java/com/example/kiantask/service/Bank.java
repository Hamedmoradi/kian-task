package com.example.kiantask.service;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.enums.TransactionTypeEnum;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.exceptionHandler.AccountNumberIsAlreadyExistException;
import com.example.kiantask.exceptionHandler.AccountNumberIsNotNullOrEmptyException;
import com.example.kiantask.exceptionHandler.SourceAndDestinationAccountAreTheSameException;
import com.example.kiantask.pattern.observer.TransactionObserver;
import com.example.kiantask.pattern.observer.impl.TransactionLogger;
import com.example.kiantask.pattern.strategy.TransactionStrategy;
import com.example.kiantask.pattern.strategy.impl.DepositStrategy;
import com.example.kiantask.pattern.strategy.impl.TransferStrategy;
import com.example.kiantask.pattern.strategy.impl.WithdrawalStrategy;
import com.example.kiantask.repository.BankAccountRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.kiantask.util.validator.AccountValidator.checkAmount;
import static com.example.kiantask.util.validator.AccountValidator.validateAccountDetail;

@Service
public class Bank {
    private final BankAccountRepository repository;
    private final List<TransactionObserver> observers = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ReentrantLock lock = new ReentrantLock(); // Added for synchronization

    public Bank(BankAccountRepository repository) {
        observers.add(new TransactionLogger());
        this.repository = repository;
    }

    @Transactional
    @SneakyThrows
    public BankAccount createAccount(String accountNumber, String accountHolderName, double initialBalance) {
        validateAccountDetail(accountNumber, accountHolderName);
        isAccountExist(accountNumber);
        return repository.save(new BankAccount(accountNumber, accountHolderName, initialBalance));
    }


    @SneakyThrows
    public void performTransaction(TransactionStrategy strategy, String sourceAccount, double amount, String destinationAccount, String transactionType) {
        checkAmount(amount);
        lock.lock(); // Synchronize access to prevent concurrent updates
        try {
            retryTransaction(() -> {
                strategy.execute(repository, sourceAccount, amount, destinationAccount);
                notifyObservers(sourceAccount, transactionType, amount);
                if (TransactionTypeEnum.TRANSFER.getValue().equals(transactionType) && destinationAccount != null) {
                    notifyObservers(destinationAccount, TransactionTypeEnum.TRANSFER_IN.getValue(), amount);
                }
            }, transactionType, sourceAccount, amount);
        } finally {
            lock.unlock();
        }
    }

    public double getBalance(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new AccountNumberIsNotNullOrEmptyException();
        }
        return repository.findByAccountNumber(accountNumber)
                .map(BankAccount::getBalance)
                .orElseThrow(AccountNotFoundException::new);
    }

    private void notifyObservers(String accountNumber, String transactionType, double amount) {
        executorService.submit(() -> {
            for (TransactionObserver observer : observers) {
                try {
                    observer.onTransaction(accountNumber, transactionType, amount);
                } catch (Exception e) {
                    System.err.println("Failed to notify observer: " + e.getMessage());
                }
            }
        });
    }

    public void deposit(String accountNumber, double amount) {
        performTransaction(new DepositStrategy(), accountNumber, amount, null, TransactionTypeEnum.DEPOSIT.getValue());
    }

    public void withdraw(String accountNumber, double amount) {
        performTransaction(new WithdrawalStrategy(), accountNumber, amount, null, TransactionTypeEnum.WITHDRAW.getValue());
    }

    public void transfer(String sourceAccount, String destinationAccount, double amount) {
        if (sourceAccount.equals(destinationAccount)) {
            throw new SourceAndDestinationAccountAreTheSameException();
        }
        performTransaction(new TransferStrategy(), sourceAccount, amount, destinationAccount, TransactionTypeEnum.TRANSFER.getValue());
    }

    private void retryTransaction(Runnable transaction, String type, String accountNumber, double amount) {
        int retries = 5;
        for (int i = 0; i < retries; i++) {
            try {
                transaction.run();
                return;
            } catch (Exception e) {
                if (i == retries - 1 || !isRetryable(e)) {
                    System.err.println(type + " failed after " + (i + 1) + " retries for account " + accountNumber + " with amount " + amount + " : " + e.getMessage());
                    throw e;
                }
                try {
                    System.err.println(type + " attempt " + (i + 1) + " failed for account " + accountNumber + " with amount " + amount + " : " + e.getMessage() + " - Retrying...");
                    Thread.sleep(100 * (i + 1)); // Exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }
    }

    private boolean isRetryable(Exception e) {
        return e instanceof org.springframework.dao.ConcurrencyFailureException ||
                e.getMessage().contains("Deadlock detected") ||
                e.getMessage().contains("Lock wait timeout") ||
                e.getMessage().contains("could not obtain lock");
    }

    private void isAccountExist(String accountNumber) {
        if (repository.existsByAccountNumber(accountNumber)) {
            throw new AccountNumberIsAlreadyExistException();
        }
    }
}