package com.example.kiantask.repository;

import com.example.kiantask.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<BankAccount> findByAccountNumber(String accountNumber);
}