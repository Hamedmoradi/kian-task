package com.example.kiantask;

import com.example.kiantask.service.Bank;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
public class BankingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(Bank bank, Environment env) {
        return args -> {
            if (!java.util.Arrays.asList(env.getActiveProfiles()).contains("test")) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose an option: ");
                while (true) {
                    displayMenu();
                    try {
                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.print("Account Number: ");
                                String accNum = scanner.nextLine();
                                System.out.print("Holder Name: ");
                                String name = scanner.nextLine();
                                System.out.print("Initial Balance: ");
                                double balance = scanner.nextDouble();
                                bank.createAccount(accNum, name, balance);
                                System.out.println("Account created!");
                                break;
                            case 2:
                                System.out.print("Account Number: ");
                                String depositAcc = scanner.nextLine();
                                System.out.print("Amount: ");
                                double depositAmt = scanner.nextDouble();
                                bank.deposit(depositAcc, depositAmt);
                                System.out.println("Deposit successful!");
                                break;
                            case 3:
                                System.out.print("Account Number: ");
                                String withdrawAcc = scanner.nextLine();
                                System.out.print("Amount: ");
                                double withdrawAmt = scanner.nextDouble();
                                bank.withdraw(withdrawAcc, withdrawAmt);
                                System.out.println("Withdrawal successful!");
                                break;
                            case 4:
                                System.out.print("From Account Number: ");
                                String fromAcc = scanner.nextLine();
                                System.out.print("To Account Number: ");
                                String toAcc = scanner.nextLine();
                                System.out.print("Amount: ");
                                double transferAmt = scanner.nextDouble();
                                bank.transfer(fromAcc, toAcc, transferAmt);
                                System.out.println("Transfer successful!");
                                break;
                            case 5:
                                System.out.print("Account Number: ");
                                String checkAcc = scanner.nextLine();
                                System.out.println("Balance: " + bank.getBalance(checkAcc));
                                break;
                            case 6:
                                System.out.println("Exiting...");
                                scanner.close();
                                System.exit(0);
                            default:
                                System.out.println("Invalid option! Please choose between 1 and 6.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Please enter a valid number.");
                        scanner.nextLine();
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                        scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        scanner.nextLine();
                    }
                }
            }
        };
    }

    private void displayMenu() {
        System.out.println(" 1. Create Account | 2. Deposit | 3. Withdraw | 4. Transfer | 5. Check Balance | 6. Exit ");
    }
}