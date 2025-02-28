package com.example.kiantask.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BANK_ACCOUNT")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ACCOUNT_NUMBER", length = 10, unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "ACCOUNT_HOLDER_NAME", length = 100, unique = true)
    private String accountHolderName;

    @Column(name = "BALANCE", nullable = false)
    @Positive(message = "Balance must be greater than zero")
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }
}