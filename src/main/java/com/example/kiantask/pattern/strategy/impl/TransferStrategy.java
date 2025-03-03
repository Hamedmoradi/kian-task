package com.example.kiantask.pattern.strategy.impl;

import com.example.kiantask.domain.BankAccount;
import com.example.kiantask.exceptionHandler.AccountNotFoundException;
import com.example.kiantask.exceptionHandler.DestinationAccountNotFoundException;
import com.example.kiantask.pattern.strategy.TransactionStrategy;
import com.example.kiantask.repository.BankAccountRepository;

import static com.example.kiantask.util.validator.AccountValidator.checkAmount;
import static com.example.kiantask.util.validator.AccountValidator.checkBalance;

public class TransferStrategy implements TransactionStrategy {
    @Override
    public void execute(BankAccountRepository repository, String fromAccountNumber, double amount, String toAccountNumber) {
        BankAccount sourceAccount = repository.findByAccountNumber(fromAccountNumber).orElseThrow(AccountNotFoundException::new);
        BankAccount destinationAccount = repository.findByAccountNumber(toAccountNumber).orElseThrow(DestinationAccountNotFoundException::new);
        checkAmount(amount);
        checkBalance(amount, sourceAccount);
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        repository.save(sourceAccount);
        repository.save(destinationAccount);
    }
}