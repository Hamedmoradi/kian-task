package com.example.kiantask.pattern.observer.impl;

import com.example.kiantask.pattern.observer.TransactionObserver;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.time.LocalDateTime;

public class TransactionLogger implements TransactionObserver {
    private static final String LOG_FILE = "transactions.log";

    @Override
    @SneakyThrows
    public void onTransaction(String accountNumber, String transactionType, double amount) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String logEntry = String.format("%s | %s | %s | %.2f%n", LocalDateTime.now(), accountNumber, transactionType, amount);
            writer.write(logEntry);
        }
    }
}