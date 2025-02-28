//package com.example.kiantask.service;
//
//import com.example.kiantask.domain.BankAccount;
//import com.example.kiantask.pattern.observer.TransactionObserver;
//import com.example.kiantask.pattern.observer.impl.TransactionLogger;
//import com.example.kiantask.pattern.strategy.TransactionStrategy;
//import com.example.kiantask.pattern.strategy.impl.DepositStrategy;
//import com.example.kiantask.pattern.strategy.impl.TransferStrategy;
//import com.example.kiantask.pattern.strategy.impl.WithdrawalStrategy;
//import com.example.kiantask.repository.BankAccountRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Service
//public class Bank {
//    @Autowired
//    private BankAccountRepository repository;
//    private final List<TransactionObserver> observers = new ArrayList<>();
//    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//    public Bank() {
//        observers.add(new TransactionLogger());
//    }
//
//    @Transactional
//    public BankAccount createAccount(String accountNumber, String accountHolderName, double initialBalance) throws IllegalArgumentException {
//        if (accountNumber == null || accountNumber.trim().isEmpty()) {
//            throw new IllegalArgumentException("Account number cannot be null or empty");
//        }
//        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
//            throw new IllegalArgumentException("Account holder name cannot be null or empty");
//        }
//        // No manual balance check; @Positive will handle it
//        if (repository.existsByAccountNumber(accountNumber)) {
//            throw new IllegalStateException("Account number already exists");
//        }
//        BankAccount account = new BankAccount(accountNumber, accountHolderName, initialBalance);
//        return repository.save(account); // JPA will assign an auto-incremented id
//    }
//
//    @Transactional
//    public synchronized void performTransaction(TransactionStrategy strategy, String accountNumber, double amount, String targetAccountNumber, String transactionType) throws Exception {
//        if (amount <= 0) {
//            throw new IllegalArgumentException("Transaction amount must be positive");
//        }
//        strategy.execute(repository, accountNumber, amount, targetAccountNumber);
//        notifyObservers(accountNumber, transactionType, amount);
//        if ("TRANSFER".equals(transactionType) && targetAccountNumber != null) {
//            notifyObservers(targetAccountNumber, "TRANSFER_IN", amount);
//        }
//    }
//
//    public double getBalance(String accountNumber) throws IllegalArgumentException {
//        if (accountNumber == null || accountNumber.trim().isEmpty()) {
//            throw new IllegalArgumentException("Account number cannot be null or empty");
//        }
//        return repository.findByAccountNumber(accountNumber)
//                .map(BankAccount::getBalance)
//                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
//    }
//
//    private void notifyObservers(String accountNumber, String transactionType, double amount) {
//        executorService.submit(() -> {
//            for (TransactionObserver observer : observers) {
//                try {
//                    observer.onTransaction(accountNumber, transactionType, amount);
//                } catch (Exception e) {
//                    System.err.println("Failed to notify observer: " + e.getMessage());
//                }
//            }
//        });
//    }
//
//    public void deposit(String accountNumber, double amount) throws Exception {
//        performTransaction(new DepositStrategy(), accountNumber, amount, null, "DEPOSIT");
//    }
//
//    public void withdraw(String accountNumber, double amount) throws Exception {
//        performTransaction(new WithdrawalStrategy(), accountNumber, amount, null, "WITHDRAWAL");
//    }
//
//    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) throws Exception {
//        if (fromAccountNumber.equals(toAccountNumber)) {
//            throw new IllegalArgumentException("Cannot transfer to the same account");
//        }
//        performTransaction(new TransferStrategy(), fromAccountNumber, amount, toAccountNumber, "TRANSFER");
//    }
//}

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
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Transactional
    @SneakyThrows
    public void performTransaction(TransactionStrategy strategy, String accountNumber, double amount, String targetAccountNumber, String transactionType) {
        checkAmount(amount);
        strategy.execute(repository, accountNumber, amount, targetAccountNumber);
        notifyObservers(accountNumber, transactionType, amount);
        if (TransactionTypeEnum.TRANSFER.getValue().equals(transactionType) && targetAccountNumber != null) {
            notifyObservers(targetAccountNumber, TransactionTypeEnum.TRANSFER_IN.getValue(), amount);
        }
    }


    public double getBalance(String accountNumber) throws IllegalArgumentException {
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
}