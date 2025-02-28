package com.example.kiantask.exceptionHandler;


import com.example.kiantask.enums.GeneralExceptionEnums;

public class AccountNumberIsAlreadyExistException extends GeneralException {
    public AccountNumberIsAlreadyExistException() {
        super(GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getCode());
    }

    public AccountNumberIsAlreadyExistException(Throwable e) {
        super(GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_NUMBER_ALREADY_EXIST_EXCEPTION_CODE.getCode(), e);
    }
}
