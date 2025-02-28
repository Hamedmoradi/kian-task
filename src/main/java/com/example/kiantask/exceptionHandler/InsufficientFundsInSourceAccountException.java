package com.example.kiantask.exceptionHandler;


import com.example.kiantask.enums.GeneralExceptionEnums;

public class InsufficientFundsInSourceAccountException extends GeneralException {
    public InsufficientFundsInSourceAccountException() {
        super(GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getCode());
    }

    public InsufficientFundsInSourceAccountException(Throwable e) {
        super(GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.INSUFFICIENT_FUNDS_IN_SOURCE_ACCOUNT_EXCEPTION_CODE.getCode(), e);
    }
}
