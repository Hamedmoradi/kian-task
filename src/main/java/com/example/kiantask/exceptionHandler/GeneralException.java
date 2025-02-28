package com.example.kiantask.exceptionHandler;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    public static final String SERVICE_NAME = "GENERAL_SERVICE";

    private String serviceName;
    private String exceptionDetails = "";
    private String localeExceptionMessage = "";

    private Integer messageCode;

    public GeneralException(String message, Integer messageCode) {
        super(message);
        this.serviceName = SERVICE_NAME;
        this.messageCode = messageCode;
    }

    public GeneralException(String message, Integer messageCode, Throwable e) {
        super(message, e);
        this.serviceName = SERVICE_NAME;
        this.messageCode = messageCode;
    }

    public GeneralException(String serviceName, String message, Integer messageCode) {
        super(message);
        this.serviceName = serviceName;
        this.messageCode = messageCode;
    }

    public GeneralException(String serviceName, String message, Integer messageCode, Throwable e) {
        super(message, e);
        this.serviceName = serviceName;
        this.messageCode = messageCode;
    }

    public GeneralException(
            @JsonProperty("serviceName") String serviceName,
            @JsonProperty("message") String message,
            @JsonProperty("messageCode") Integer messageCode,
            @JsonProperty("localeExceptionMessage") String localeExceptionMessage,
            @JsonProperty("exceptionDetails") String exceptionDetails,
            @JsonProperty("e") Throwable e) {
        super(message, e);
        this.serviceName = serviceName;
        this.localeExceptionMessage = localeExceptionMessage;
        this.exceptionDetails = exceptionDetails;
        this.messageCode = messageCode;
    }

}

