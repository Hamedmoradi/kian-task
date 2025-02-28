package com.example.kiantask.exceptionHandler;


import com.example.kiantask.enums.GeneralExceptionEnums;

public class AccountHolderIsNotNullOrEmptyException extends GeneralException {
    public AccountHolderIsNotNullOrEmptyException() {
        super(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode());
    }

    public AccountHolderIsNotNullOrEmptyException(Throwable e) {
        super(GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_HOLDER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode(), e);
    }
}
