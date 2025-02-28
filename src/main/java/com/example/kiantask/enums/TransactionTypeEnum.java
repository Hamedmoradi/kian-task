package com.example.kiantask.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionTypeEnum {
    TRANSFER("TRANSFER"),
    TRANSFER_IN("TRANSFER_IN"),
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
    ;
    private final String value;
}
