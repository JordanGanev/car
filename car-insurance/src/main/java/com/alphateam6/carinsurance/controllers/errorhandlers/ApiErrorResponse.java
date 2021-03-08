package com.alphateam6.carinsurance.controllers.errorhandlers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {

    private String uri;
    private String message;
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    public ApiErrorResponse(String uri, String message, int status) {
        super();
        this.uri = uri;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
