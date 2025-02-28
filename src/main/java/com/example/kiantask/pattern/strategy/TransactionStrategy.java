package com.example.kiantask.pattern.strategy;

import com.example.kiantask.repository.BankAccountRepository;

public interface TransactionStrategy {
    void execute(BankAccountRepository repository, String accountNumber, double amount, String targetAccountNumber) throws Exception;
}