package com.example.kiantask.exceptionHandler;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final Integer code;

    public GeneralException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public GeneralException(String message, Integer code, Throwable e) {
        super(message, e);
        this.code = code;
    }
}

