package com.example.kiantask.pattern.strategy.impl;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.pattern.strategy.TransactionStrategy;
import com.example.kiantask.repository.BankAccountRepository;

import static com.example.kiantask.util.validator.AccountValidator.checkAmount;

public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(BankAccountRepository repository, String accountNumber, double amount, String targetAccountNumber) {
        checkAmount(amount);
        BankAccount account = repository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
        account.setBalance(account.getBalance() + amount);
        repository.save(account);
    }
}