package com.example.kiantask.pattern.strategy.impl;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.pattern.strategy.TransactionStrategy;
import com.example.kiantask.repository.BankAccountRepository;

import static com.example.kiantask.util.validator.AccountValidator.checkAmount;
import static com.example.kiantask.util.validator.AccountValidator.checkBalance;

public class WithdrawalStrategy implements TransactionStrategy {
    @Override
    public void execute(BankAccountRepository repository, String accountNumber, double amount, String targetAccountNumber) {
        BankAccount account = repository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
        checkAmount(amount);
        checkBalance(amount, account);
        account.setBalance(account.getBalance() - amount);
        repository.save(account);
    }
}
