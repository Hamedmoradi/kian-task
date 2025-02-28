package com.example.kiantask.pattern.strategy.impl;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.exceptionHandler.DestinationAccountNotFoundException;
import com.example.kiantask.exceptionHandler.InsufficientFundsInSourceAccountException;
import com.example.kiantask.pattern.strategy.TransactionStrategy;
import com.example.kiantask.repository.BankAccountRepository;

public class TransferStrategy implements TransactionStrategy {
    @Override
    public void execute(BankAccountRepository repository, String fromAccountNumber, double amount, String toAccountNumber) throws Exception {
        BankAccount sourceAccount = repository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(AccountNotFoundException::new);
        BankAccount destinationAccount = repository.findByAccountNumber(toAccountNumber)
                .orElseThrow(DestinationAccountNotFoundException::new);
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientFundsInSourceAccountException();
        }
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        repository.save(sourceAccount);
        repository.save(destinationAccount);
    }
}