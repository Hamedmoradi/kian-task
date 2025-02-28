package com.example.kiantask.exceptionHandler;

import com.example.kiantask.enums.GeneralExceptionEnums;

public class InsufficientFundsException extends GeneralException {
    public InsufficientFundsException() {
        super(GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getCode());
    }

    public InsufficientFundsException(Throwable e) {
        super(GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.INSUFFICIENT_FUNDS_EXCEPTION_CODE.getCode(), e);
    }
}
