package com.example.kiantask.service;//package com.example.kiantask.service;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.enums.TransactionTypeEnum;
import com.example.kiantask.exceptionHandler.*;
import com.example.kiantask.pattern.observer.TransactionObserver;
import com.example.kiantask.pattern.observer.impl.TransactionLogger;
import com.example.kiantask.pattern.strategy.TransactionStrategy;
import com.example.kiantask.pattern.strategy.impl.DepositStrategy;
import com.example.kiantask.pattern.strategy.impl.TransferStrategy;
import com.example.kiantask.pattern.strategy.impl.WithdrawalStrategy;
import com.example.kiantask.repository.BankAccountRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.kiantask.util.validation.AccountValidator.checkAmount;
import static com.example.kiantask.util.validation.AccountValidator.validateAccountDetail;

@Service
public class Bank {
    @Autowired
    private BankAccountRepository repository;
    private final List<TransactionObserver> observers = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public Bank() {
        observers.add(new TransactionLogger());
    }

    @Transactional
    @SneakyThrows
    public BankAccount createAccount(String accountNumber, String accountHolderName, double initialBalance) {
        validateAccountDetail(accountNumber, accountHolderName);//TODO
        if (repository.existsByAccountNumber(accountNumber)) {//TODO
            throw new AccountNumberIsAlreadyExistException();
        }
        return repository.save(new BankAccount(accountNumber, accountHolderName, initialBalance));
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    @SneakyThrows
    public void performTransaction(TransactionStrategy strategy, String accountNumber, double amount, String targetAccountNumber, String transactionType) {
        checkAmount(amount);
        strategy.execute(repository, accountNumber, amount, targetAccountNumber);
        notifyObservers(accountNumber, transactionType, amount);
        if (TransactionTypeEnum.TRANSFER.getValue().equals(transactionType) && targetAccountNumber != null) {
            notifyObservers(targetAccountNumber, TransactionTypeEnum.TRANSFER_IN.getValue(), amount);
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

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new SourceAndDestinationAccountAreTheSameException();
        }
        performTransaction(new TransferStrategy(), fromAccountNumber, amount, toAccountNumber, TransactionTypeEnum.TRANSFER.getValue());
    }

    private void retryTransaction(Runnable transaction, String type, String accountNumber, double amount) {
        int retries = 5;
        TransactionLogger logger = new TransactionLogger();
        for (int i = 0; i < retries; i++) {
            try {
                transaction.run();
                logger.onTransaction(accountNumber, type, amount);
                return;
            } catch (Exception e) {
                if (i == retries - 1 || !isRetryable(e)) {
                    System.err.println(type + " failed after retries: " + e.getMessage());
                    throw e;
                }
                try {
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
                e.getMessage().contains("Deadlock detected");
    }
}