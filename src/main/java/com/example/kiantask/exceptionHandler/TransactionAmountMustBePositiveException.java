package com.example.kiantask.exceptionHandler;


import com.example.kiantask.enums.GeneralExceptionEnums;

public class TransactionAmountMustBePositiveException extends GeneralException {
    public TransactionAmountMustBePositiveException() {
        super(GeneralExceptionEnums.TRANSACTION_AMOUNT_MUST_BE_POSITIVE_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.TRANSACTION_AMOUNT_MUST_BE_POSITIVE_EXCEPTION_CODE.getCode());
    }

    public TransactionAmountMustBePositiveException(Throwable e) {
        super(GeneralExceptionEnums.TRANSACTION_AMOUNT_MUST_BE_POSITIVE_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.TRANSACTION_AMOUNT_MUST_BE_POSITIVE_EXCEPTION_CODE.getCode(), e);
    }
}
