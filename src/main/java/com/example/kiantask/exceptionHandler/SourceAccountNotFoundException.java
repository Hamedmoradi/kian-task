package com.example.kiantask.exceptionHandler;


import com.example.kiantask.enums.GeneralExceptionEnums;

public class SourceAccountNotFoundException extends GeneralException {
    public SourceAccountNotFoundException() {
        super(GeneralExceptionEnums.SOURCE_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.SOURCE_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode());
    }

    public SourceAccountNotFoundException(Throwable e) {
        super(GeneralExceptionEnums.SOURCE_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.SOURCE_ACCOUNT_NOT_FOUND_EXCEPTION_CODE.getCode(), e);
    }
}