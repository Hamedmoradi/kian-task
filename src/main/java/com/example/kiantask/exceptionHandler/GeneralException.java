package com.example.kiantask.exceptionHandler;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final Integer messageCode;

    public GeneralException(String message, Integer messageCode) {
        super(message);
        this.messageCode = messageCode;
    }

    public GeneralException(String message, Integer messageCode, Throwable e) {
        super(message, e);
        this.messageCode = messageCode;
    }
}

