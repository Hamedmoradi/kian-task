package com.example.kiantask.pattern.observer;

import java.io.IOException;

public interface TransactionObserver {
    void onTransaction(String accountNumber, String transactionType, double amount) throws IOException;
}