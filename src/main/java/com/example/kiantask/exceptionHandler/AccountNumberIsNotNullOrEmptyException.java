package com.example.kiantask.exceptionHandler;

import com.example.kiantask.enums.GeneralExceptionEnums;

public class AccountNumberIsNotNullOrEmptyException extends GeneralException {
    public AccountNumberIsNotNullOrEmptyException() {
        super(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode());
    }

    public AccountNumberIsNotNullOrEmptyException(Throwable e) {
        super(GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.ACCOUNT_NUMBER_CAN_NOT_BE_NULL_OR_EMPTY_EXCEPTION_CODE.getCode(), e);
    }
}
