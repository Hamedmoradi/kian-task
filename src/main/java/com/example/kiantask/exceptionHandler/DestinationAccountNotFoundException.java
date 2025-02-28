package com.example.kiantask.exceptionHandler;


import com.example.kiantask.enums.GeneralExceptionEnums;

public class DestinationAccountNotFoundException extends GeneralException {
    public DestinationAccountNotFoundException() {
        super(GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode());
    }

    public DestinationAccountNotFoundException(Throwable e) {
        super(GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(), e);
    }
}