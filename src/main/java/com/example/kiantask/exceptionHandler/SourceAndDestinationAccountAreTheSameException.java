package com.example.kiantask.exceptionHandler;

import com.example.kiantask.enums.GeneralExceptionEnums;

public class SourceAndDestinationAccountAreTheSameException extends GeneralException {
    public SourceAndDestinationAccountAreTheSameException() {
        super(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getCode());
    }

    public SourceAndDestinationAccountAreTheSameException(Throwable e) {
        super(GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getMessage(), GeneralExceptionEnums.SOURCE_AND_DESTINATION_ACCOUNT_ARE_THE_SAME_EXCEPTION_CODE.getCode(), e);
    }
}
