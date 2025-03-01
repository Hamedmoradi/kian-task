package com.example.kiantask.pattern.observer;

public interface TransactionObserver {
    void onTransaction(String accountNumber, String transactionType, double amount);
}