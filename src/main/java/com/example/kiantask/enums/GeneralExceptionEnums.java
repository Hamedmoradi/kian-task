package com.example.kiantask.enums;

import lombok.Getter;

@Getter
public enum GeneralExceptionEnums {
    ACCOUNT_NOT_FOUND_EXCEPTION_CODE(100001, "account not found"),
    ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE(100002, "Account number cannot be null or empty"),
    ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE(100003, "Account holder name cannot be null or empty"),
    ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE(100004, "Account number already exists"),
    TRANSACTION_AMOUNT_MUST_BE_POSITIVE_EXCEPTION_CODE(100005, "Transaction amount must be positive"),
    SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE(100006, "Cannot transfer to the same account"),
    INSUFFICIENT_FUNDS_EXCEPTION_CODE(100007, "Insufficient funds"),
    SOURCE_ACCOUNT_NOT_FOUND_EXCEPTION_CODE(100008, "source account not found"),
    DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE(100009, "destination account not found"),
    INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE(100010, "Insufficient funds in source account"),

    ;
    private final String message;
    private final int code;

    GeneralExceptionEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
