package com.example.kiantask.exceptionHandler;

import com.example.kiantask.enums.GeneralExceptionEnums;

public class AccountNotFoundException extends GeneralException {
    public AccountNotFoundException() {
        super(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode());
    }

    public AccountNotFoundException(Throwable e) {
        super(GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(), e);
    }
}
